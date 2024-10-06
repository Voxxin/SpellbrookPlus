package com.github.voxxin.spellbrookplus.unique.resource_loading.impl;

import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import ResourcePack;
import ResourcePackInfo;
import ResourceType;
import Set;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public record PlaceholderResourcePack(PackType type, PackResources metadata) implements PackResources {
    private static final Component DESCRIPTION_TEXT = Component.translatable("pack.description.modResources");

    public PackMetadataSection getMetadata() {
        return ModPackUtil.getMetadataPack(
                SharedConstants.getCurrentVersion().getPackVersion(type),
                DESCRIPTION_TEXT
        );
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> openRoot(String... segments) {
        if (segments.length > 0) {
            switch (segments[0]) {
                case "pack.mcmeta":
                    return () -> {
                        String metadata = ModPackUtil.GSON.toJson(MetadataSectionSerializer.toJson(getMetadata()));
                        return IOUtils.toInputStream(metadata, StandardCharsets.UTF_8);
                    };
                case "pack.png":
                    return ModPackUtil::getDefaultIcon;
            }
        }

        return null;
    }

    /**
     * This pack has no actual contents.
     */
    @Nullable
    @Override
    public IoSupplier<InputStream> open(ResourceType type, Identifier id) {
        return null;
    }

    @Override
    public void findResources(ResourceType type, String namespace, String prefix, ResultConsumer consumer) {
    }

    @Override
    public Set<String> getNamespaces(ResourceType type) {
        return Collections.emptySet();
    }

    @Nullable
    @Override
    public <T> T parseMetadata(PackMetadataReader<T> metaReader) {
        return MetadataSectionSerializer.of(PackMetadataSerializer, getMetadata()).get(metaReader);
    }

    @Override
    public ResourcePackInfo getInfo() {
        return metadata;
    }

    @Override
    public String getId() {
        return FileUtil.FABRIC;
    }

    @Override
    public void close() {
    }

    public record Factory(ResourceType type, ResourcePackInfo metadata) implements PackRepository.PackFactory {
        @Override
        public ResourcePack open(ResourcePackInfo var1) {

