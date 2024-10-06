package com.github.voxxin.spellbrookplus.unique.resource_loading.mixin;

import com.github.voxxin.spellbrookplus.unique.resource_loading.impl.ModPack;
import com.github.voxxin.spellbrookplus.unique.resource_loading.impl.PackSourceTracker;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.function.Predicate;

@Mixin(Pack.class)
abstract class PackMixin implements ModPack {
    @Unique
    private static final Predicate<Set<String>> DEFAULT_PARENT_PREDICATE = parents -> true;
    @Shadow
    @Final
    private PackSource packSource;
    @Unique
    private Predicate<Set<String>> parentsPredicate = DEFAULT_PARENT_PREDICATE;

    @Inject(method = "open", at = @At("RETURN"))
    private void onOpen(CallbackInfoReturnable<Pack> info) {
        PackSourceTracker.setSource(info.getReturnValue().open(), packSource);
    }

    @Override
    public boolean mod_isHidden() {
        return parentsPredicate != DEFAULT_PARENT_PREDICATE;
    }

    @Override
    public boolean mod_parentsEnabled(Set<String> enabled) {
        return parentsPredicate.test(enabled);
    }

    @Override
    public void mod_setParentsPredicate(Predicate<Set<String>> predicate) {
        this.parentsPredicate = predicate;
    }
}
