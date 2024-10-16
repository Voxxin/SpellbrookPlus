package com.github.voxxin.spellbrookplus.core.utilities;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class Constants {
    public static final String MOD_ID = "spellbrookplus";
    public static final String MOD_NAME = "Spellbrook+";
    public static final String MOD_VERSION = String.valueOf(FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion());
    public static boolean MOD_MENU_PRESENT = false;

    public static final Color SpellbrookPurple = new Color(158, 110, 218);
}
