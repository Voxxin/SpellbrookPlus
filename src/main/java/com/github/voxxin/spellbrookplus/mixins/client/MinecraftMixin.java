package com.github.voxxin.spellbrookplus.mixins.client;

import com.github.voxxin.spellbrookplus.SpellBrookPlus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.Minecraft.class)
public class MinecraftMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo ci){
        SpellBrookPlus.lifecycle().tick();
    }
}
