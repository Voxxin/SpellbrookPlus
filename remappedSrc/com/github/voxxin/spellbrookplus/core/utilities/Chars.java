package com.github.voxxin.spellbrookplus.core.utilities;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class Chars {

    public static Component styledUnicode(String unicodeCharacter) {
        return Component.literal(unicodeCharacter).setStyle(Style.EMPTY.withFont(
                new ResourceLocation(Constants.MOD_ID + ":text")
        )).withStyle(ChatFormatting.WHITE);
    }
}
