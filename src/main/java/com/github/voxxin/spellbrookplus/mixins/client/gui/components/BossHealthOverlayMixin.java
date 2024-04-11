package com.github.voxxin.spellbrookplus.mixins.client.gui.components;

import com.github.voxxin.spellbrookplus.core.mixin.asr.BossHealthOverlayAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)

public class BossHealthOverlayMixin implements BossHealthOverlayAccessor {
    @Shadow
    @Final
    Map<UUID, LerpingBossEvent> events;
    @Shadow @Final private Minecraft minecraft;
    @Unique private String bossBarName;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderBossBarName(GuiGraphics guiGraphics, CallbackInfo ci) {
        this.bossBarName = this.events.isEmpty() ? null : this.events.values().iterator().next().getName().getString();

        if (this.minecraft.options.keyPlayerList.isDown()) ci.cancel();
    }
    @Override
    public String getBossBarName() {
        return bossBarName;
    }
}
