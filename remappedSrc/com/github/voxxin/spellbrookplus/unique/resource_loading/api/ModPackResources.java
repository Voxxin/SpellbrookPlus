package com.github.voxxin.spellbrookplus.unique.resource_loading.api;

import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;

public interface ModPackResources extends PackResources {
    ModMetadata getModMetadata();

    ModPackResources createOverlay(String overlay);
}
