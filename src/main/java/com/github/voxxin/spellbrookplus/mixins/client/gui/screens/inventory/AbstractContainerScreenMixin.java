package com.github.voxxin.spellbrookplus.mixins.client.gui.screens.inventory;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen {
    @Shadow protected int titleLabelY;

    @Shadow protected int imageWidth;

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "renderLabels")
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (this.title.getString().contains("\uE02D")) {
            String resetTimeString = String.format(Component.translatable("gui.spellbrookplus.academy_quests.next_reset").getString(), calculateTimeUntilMidnight());
            int textSize = (this.imageWidth - 2 - this.font.width(resetTimeString)) / 2;
            int textPosY = this.titleLabelY - 14;
            guiGraphics.drawString(this.font, resetTimeString, textSize + 1, textPosY + 1, 0, true);
            guiGraphics.drawString(this.font, resetTimeString, textSize + 1, textPosY, 16777215, true);
        }
    }

    @Unique
    private String calculateTimeUntilMidnight() {
        LocalTime currentTime = LocalTime.now(ZoneId.of("America/Los_Angeles"));
        LocalTime midnight = LocalTime.of(23, 29, 59);
        Duration duration = Duration.between(currentTime, midnight);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
