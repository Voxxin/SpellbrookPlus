package com.github.voxxin.spellbrookplus.unique.resource_loading.api;

public enum ResourcePackActivationType {
    NORMAL,
    DEFAULT_ENABLED,
    ALWAYS_ENABLED;

    public boolean isEnabledByDefault() {
        return this == DEFAULT_ENABLED || this == ALWAYS_ENABLED;
    }
}

