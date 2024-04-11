package com.github.voxxin.spellbrookplus.mixins.world.entity.projectile;

import com.github.voxxin.spellbrookplus.SpellBrookPlus;
import com.github.voxxin.spellbrookplus.core.utilities.Chars;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin extends Entity {

    @Shadow private boolean biting;

    public FishingHookMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract @Nullable Player getPlayerOwner();

    @Unique int timer = 0;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(II)I", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void tick(CallbackInfo ci, float f, BlockPos blockPos, FluidState fluidState, boolean bl) {
        if (!SpellBrookPlus.connected()) return;
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if (this.getPlayerOwner() != player) return;

        if (biting && timer < 1) {
            timer++;
            player.playSound(SoundEvents.ARROW_HIT_PLAYER, 4, 2);
            this.setCustomNameVisible(true);
            this.setCustomName(Chars.styledUnicode("\uEF01"));
        } else if (!biting && timer > 0) {
            timer = 0;
            this.setCustomNameVisible(false);
        }
    }
}
