package com.github.voxxin.spellbrookplus.unique.resource_loading.impl;

import com.github.voxxin.spellbrookplus.unique.resource_loading.api.ModPackResources;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ModResourcePackCreator implements RepositorySource {
    public static final String MOD = "mod";
    private static final String PROGRAMMER_ART = "programmer_art";
    private static final String HIGH_CONTRAST = "high_contrast";
    public static final Set<String> POST_CHANGE_HANDLE_REQUIRED = Set.of(MOD, PROGRAMMER_ART, HIGH_CONTRAST);
    @VisibleForTesting
    public static final Predicate<Set<String>> BASE_PARENT = enabled -> enabled.contains(MOD);
    @VisibleForTesting
    public static final Predicate<Set<String>> PROGRAMMER_ART_PARENT = enabled -> enabled.contains(MOD) && enabled.contains(PROGRAMMER_ART);
    @VisibleForTesting
    public static final Predicate<Set<String>> HIGH_CONTRAST_PARENT = enabled -> enabled.contains(MOD) && enabled.contains(HIGH_CONTRAST);
    public static final PackSource PACK_SOURCE = new PackSource() {
        @Override
        public Component decorate(Component packName) {
            return Component.translatable("pack.nameAndSource", packName, Component.translatable("pack.source.mod"));
        }

        @Override
        public boolean shouldAddAutomatically() {
            return true;
        }
    };
    public static final ModResourcePackCreator CLIENT_RESOURCE_PACK_PROVIDER = new ModResourcePackCreator(PackType.CLIENT_RESOURCES);
    private static final PackSource ACTIVATION_INFO = new PackSource(true, Pack.Position.TOP, false);

    private final PackType type;

    public ModResourcePackCreator(PackType type) {
        this.type = type;
    }

    @Override
    public void loadPacks(Consumer<Pack> consumer) {
        PackResources metadata = new PackResources(
                MOD,
                Component.translatable("pack.name.mods"),
                PACK_SOURCE,
                Optional.empty()
        );

        consumer.accept(new PackRepository(
                metadata,
                new PlaceholderResourcePack.Factory(this.type, metadata),
                this.type,
                ACTIVATION_INFO
        ));

        registerModPack(consumer, null, BASE_PARENT);

        if (this.type == PackType.CLIENT_RESOURCES) {
            registerModPack(consumer, PROGRAMMER_ART, PROGRAMMER_ART_PARENT);
            registerModPack(consumer, HIGH_CONTRAST, HIGH_CONTRAST_PARENT);
        }

        FolderRepositorySource.registerBuiltinPacks(this.type, consumer);
    }

    private void registerModPack(Consumer<Pack> consumer, @Nullable String subPath, Predicate<Set<String>> parents) {
        List<PackResources> packs = new ArrayList<>();
        ModPackUtil.appendModPacks(packs, this.type, subPath);

        for (PackResources pack : packs) {
            Pack profile = new PackResources(
                    pack.getInfo(),
                    new ModResourcePackFactory(pack),
                    this.type,
                    ACTIVATION_INFO
            );

            if (profile != null) {
                ((AbstractPackResources) profile).shouldAddAutomatically = parents;
                consumer.accept(profile);
            }
        }
    }
}
