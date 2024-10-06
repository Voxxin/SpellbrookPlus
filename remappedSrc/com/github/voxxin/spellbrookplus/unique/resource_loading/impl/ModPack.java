package com.github.voxxin.spellbrookplus.unique.resource_loading.impl;


import java.util.Set;
import java.util.function.Predicate;

public interface ModPack {
    default boolean mod_isHidden() {
        return false;
    }

    default boolean mod_parentsEnabled(Set<String> enabled) {
        return true;
    }

    default void mod_setParentsPredicate(Predicate<Set<String>> predicate) {
    }
}

