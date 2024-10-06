package com.github.voxxin.spellbrookplus.unique.resource_loading.impl;

import net.minecraft.server.packs.repository.PackSource;
import org.slf4j.LoggerFactory;

public interface ModResource {
    default PackSource getModPackSource() {
        LoggerFactory.getLogger(ModResource.class).error("Unknown Resource implementation {}, returning DEFAULT as the source", getClass().getName());
        return PackSource.DEFAULT;
    }
}
