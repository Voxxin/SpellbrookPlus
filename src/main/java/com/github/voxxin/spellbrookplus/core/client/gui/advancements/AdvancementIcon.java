package com.github.voxxin.spellbrookplus.core.client.gui.advancements;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

import java.util.Optional;

public class AdvancementIcon {
    private final Icon icon;
    private final Optional<ItemStack> itemStack;
    private final Optional<Quaternionf> rotation;
    private final Optional<Quaternionf> baseRotation;
    private final Optional<LivingEntity> entity;

    private AdvancementIcon(Icon icon, Optional<ItemStack> itemStack, Optional<Quaternionf> rotation,
                            Optional<Quaternionf> baseRotation, Optional<LivingEntity> entity) {
        this.icon = icon;
        this.itemStack = itemStack;
        this.rotation = rotation;
        this.baseRotation = baseRotation;
        this.entity = entity;
    }

    public static AdvancementIcon createItemStackIcon(ItemStack itemStack) {
        return new AdvancementIcon(Icon.ITEM_STACK, Optional.of(itemStack), Optional.empty(), Optional.empty(), Optional.empty());
    }

    public static AdvancementIcon createRenderedEntityIcon(LivingEntity entity, Quaternionf baseRotation, Quaternionf rotation) {
        return new AdvancementIcon(Icon.RENDERED_ENTITY, Optional.empty(), Optional.of(rotation),
                Optional.of(baseRotation), Optional.of(entity));
    }

    public Icon getIcon() {
        return icon;
    }

    public ItemStack getItemStack() {
        return itemStack.get();
    }

    public Quaternionf getRotation() {
        return rotation.get();
    }

    public Quaternionf getBaseRotation() {
        return baseRotation.get();
    }

    public LivingEntity getEntity() {
        return entity.get();
    }

    // Enum for icon types
    public enum Icon {
        ITEM_STACK,
        BLIT,
        RENDERED_ENTITY
    }
}
