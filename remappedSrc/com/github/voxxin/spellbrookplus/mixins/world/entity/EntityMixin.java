package com.github.voxxin.spellbrookplus.mixins.world.entity;

import com.github.voxxin.spellbrookplus.core.mixin.extenders.EntityExtender;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityExtender {

    @Shadow protected abstract void setSharedFlag(int flag, boolean set);

    @Unique Integer customTeamColor = null;

    @Unique int customLightLevel = -1;

    @Override
    public void sb$setSharedFlagIsGlowing(boolean isGlowing) {
        this.setSharedFlag(6, isGlowing);
    }

    @Override
    public void sb$setFakeTeamColor(Color color) {
        this.customTeamColor = color.getRGB();
    }

    @Inject(at = @At("HEAD"), method = "getTeamColor", cancellable = true)
    private void getTeamColor(CallbackInfoReturnable<Integer> cir) {
        if (this.customTeamColor != null) cir.setReturnValue(this.customTeamColor);
    }

    @Override
    public int sb$getCustomLightLevel() {
        return this.customLightLevel;
    }

    @Override
    public void sb$setCustomLightLevel(int customLightLevel) {
        this.customLightLevel = customLightLevel;
    }
}
