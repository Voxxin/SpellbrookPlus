package com.github.voxxin.spellbrookplus.unique.resource_loading.impl;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class ModPackUtil {
    public static final Gson GSON = new Gson();
    private static final Logger LOGGER = LoggerFactory.getLogger(ModPackUtil.class);

    private ModPackUtil() {
    }

    public static void appendModPacks(List<PackResources> packs, PackType type, @Nullable String subPath) {
        for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
            if (container.getMetadata().getType().equals("mod_")) {
                continue;
            }

            PackResources pack = ModNioResourcePack.create(container.getMetadata().getId(), container, subPath, type, PackSource.DEFAULT, true);

            if (pack != null) {
                packs.add(pack);
            }
        }
    }

    public static void refreshAutoEnabledPacks(List<Pack> enabledProfiles, Map<String, PackMetadataSection> allProfiles) {
        LOGGER.debug("[Fabric] Starting internal pack sorting with: {}", enabledProfiles.stream().map(Pack::getId).toList());
        enabledProfiles.removeIf(profile -> ((ModResourcePackProfile) profile).mod_isHidden());
        LOGGER.debug("[Fabric] Removed all internal packs, result: {}", enabledProfiles.stream().map(Pack::getId).toList());
        ListIterator<Pack> it = enabledProfiles.listIterator();
        Set<String> seen = new LinkedHashSet<>();

        while (it.hasNext()) {
            Pack profile = it.next();
            seen.add(profile.getId());

            for (Pack p : allProfiles.values()) {
                ModResourcePackProfile pack = (ModResourcePackProfile) p;

                if (pack.mod_isHidden() && pack.mod_parentsEnabled(seen) && seen.add(p.getId())) {
                    it.add(p);
                    LOGGER.debug("[Fabric] cur @ {}, auto-enabled {}, currently enabled: {}", profile.getId(), p.getId(), seen);
                }
            }
        }

        LOGGER.debug("[Fabric] Final sorting result: {}", enabledProfiles.stream().map(Pack::getId).toList());
    }

    public static boolean containsDefault(String filename, boolean modBundled) {
        return "pack.mcmeta".equals(filename) || (modBundled && "pack.png".equals(filename));
    }

    public static InputStream getDefaultIcon() throws IOException {
        Optional<Path> loaderIconPath = FabricLoader.getInstance().getModContainer("spellbrokplus")
                .flatMap(resourceLoaderContainer -> resourceLoaderContainer.getMetadata().getIconPath(512).flatMap(resourceLoaderContainer::findPath));

        if (loaderIconPath.isPresent()) {
            return Files.newInputStream(loaderIconPath.get());
        }

        return null;
    }

    public static InputStream openDefault(ModContainer container, PackType type, String filename) throws IOException {
        switch (filename) {
            case "pack.mcmeta":
                String description = Objects.requireNonNullElse(container.getMetadata().getId(), "");
                String metadata = serializeMetadata(SharedConstants.getCurrentVersion().getPackVersion(type), description);
                return IOUtils.toInputStream(metadata, Charsets.UTF_8);
            case "pack.png":
                Optional<Path> path = container.getMetadata().getIconPath(512).flatMap(container::findPath);

                if (path.isPresent()) {
                    return Files.newInputStream(path.get());
                } else {
                    return getDefaultIcon();
                }
            default:
                return null;
        }
    }

    public static PackMetadataSection getMetadataPack(int packVersion, Component description) {
        return new PackMetadataSection(description, packVersion, Optional.empty());
    }

    public static JsonObject getMetadataPackJson(int packVersion, Component description) {
        return PackMetadataSection.TYPE.toJson(getMetadataPack(packVersion, description));
    }

    public static String serializeMetadata(int packVersion, String description) {
        JsonObject pack = getMetadataPackJson(packVersion, Component.literal(description));
        JsonObject metadata = new JsonObject();
        metadata.add("pack", pack);
        return GSON.toJson(metadata);
    }

    public static Component getName(ModMetadata info) {
        if (info.getId() != null) {
            return Component.literal(info.getId());
        } else {
            return Component.translatable("pack.name.mod", info.getId());
        }
    }

    public static WorldDataConfiguration createDefaultDataConfiguration() {
        ModPackCreator modPackCreator = new ModPackCreator(PackType.SERVER_DATA);
        List<Pack.Info> moddedPacks = new ArrayList<>();
        modPackCreator.register(moddedPacks::add);

        List<String> enabled = new ArrayList<>(DataPacks.DEFAULT.getEnabled());
        List<String> disabled = new ArrayList<>(DataPacks.DEFAULT.getDisabled());

        for (Pack.Info profile : moddedPacks) {
            if (profile.getSource() == ModPackCreator.PACK_SOURCE) {
                enabled.add(profile.getId());
                continue;
            }

            try (Pack pack = profile.createPack()) {
                if (pack instanceof ModNioResourcePack && ((ModNioResourcePack) pack).getActivationType().shouldAddAutomatically()) {
                    enabled.add(profile.getId());
                } else {
                    disabled.add(profile.getId());
                }
            }
        }

        return new WorldDataConfiguration(
                new DataPackConfig(enabled, disabled),
                FeatureFlags.DEFAULT_FLAGS
        );
    }

    public static DataPackConfig createTestServerSettings(List<String> enabled, List<String> disabled) {
        Set<String> moddedProfiles = new HashSet<>();
        ModPackCreator modPackCreator = new ModPackCreator(PackType.SERVER_DATA);
        modPackCreator.register(profile -> moddedProfiles.add(profile.getId()));

        List<String> moveToTheEnd = new ArrayList<>();

        for (Iterator<String> it = enabled.iterator(); it.hasNext();) {
            String profile = it.next();

            if (moddedProfiles.contains(profile)) {
                moveToTheEnd.add(profile);
                it.remove();
            }
        }

        enabled.addAll(moveToTheEnd);

        return new DataPackConfig(enabled, disabled);
    }
}
```