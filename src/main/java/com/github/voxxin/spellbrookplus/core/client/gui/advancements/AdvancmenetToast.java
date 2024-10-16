package com.github.voxxin.spellbrookplus.core.client.gui.advancements;

import com.github.voxxin.spellbrookplus.core.client.gui.misc.FakeGuiEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;


@Environment(EnvType.CLIENT)
public class AdvancmenetToast implements Toast {
    private final Advancement advancement;
    private boolean playedSound;

    public AdvancmenetToast(Advancement newAdvancement) {
        this.advancement = newAdvancement;
    }
    public Toast.@NotNull Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long timeSinceLastVisible) {
        guiGraphics.blit(this.advancement.getBackgroundSprite(), 0, 0, 0, 0, this.width(), this.height(), this.width(), this.height());

        int titleColor = this.advancement.getTitleColor();
        int subtitleColor = this.advancement.getSubtitleColor();
        int opacity = (int) (Mth.clamp((float) Math.abs(timeSinceLastVisible - 1500L) / 300.0F, 0.0F, 1.0F) * (timeSinceLastVisible < 1500L ? 255.0F : 252.0F)) << 24 | 67108864;

        guiGraphics.drawString(toastComponent.getMinecraft().font, this.advancement.getTitle(), 30, 7, titleColor, false);
        guiGraphics.drawString(toastComponent.getMinecraft().font, this.advancement.getSubtitle(), 30, 16, subtitleColor, false);

        if (!this.playedSound && timeSinceLastVisible > 0L) {
            this.playedSound = true;
            AdvancementSoundEvent soundEvent = this.advancement.getSoundEvent();
            if (soundEvent != null) {
                toastComponent.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(soundEvent.sound, soundEvent.pitch, soundEvent.volume));
            }
        }

        if (this.advancement.getIcon().getIcon() == AdvancementIcon.Icon.ITEM_STACK) {
            guiGraphics.renderFakeItem(this.advancement.getIcon().getItemStack(), 8, 8);
        } else if (this.advancement.getIcon().getIcon() == AdvancementIcon.Icon.RENDERED_ENTITY) {
            AdvancementIcon icon = this.advancement.getIcon();
            FakeGuiEntity fakeGuiEntity = new FakeGuiEntity();

            fakeGuiEntity.renderEntity(guiGraphics, 8, 8, 1, new Vector3f(0.75f, icon.getEntity().getBbHeight() - 0.0655f, 0F), icon.getBaseRotation(), icon.getRotation(), icon.getEntity());
        }

        return (double) timeSinceLastVisible >= 5000.0 * toastComponent.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }

    public @NotNull Advancement getAdvancement() {
        return this.advancement;
    }
}
