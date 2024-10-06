package com.github.voxxin.spellbrookplus.unique.resource_loading.api;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.util.perf.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface SimpleResourceReloadListener<T> extends IdentifiablePackReloadListener {
    @Override
    default CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier helper, ResourceManager manager, Profiler loadProfiler, Profiler applyProfiler, Executor loadExecutor, Executor applyExecutor) {
        return load(manager, loadProfiler, loadExecutor).thenCompose(helper::wait).thenCompose(
                (o) -> apply(o, manager, applyProfiler, applyExecutor)
        );
    }

    CompletableFuture<T> load(ResourceManager manager, Profiler profiler, Executor executor);

    CompletableFuture<Void> apply(T data, ResourceManager manager, Profiler profiler, Executor executor);
}