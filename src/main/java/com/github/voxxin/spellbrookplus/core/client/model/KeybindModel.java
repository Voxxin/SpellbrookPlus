package com.github.voxxin.spellbrookplus.core.client.model;

import com.github.voxxin.spellbrookplus.core.mixin.accessors.KeyMappingAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public enum KeybindModel {

    QUICK_CAST_1(
            "key.spellbrookplus.quick_cast.1",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_KP_1,
            KeybindCategoryModel.QUICK_CASTS.translationString
    ),
    QUICK_CAST_2(
            "key.spellbrookplus.quick_cast.2",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_KP_2,
            KeybindCategoryModel.QUICK_CASTS.translationString
    ),
    QUICK_CAST_3(
            "key.spellbrookplus.quick_cast.3",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_KP_3,
            KeybindCategoryModel.QUICK_CASTS.translationString
    ),
    QUICK_CAST_4(
            "key.spellbrookplus.quick_cast.4",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_KP_4,
            KeybindCategoryModel.QUICK_CASTS.translationString
    ),
    QUICK_CAST_5(
            "key.spellbrookplus.quick_cast.5",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_KP_5,
            KeybindCategoryModel.QUICK_CASTS.translationString
    ),
    QUICK_CAST_6(
            "key.spellbrookplus.quick_cast.6",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_KP_6,
            KeybindCategoryModel.QUICK_CASTS.translationString
    ),
    QUICK_CAST_7(
            "key.spellbrookplus.quick_cast.7",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_KP_7,
            KeybindCategoryModel.QUICK_CASTS.translationString
    ),
    QUICK_CAST_8(
            "key.spellbrookplus.quick_cast.8",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_KP_8,
            KeybindCategoryModel.QUICK_CASTS.translationString
    );

    public final String translationString;
    public final InputConstants.Type type;
    public final int keyCode;
    public final String category;
    public final KeyMapping keyMapping;

    public boolean isDown() {
        return GLFW.glfwGetKey(GLFW.glfwGetCurrentContext(), ((KeyMappingAccessor) this.keyMapping).sb$getKey().getValue()) == GLFW.GLFW_PRESS;
    }

    KeybindModel(String translationString, InputConstants.Type type, int keyCode, String category) {
        this.translationString = translationString;
        this.type = type;
        this.keyCode = keyCode;
        this.category = category;
        this.keyMapping = new KeyMapping(translationString, type, keyCode, category);
    }
}
