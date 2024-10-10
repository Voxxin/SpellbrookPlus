package com.github.voxxin.spellbrookplus.core.level;

import com.github.voxxin.spellbrookplus.SpellbrookPlus;
import com.github.voxxin.spellbrookplus.core.client.gui.advancements.Advancement;
import com.github.voxxin.spellbrookplus.core.client.gui.advancements.AdvancementIcon;
import com.github.voxxin.spellbrookplus.core.client.gui.advancements.AdvancementTime;
import com.github.voxxin.spellbrookplus.core.client.gui.advancements.AdvancmenetToast;
import com.github.voxxin.spellbrookplus.core.client.gui.conifg.ConfigManager;
import com.github.voxxin.spellbrookplus.core.client.gui.misc.FakeGuiEntity;
import com.github.voxxin.spellbrookplus.core.mixin.extenders.ChatComponentExtender;
import com.github.voxxin.spellbrookplus.core.mixin.extenders.EntityExtender;
import com.github.voxxin.spellbrookplus.core.mixin.extenders.GuiExtender;
import com.github.voxxin.spellbrookplus.core.utilities.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandleMagicEvents {
    private static final Minecraft client = Minecraft.getInstance();
    private static int entityTick = 0;

    public static void tick() {
        long[] nextBingoResets = {
                (long) ConfigManager.firstAssignmentsTimeFire.getValueAsFloat() == -1L ? -1L : (long) ConfigManager.firstAssignmentsTimeFire.getValueAsFloat()  + 86400L,
                (long) ConfigManager.firstAssignmentsTimeWater.getValueAsFloat() == -1L ? -1L : (long) ConfigManager.firstAssignmentsTimeWater.getValueAsFloat()  + 86400L,
                (long) ConfigManager.firstAssignmentsTimeNature.getValueAsFloat() == -1L ? -1L : (long) ConfigManager.firstAssignmentsTimeNature.getValueAsFloat()  + 86400L
        };

        if (((SpellbrookPlus.connected() || ConfigManager.assignmentsResetAlwaysNotify.getValue()))) {
            long currentEpoch = ZonedDateTime.now().toEpochSecond();

            for (int i = 0; i < nextBingoResets.length; i++) {
                if (nextBingoResets[i] != -1L && nextBingoResets[i] > 86400L && nextBingoResets[i] < currentEpoch) {
                    bingoReset();
                    if (i == 0) ConfigManager.firstAssignmentsTimeFire.setValue(0L);
                    else if (i == 1) ConfigManager.firstAssignmentsTimeWater.setValue(0L);
                    else ConfigManager.firstAssignmentsTimeNature.setValue(0L);

                    ConfigManager.updateConfig();
                }
            }
        }

        if (!SpellbrookPlus.connected()) return;

        GuiExtender gui = (GuiExtender) client.gui;
        String overlayMessage = gui.sp$getOverlayMessage() == null ? "" : gui.sp$getOverlayMessage().getString();

        if (!overlayMessage.contains("Your magic senses something nearby...") && entityTick != 0) entityTick = 0;
        else if (overlayMessage.contains("Your magic senses something nearby...")) {
            handleMagicEntity();
            entityTick++;
        }

        if (overlayMessage.matches("^[LR\uE022\uE023 ]+$")) {
            useSpellNames(overlayMessage);
        }
    }

    private static void useSpellNames(String overlayMessage) {
        GuiExtender gui = (GuiExtender) client.gui;
        ChatComponentExtender chat = (ChatComponentExtender) gui.sp$chat();

        ItemStack wandItem = client.player.getMainHandItem();
        final String strippedMessage = overlayMessage.replaceAll("[\uE022\uE023 ]+", "");
        final Map<String, Component> spellNames = new HashMap<>();

        List<Component> tooltipLines = wandItem.getTooltipLines(client.player, TooltipFlag.NORMAL);
        for (Component tooltipLine : tooltipLines) {
            if (!tooltipLine.getString().contains("•")) continue;
            String[] parts = tooltipLine.getString().split("•");
            spellNames.putIfAbsent(parts[1].strip(), Component.literal("").append(parts[2].strip()).setStyle(tooltipLine.toFlatList().get(3).getStyle()));
        }

        Style textureStyle = Style.EMPTY.withFont(ResourceLocation.tryParse("spellbrook:small")).withColor(ChatFormatting.WHITE);
        Style errorStyle = Style.EMPTY.withFont(ResourceLocation.tryParse("spellbrook:small")).withColor(ChatFormatting.RED);
        Style tittleStyle = Style.EMPTY.withFont(ResourceLocation.tryParse("spellbrook:small")).withColor(ChatFormatting.GRAY);
        Style filledOutLettersStyle = Style.EMPTY.withFont(ResourceLocation.tryParse("spellbrook:small")).withColor(ChatFormatting.YELLOW);

        List<Component> lines = gui.sp$getOverlayMessage().toFlatList();
        Component emptySpace = Component.empty().append(lines.get(lines.size() - 1)).setStyle(gui.sp$getOverlayMessage().getStyle());

        Style guiStyle = gui.sp$getOverlayMessage().getStyle();

        if (lines.size() >= 5) {
            if (
                    spellNames.containsKey(strippedMessage)
                            && chat.sp$previousDeletedMessage().getString().isEmpty()
                            && !spellNames.get(strippedMessage).getString().toLowerCase().contains("empty")
            ) {
                gui.sp$setOverlayMessage(
                        Component.empty()
                                .append("[ ").withStyle(textureStyle).append(spellNames.get(strippedMessage)).append(" ]").withStyle(textureStyle)
                                .append(emptySpace).setStyle(guiStyle)
                                .append("\n")
                                .append(gui.sp$getOverlayMessage()).setStyle(guiStyle)
                );
            } else {
                String errorMessage = spellNames.get(strippedMessage) == null ? "locked" : spellNames.get(strippedMessage).getString().toLowerCase();
                String chatMessage = chat.sp$previousDeletedMessage().getString();
                if (errorMessage.contains("locked") || errorMessage.contains("empty")) {
                } else if (chatMessage.contains("required level")) {
                    errorMessage = "level too low";
                } else if (chatMessage.contains("target")) {
                    errorMessage = "nothing hit";
                } else if (chatMessage.contains("realm")) {
                    errorMessage = "not here";
                }

                gui.sp$setOverlayMessage(
                        Component.empty()
                                .append("[ ").withStyle(textureStyle).append(Component.literal(errorMessage).withStyle(errorStyle)).append(" ]").withStyle(textureStyle)
                                .append(emptySpace).setStyle(guiStyle)
                                .append("\n")
                                .append(gui.sp$getOverlayMessage()).setStyle(guiStyle)
                );
            }
        } else if (ConfigManager.spellSuggestions.getValue()) { /* Spell Suggestions */
            final Map<String, Component> possibleSpells = spellNames.keySet().stream()
                    .filter(spellName -> spellName.startsWith(strippedMessage)
                            &&
                            !spellNames.get(spellName).getString().strip().toLowerCase().matches("empty")
                    ).collect(Collectors.toMap(spellName -> spellName, spellNames::get));

            if (possibleSpells.isEmpty()) return;

            MutableComponent suggestions = Component.empty()
                    .append(Component.literal("suggestions:").withStyle(tittleStyle))
                    .append(emptySpace)
                    .setStyle(guiStyle);

            possibleSpells.forEach((spellName, component) -> suggestions.append("\n")
                    .setStyle(filledOutLettersStyle)
                    .append(Component.empty().append(strippedMessage).setStyle(filledOutLettersStyle))
                    .append(Component.empty().append(spellName.substring(strippedMessage.length())).setStyle(tittleStyle))
                    .append(" • ").setStyle(textureStyle)
                    .append(component).setStyle(component.getStyle())
                    .append(emptySpace).setStyle(guiStyle));

            gui.sp$setOverlayMessage(Component.empty()
                    .append(suggestions)
                    .append("\n")
                    .append(emptySpace).setStyle(guiStyle)
                    .append("\n")
                    .append(gui.sp$getOverlayMessage())
                    .setStyle(guiStyle));

        }
    }

    private static void handleMagicEntity() {
        GuiExtender gui = (GuiExtender) client.gui;
        if (ConfigManager.magicGlow.getValue() && entityTick == 0) gui.sp$setOverlayMessage(gui.sp$getOverlayMessage().copy().append(" " + Component.translatable("toast.spellbrookplus.glowing").getString()));
        if (entityTick >= 1000) gui.sp$setOverlayMessage(Component.empty());

        Entity entity = getEntity();
        if (entity != null) {
            gui.sp$setOverlayMessage(Component.empty());

            EntityExtender extEntity = (EntityExtender) entity;
            if (ConfigManager.magicGlow.getValue()) extEntity.sb$setSharedFlagIsGlowing(true);
            if (ConfigManager.magicGlow.getValue()) extEntity.sb$setFakeTeamColor(Constants.SpellbrookPurple);
            if (ConfigManager.magicAlwaysBright.getValue()) extEntity.sb$setCustomLightLevel(15);
            AdvancementIcon icon;

            if (entity instanceof ItemEntity || entity instanceof Display.ItemDisplay) {
                icon = AdvancementIcon.createItemStackIcon(entity instanceof ItemEntity ? ((ItemEntity) entity).getItem() : entity.getSlot(0).get());
            } else if (entity instanceof RemotePlayer) {
                icon = AdvancementIcon.createRenderedEntityIcon((LivingEntity) entity, new Quaternionf(0, 0, 0, 1), new Quaternionf(0, 0, 0, 1));
            } else {
                icon = AdvancementIcon.createItemStackIcon(new ItemStack(Items.AIR));
            }

            Advancement advancement = new Advancement(icon, Component.translatable("toast.spellbrookplus.magic.title"), null, Component.translatable("toast.spellbrookplus.magic.subtitle"), Constants.SpellbrookPurple);
            advancement.setAdvancementTime(new AdvancementTime(-1, 12000L, -1));
            advancement.setBackgroundSprite(new ResourceLocation("spellbrookplus:textures/gui/sprites/toast/advancement.png"));
            if (ConfigManager.magicAdvancement.getValue()) client.getToasts().addToast(new AdvancmenetToast(advancement));
        }
    }

    private static void bingoReset() {
        if (!ConfigManager.assignmentsResetAdvancement.getValue()) return;
        ItemStack itemStack = new ItemStack(Items.KNOWLEDGE_BOOK);
        Advancement advancement = new Advancement(AdvancementIcon.createItemStackIcon(itemStack), Component.translatable("toast.spellbrookplus.bingoReset.title"), null, Component.translatable("toast.spellbrookplus.bingoReset.subtitle"), Constants.SpellbrookPurple);
        advancement.setAdvancementTime(new AdvancementTime(-1, 24000L, -1));
        advancement.setBackgroundSprite(new ResourceLocation("spellbrookplus:textures/gui/sprites/toast/advancement.png"));
        client.getToasts().addToast(new AdvancmenetToast(advancement));
    }

    private static Entity getEntity() {
        List<Entity> entities = client.level.getEntitiesOfClass(Entity.class, client.player.getBoundingBox().inflate(40));
        for (Entity entity : entities) {

            System.out.println(entity.getClass());

            if (entity instanceof RemotePlayer remotePlayer &&
                    remotePlayer.getName().getString().contains("§") &&
                    Math.abs(entityTick - entity.tickCount) <= 2) {
                return entity;
            }

            if (entity instanceof ItemEntity || entity instanceof Display.ItemDisplay) {
                ItemStack itemStack = entity instanceof ItemEntity ? ((ItemEntity) entity).getItem() : entity.getSlot(0).get();

                if ((itemStack.getItem() == Items.PAPER || itemStack.getItem() == Items.LEATHER_HORSE_ARMOR) &&
                        itemStack.getTag() != null &&
                        itemStack.getTag().contains("CustomModelData") &&
                        Math.abs(entityTick - entity.tickCount) <= 1) {
                    return entity;
                }
            }
        }
        return null;
    }
}

