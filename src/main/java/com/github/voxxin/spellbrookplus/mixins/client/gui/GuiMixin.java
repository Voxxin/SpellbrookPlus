package com.github.voxxin.spellbrookplus.mixins.client.gui;

import com.github.voxxin.spellbrookplus.SpellBrookPlus;
import com.github.voxxin.spellbrookplus.core.mixin.extenders.GuiExtender;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin implements GuiExtender {

    @Final
    @Shadow
    private DebugScreenOverlay debugOverlay;

    @Shadow @Nullable private Component overlayMessageString;

    @Shadow @Nullable private Component title;

    @Shadow @Nullable private Component subtitle;

    @Inject(method = "Lnet/minecraft/client/gui/Gui;displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/scores/Objective;)V", at = @At("HEAD"), cancellable = true)
    private void onDisplayScoreboardSidebar(GuiGraphics guiGraphics, Objective objective, CallbackInfo ci) {
        if (SpellBrookPlus.connected() && this.debugOverlay.showDebugScreen()) ci.cancel();
    }

    @Override
    public Component sp$getOverlayMessage()  {
        return this.overlayMessageString;
    }

    @Override
    public Component sp$getTitleMessage()  {
        return this.title;
    }

    @Override
    public Component sp$getSubtitleMessage()  {
        return this.subtitle;
    }

    @Override
    public void sp$setOverlayMessage(Component message) {
        this.overlayMessageString = message;
    }

    @Override
    public void sp$setTitleMessage(Component message) {
        this.title = message;
    }

    @Override
    public void sp$setSubtitleMessage(Component message) {
        this.subtitle = message;
    }
}
