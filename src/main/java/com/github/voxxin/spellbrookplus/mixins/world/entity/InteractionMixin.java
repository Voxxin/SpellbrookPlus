package com.github.voxxin.spellbrookplus.mixins.world.entity;

import net.minecraft.world.entity.Interaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Interaction.class)
public class InteractionMixin {

    @Inject(method = "tick", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void tick(CallbackInfo ci) {
        Interaction interactionEntity = (Interaction) (Object) (this);
//        System.out.println(interactionEntity.get);
    }
}
