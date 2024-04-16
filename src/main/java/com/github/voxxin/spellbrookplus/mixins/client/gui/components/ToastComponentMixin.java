package com.github.voxxin.spellbrookplus.mixins.client.gui.components;

import com.github.voxxin.spellbrookplus.core.client.gui.advancements.AdvancementTime;
import com.github.voxxin.spellbrookplus.core.client.gui.advancements.AdvancmenetToast;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(ToastComponent.ToastInstance.class)
public abstract class ToastComponentMixin<T extends Toast> {

    @Shadow
    @Final
    private static long ANIMATION_TIME;
    @Shadow private long animationTime;
    @Shadow private Toast.Visibility visibility;
    @Mutable
    @Unique
    @Final
    private long IN_TIME;

    @Mutable
    @Unique
    @Final
    private long VISIBLE_TIME;

    @Mutable
    @Unique
    @Final
    private long OUT_TIME;


    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(ToastComponent toastComponent, Toast toast, int index, int slotCount, CallbackInfo ci) {
        AdvancmenetToast advancementToast = toast instanceof AdvancmenetToast ? (AdvancmenetToast) toast : null;
        if (advancementToast == null) {
            this.IN_TIME = ANIMATION_TIME;
            this.VISIBLE_TIME = ANIMATION_TIME;
            this.OUT_TIME = ANIMATION_TIME;
            return;
        }
        AdvancementTime time = advancementToast.getAdvancement().getAdvancementTime();
        this.IN_TIME = time.getInTime() <= -1 ? ANIMATION_TIME : time.getInTime();
        this.VISIBLE_TIME = time.getStayTime() <= -1 ? ANIMATION_TIME : time.getStayTime();
        this.OUT_TIME = time.getOutTime() <= -1 ? ANIMATION_TIME : time.getOutTime();
    }

    @ModifyReturnValue(
            method = "getVisibility(J)F",
            at = @At(value = "RETURN")
    )
    private float modifyVisibilityTime(float original, @Local(argsOnly = true) long time) {
        float f = Mth.clamp((float)(time - this.animationTime) / (this.visibility != Toast.Visibility.HIDE ? this.IN_TIME : this.OUT_TIME), 0.0F, 1.0F);
        f *= f;
        return this.visibility == Toast.Visibility.HIDE ? 1.0F - f : f;
    }

    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "CONSTANT", args = "floatValue=600.0", ordinal = 0)
    )
    private float modifyRenderOutTime(float original) {
        return this.VISIBLE_TIME;
    }


    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "CONSTANT", args = "longValue=600", ordinal = 1)
    )
    private long modifyRenderOutTime(long original) {
        return this.OUT_TIME;
    }
}
