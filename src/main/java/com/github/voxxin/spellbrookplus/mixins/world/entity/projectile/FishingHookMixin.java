package com.github.voxxin.spellbrookplus.mixins.world.entity.projectile;

import com.github.voxxin.spellbrookplus.SpellbrookPlus;
import com.github.voxxin.spellbrookplus.core.client.gui.conifg.ConfigManager;
import com.github.voxxin.spellbrookplus.core.utilities.Chars;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
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
        if (!SpellbrookPlus.connected()) return;
        Minecraft client = Minecraft.getInstance();
        Player player = client.player;

        if (this.getPlayerOwner() != player) return;

        if (biting && timer < 1) {
            timer++;
            if (ConfigManager.fishingPing.getValue()) player.playSound(SoundEvents.ARROW_HIT_PLAYER, 4, 2);
            if (ConfigManager.fishingAlert.getValue()) {
                this.setCustomNameVisible(true);
                this.setCustomName(Chars.styledUnicode("\uEF01"));
            }
        } else if (!biting && timer > 0) {
            timer = 0;
            this.setCustomNameVisible(false);
        }
    }
}
