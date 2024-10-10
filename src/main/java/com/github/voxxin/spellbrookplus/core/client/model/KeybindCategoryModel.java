package com.github.voxxin.spellbrookplus.core.client.model;

public enum KeybindCategoryModel {
    SPELLBROOK_PLUS (
            "categories.spellbrookplus"
    ),

    QUICK_CASTS(
            "categories.spellbrookplus.quick_casts"
    ),;

    public final String translationString;

    KeybindCategoryModel(String translationString) {
        this.translationString = translationString;
    }
}
