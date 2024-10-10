package com.github.voxxin.spellbrookplus.core.client;

import com.github.voxxin.spellbrookplus.core.client.model.KeybindCategoryModel;
import com.github.voxxin.spellbrookplus.core.client.model.KeybindModel;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;

import java.util.ArrayList;
import java.util.List;

import static com.github.voxxin.spellbrookplus.core.client.model.KeybindModel.*;

public class SpellbrookKeybinds {
    private final Minecraft client = Minecraft.getInstance();
    List<String> castActions = new ArrayList<>();
    int cooldown = 0;

    public SpellbrookKeybinds() {
        for (KeybindCategoryModel category : KeybindCategoryModel.values()) {
            KeybindingRegistryImpl.addCategory(category.translationString);
        }

        for (KeybindModel keybind : KeybindModel.values()) {
            KeybindingRegistryImpl.registerKeyBinding(keybind.keyMapping);
        }
    }

    public void tick() {
        handleQuickCasts();

        while (QUICK_CAST_3.keyMapping.consumeClick()) castActions.addAll(List.of("l", "l", "l"));
        while (QUICK_CAST_8.keyMapping.consumeClick()) castActions.addAll(List.of("l", "r", "l"));
        while (QUICK_CAST_2.keyMapping.consumeClick()) castActions.addAll(List.of("l", "r", "r"));
        while (QUICK_CAST_6.keyMapping.consumeClick()) castActions.addAll(List.of("r", "l", "l"));
        while (QUICK_CAST_4.keyMapping.consumeClick()) castActions.addAll(List.of("r", "l", "r"));
        while (QUICK_CAST_1.keyMapping.consumeClick()) castActions.addAll(List.of("r", "r", "l"));
        while (QUICK_CAST_7.keyMapping.consumeClick()) castActions.addAll(List.of("r", "r", "r"));
        while (QUICK_CAST_5.keyMapping.consumeClick()) castActions.addAll(List.of("l", "l", "r"));
    }

    private void handleQuickCasts() {
        if (cooldown > 0) cooldown--;
        if (castActions.isEmpty() || cooldown != 0) return;

        if (castActions.get(0).equalsIgnoreCase("r")) client.getConnection().send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 0));
        else client.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, client.player.getOnPos(), Direction.DOWN));

        cooldown = 2;
        castActions.remove(0);
    }
}
