package com.github.voxxin.spellbrookplus.unique.resource_loading.impl;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;

import java.util.WeakHashMap;


public final class PackSourceTracker {
    private static final WeakHashMap<Pack, PackSource> SOURCES = new WeakHashMap<>();

    public static PackSource getSource(PackResources pack) {
        return SOURCES.getOrDefault(pack, PackSource.DEFAULT);
    }

    public static void setSource(PackResources pack, PackSource source) {
        SOURCES.put((Pack) pack, source);
    }
}
