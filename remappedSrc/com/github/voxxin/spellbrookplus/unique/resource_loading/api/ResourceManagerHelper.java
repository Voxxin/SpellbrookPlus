package com.github.voxxin.spellbrookplus.unique.resource_loading.api;

import net.fabricmc.loader.api.ModContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface ResourceManagerHelper {

    @Deprecated
    default void addReloadListener(IdentifiablePackReloadListener listener) {
        registerReloadListener(listener);
    }

    void registerReloadListener(IdentifiablePackReloadListener listener);

    static ResourceManagerHelper get(PackType type) {
        return ResourceManagerHelperImpl.get(type);
    }

    static boolean registerBuiltinResourcePack(ResourceLocation id, ModContainer container, ResourcePackActivationType activationType) {
        return ResourceManagerHelperImpl.registerBuiltinResourcePack(id, "resourcepacks/" + id.getPath(), container, activationType);
    }

    static boolean registerBuiltinResourcePack(ResourceLocation id, ModContainer container, Component displayName, ResourcePackActivationType activationType) {
        return ResourceManagerHelperImpl.registerBuiltinResourcePack(id, "resourcepacks/" + id.getPath(), container, displayName, activationType);
    }

    @Deprecated
    static boolean registerBuiltinResourcePack(ResourceLocation id, ModContainer container, String displayName, ResourcePackActivationType activationType) {
        return ResourceManagerHelperImpl.registerBuiltinResourcePack(id, "resourcepacks/" + id.getPath(), container, Component.literal(displayName), activationType);
    }

    @Deprecated
    static boolean registerBuiltinResourcePack(ResourceLocation id, String subPath, ModContainer container, boolean enabledByDefault) {
        return ResourceManagerHelperImpl.registerBuiltinResourcePack(id, subPath, container,
                enabledByDefault ? ResourcePackActivationType.DEFAULT_ENABLED : ResourcePackActivationType.NORMAL);
    }
}