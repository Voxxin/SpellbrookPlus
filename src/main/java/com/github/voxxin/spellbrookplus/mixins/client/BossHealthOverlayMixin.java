package com.github.voxxin.spellbrookplus.mixins.client;

import com.github.voxxin.spellbrookplus.core.ext.BossHealthOverlayAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)

public class BossHealthOverlayMixin implements BossHealthOverlayAccessor {
    @Shadow
    @Final
    Map<UUID, LerpingBossEvent> events;
    @Unique private String bossBarName;

    @Inject(method = "render", at = @At("HEAD"))
    private void renderBossBarName(GuiGraphics guiGraphics, CallbackInfo ci) {
        this.bossBarName = this.events.isEmpty() ? null : this.events.values().iterator().next().getName().getString();
    }
    @Override
    public String getBossBarName() {
        return bossBarName;
    }
}
