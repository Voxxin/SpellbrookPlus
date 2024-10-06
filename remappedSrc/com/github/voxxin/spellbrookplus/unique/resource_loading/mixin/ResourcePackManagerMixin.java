package com.github.voxxin.spellbrookplus.unique.resource_loading.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(PackRepository.class)
public abstract class PackRepositoryMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("PackRepositoryMixin");

    @Shadow
    @Final
    @Mutable
    public Set<RepositorySource> sources;

    @Shadow
    private Map<String, Pack> available;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void construct(RepositorySource[] repositorySources, CallbackInfo info) {
        sources = new LinkedHashSet<>(sources);

        // Search pack sources to find any server-related source.
        boolean shouldAddServerSource = false;

        for (RepositorySource source : this.sources) {
            if (source instanceof FolderRepositorySource
                    && (((FolderRepositorySource) source).type == PackType.WORLD
                    || ((FolderRepositorySource) source).type == PackType.SERVER)) {
                shouldAddServerSource = true;
                break;
            }
        }

        // On server, add the mod pack source.
        if (shouldAddServerSource) {
            sources.add(new ModPackCreator(PackType.SERVER_DATA));
        }
    }

    @Inject(method = "rebuildSelected", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;", shift = At.Shift.BEFORE))
    private void handleAutoEnableDisable(Collection<String> enabledNames, CallbackInfoReturnable<List<Pack>> cir, @Local List<Pack> enabledAfterFirstRun) {
        ModPackUtil.refreshAutoEnabledPacks(enabledAfterFirstRun, this.available);
    }

    @Inject(method = "addPack", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER))
    private void handleAutoEnable(String profile, CallbackInfoReturnable<Boolean> cir, @Local List<Pack> newlyEnabled) {
        if (ModPackCreator.POST_CHANGE_HANDLE_REQUIRED.contains(profile)) {
            ModPackUtil.refreshAutoEnabledPacks(newlyEnabled, this.available);
        }
    }

    @Inject(method = "removePack", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z"))
    private void handleAutoDisable(String profile, CallbackInfoReturnable<Boolean> cir, @Local List<Pack> enabled) {
        if (ModPackCreator.POST_CHANGE_HANDLE_REQUIRED.contains(profile)) {
            Set<String> currentlyEnabled = enabled.stream().map(Pack::getId).collect(Collectors.toSet());
            enabled.removeIf(p -> !((FabricPack) p).fabric_parentsEnabled(currentlyEnabled));
            LOGGER.debug("[Fabric] Internal pack auto-removed upon disabling {}, result: {}", profile, enabled.stream().map(Pack::getId).toList());
        }
    }
}

