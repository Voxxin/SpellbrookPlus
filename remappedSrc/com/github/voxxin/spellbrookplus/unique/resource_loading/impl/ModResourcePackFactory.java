package com.github.voxxin.spellbrookplus.unique.resource_loading.impl;

import net.minecraft.server.packs.CompositePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;

import java.util.ArrayList;
import java.util.List;

public record ModResourcePackFactory(PackResources pack) implements Pack.ResourcesSupplier {
    @Override
    public PackResources openPrimary(String var1) {
        return pack;
    }

    @Override
    public PackResources openFull(String var1, Pack.Info metadata) {
        if (metadata.overlays().isEmpty()) {
            return pack;
        } else {
            List<PackResources> overlays = new ArrayList<>(metadata.overlays().size());

            for (String overlay : metadata.overlays()) {
                overlays.add(pack(overlay));
            }

            return new CompositePackResources(pack, overlays);
        }
    }
}
