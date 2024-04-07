package com.github.voxxin.spellbrookplus.mixins.client;

import com.github.voxxin.spellbrookplus.core.ext.SoundEventAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static net.minecraft.sounds.SoundEvent.createFixedRangeEvent;

@Mixin(SoundEvent.class)
public class SoundEventMixin {
    @Inject(at = @At("HEAD"), method = "create", cancellable = true)
    private static void newSoundEvent(ResourceLocation location, Optional<Float> range, CallbackInfoReturnable<SoundEvent> cir) {
        if (location != null && location.getNamespace().equals("random")) {
            cir.setReturnValue(createFixedRangeEvent(new ResourceLocation(""), 0));
        }
    }
}

