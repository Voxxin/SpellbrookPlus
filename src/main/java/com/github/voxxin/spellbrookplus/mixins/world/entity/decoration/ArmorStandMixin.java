package com.github.voxxin.spellbrookplus.mixins.world.entity.decoration;

import com.github.voxxin.spellbrookplus.SpellbrookPlus;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin extends Entity {

    @Shadow public abstract void kill();

    public ArmorStandMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (!SpellbrookPlus.connected()) return;

        ArmorStand armorStand = (ArmorStand) (Object) this;
        if (armorStand.hasCustomName() && Minecraft.getInstance().level != null) {
            Player nearestPlayer = Minecraft.getInstance().level.getNearestPlayer(armorStand, 3);
            if (nearestPlayer != null && nearestPlayer.getDisplayName().getString().equals(armorStand.getCustomName().getString())) {
                this.kill();
            }
        }
    }

}
