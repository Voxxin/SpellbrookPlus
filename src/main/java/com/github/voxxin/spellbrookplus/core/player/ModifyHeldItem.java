package com.github.voxxin.spellbrookplus.core.player;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModifyHeldItem {
    public static void tick() {
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;
    }
}
