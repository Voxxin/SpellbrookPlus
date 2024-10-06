package com.github.voxxin.spellbrookplus.unique.resource_loading.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.util.perf.Profiler;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface IdentifiablePackReloadListener extends PreparableReloadListener {
    ResourceLocation getModId();

    default Collection<ResourceLocation> getModDependencies() {
        return Collections.emptyList();
    }
}

