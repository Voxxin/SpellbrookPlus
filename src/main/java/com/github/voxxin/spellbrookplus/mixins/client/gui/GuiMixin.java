package com.github.voxxin.spellbrookplus.mixins.client.gui;

import com.github.voxxin.spellbrookplus.SpellbrookPlus;
import com.github.voxxin.spellbrookplus.core.mixin.extenders.GuiExtender;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.scores.Objective;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(Gui.class)
public abstract class GuiMixin implements GuiExtender {

    @Final
    @Shadow
    private DebugScreenOverlay debugOverlay;

    @Shadow @Nullable private Component overlayMessageString;

    @Shadow @Nullable private Component title;

    @Shadow @Nullable private Component subtitle;

    @Shadow @Final private ChatComponent chat;
    @Shadow private int overlayMessageTime;
    @Unique private Component previousMessageString;

    @Inject(method = "Lnet/minecraft/client/gui/Gui;displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/scores/Objective;)V", at = @At("HEAD"), cancellable = true)
    private void onDisplayScoreboardSidebar(GuiGraphics guiGraphics, Objective objective, CallbackInfo ci) {
        if (SpellbrookPlus.connected() && this.debugOverlay.showDebugScreen()) ci.cancel();
    }

    @Inject(method = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;F)V", at = @At("TAIL"))
    private void resetOverlayString(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        if (this.overlayMessageString != null && this.overlayMessageString.getString().isEmpty()) {
            this.overlayMessageString = this.previousMessageString;
            this.previousMessageString = null;
        }
    }

    @Inject(method = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;width(Lnet/minecraft/network/chat/FormattedText;)I", ordinal = 0, shift = At.Shift.AFTER))
    private void onRender(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci, @Local Font font, @Local(ordinal = 1) int k, @Local(ordinal = 2) int m) {
        if (this.overlayMessageString == null || this.overlayMessageString.toFlatList().stream().noneMatch(c -> c.getString().matches("\n"))) return;

        List<FormattedCharSequence> messagesList = new ArrayList<>();
        List<FormattedCharSequence> componentList = new ArrayList<>();
        List<Component> lines = this.overlayMessageString.toFlatList();

        for (int i = 0; i < lines.size(); i++) {
            Component line = lines.get(i);
            String lineStr = line.getString();

            if (!lineStr.matches("\n")) componentList.add(line.getVisualOrderText());

            if (lineStr.matches("\n") || i == lines.size() - 1) {
                if (!componentList.isEmpty()) {
                    messagesList.add(FormattedCharSequence.fromList(new ArrayList<>(componentList)));
                    componentList.clear();
                } else {
                    messagesList.add(Component.empty().getVisualOrderText());
                }
            }
        }

        Collections.reverse(messagesList);
        for (int i = 0; i < messagesList.size(); i++) {
            int calcX = font.width(messagesList.get(i));
            calcX = calcX / 2 - calcX;
            guiGraphics.drawString(font, messagesList.get(i), calcX, -4 + (-i * 10), k | m);
        }

        if (messagesList.size() > 1) {
            this.previousMessageString = this.overlayMessageString;
            this.overlayMessageString = Component.empty();
        }
    }

    @Override
    public Component sp$getOverlayMessage()  {
        if (this.overlayMessageString == null && this.previousMessageString != null) return this.previousMessageString;
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
    public void sp$setOverlayMessage(Component message, int tickDuration) {
        this.overlayMessageString = message;
        this.overlayMessageTime = tickDuration;
    }

    @Override
    public void sp$setTitleMessage(Component message) {
        this.title = message;
    }

    @Override
    public void sp$setSubtitleMessage(Component message) {
        this.subtitle = message;
    }

    @Override
    public ChatComponent sp$chat() {
        return chat;
    }
}
