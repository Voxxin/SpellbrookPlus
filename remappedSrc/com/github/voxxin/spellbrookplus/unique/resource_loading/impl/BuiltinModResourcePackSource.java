package com.github.voxxin.spellbrookplus.unique.resource_loading.impl;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;

public class BuiltinModPackSource implements PackSource {
    private final String modId;

    public BuiltinModPackSource(String modId) {
        this.modId = modId;
    }

    @Override
    public boolean shouldAddAutomatically() {
        return true;
    }

    @Override
    public Component decorate(Component packName) {
        return Component.translatable("pack.nameAndSource", packName, Component.translatable("pack.source.builtinMod", modId)).withStyle(ChatFormatting.GRAY);
    }
}
