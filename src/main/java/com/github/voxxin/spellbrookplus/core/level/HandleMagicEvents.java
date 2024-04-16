package com.github.voxxin.spellbrookplus.core.level;

import com.github.voxxin.spellbrookplus.core.advancements.Advancement;
import com.github.voxxin.spellbrookplus.core.advancements.AdvancementIcon;
import com.github.voxxin.spellbrookplus.core.advancements.AdvancementTime;
import com.github.voxxin.spellbrookplus.core.advancements.AdvancmenetToast;
import com.github.voxxin.spellbrookplus.core.mixin.ext.EntityExtender;
import com.github.voxxin.spellbrookplus.core.mixin.ext.GuiExtender;
import com.github.voxxin.spellbrookplus.core.utilities.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class HandleMagicEvents {
    private static final Minecraft client = Minecraft.getInstance();

    public static void tick() {
        GuiExtender gui = (GuiExtender) client.gui;
        String overlayMessage = gui.sp$getOverlayMessage() == null ? null : gui.sp$getOverlayMessage().getString();
        if ("Your magic senses something nearby...".equals(overlayMessage)) {
            handleMagicEntity();
        }
    }

    private static void handleMagicEntity() {
        GuiExtender gui = (GuiExtender) client.gui;
        gui.sp$setOverlayMessage(gui.sp$getOverlayMessage().copy().append(" " + Component.translatable("toast.spellbrookplus.glowing").getString()));

        Entity entity = getEntity();
        if (entity != null) {
            EntityExtender extEntity = (EntityExtender) entity;
            extEntity.sb$setSharedFlagIsGlowing(true);
            extEntity.sb$setFakeTeamColor(Constants.SpellbrookPurple);
            extEntity.sb$setCustomLightLevel(15);
            ItemStack itemStack = entity instanceof ItemEntity ? ((ItemEntity) entity).getItem() : entity.getSlot(0).get();
            Advancement advancement = new Advancement(AdvancementIcon.createItemStackIcon(itemStack), Component.translatable("toast.spellbrookplus.magic.title"), null, Component.translatable("toast.spellbrookplus.magic.subtitle"), Constants.SpellbrookPurple);
            advancement.setAdvancementTime(new AdvancementTime(-1, 12000L, -1));
            advancement.setBackgroundSprite(new ResourceLocation("spellbrookplus:textures/gui/sprites/toast/advancement.png"));
            client.getToasts().addToast(new AdvancmenetToast(advancement));
        }
    }

    private static Entity getEntity() {
        List<Entity> entities = client.level.getEntitiesOfClass(Entity.class, client.player.getBoundingBox().inflate(40));
        for (Entity entity : entities) {
            if (entity instanceof ItemEntity || entity instanceof Display.ItemDisplay) {
                ItemStack itemStack = entity instanceof ItemEntity ? ((ItemEntity) entity).getItem() : entity.getSlot(0).get();
                if (itemStack.getItem() == Items.PAPER &&
                        itemStack.getTag() != null &&
                        itemStack.getTag().contains("CustomModelData") &&
                        entity.tickCount <= 1) {
                    return entity;
                }
            }
        }
        return null;
    }
}

