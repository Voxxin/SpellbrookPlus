package com.github.voxxin.spellbrookplus.core.client.gui.advancements;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class Advancement {
    private final AdvancementIcon icon;
    private final Component title;
    private final Color titleColor;
    private final Component subtitle;
    private final Color subtitleColor;
    private ResourceLocation BACKGROUND_SPRITE;
    private AdvancementSoundEvent soundEvent;
    private AdvancementTime advancementTime;

    public Advancement(AdvancementIcon icon, Component title, Color titleColor, Component subtitle, Color subtitleColor) {
        this.icon = icon;
        this.title = title;
        this.titleColor = titleColor;
        this.subtitle = subtitle;
        this.subtitleColor = subtitleColor;
        this.BACKGROUND_SPRITE = new ResourceLocation("toast/advancement");
        this.soundEvent = null;
        this.advancementTime = null;

        System.out.println(BACKGROUND_SPRITE);
    }

    public void setBackgroundSprite(ResourceLocation location) {
        System.out.println(location);
        this.BACKGROUND_SPRITE = location;
    }

    public void setAdvancementSoundEvent(AdvancementSoundEvent soundEvent) {
        this.soundEvent = soundEvent;
    }

    public void setAdvancementTime(AdvancementTime advancementTime) {
        this.advancementTime = advancementTime;
    }

    public AdvancementIcon getIcon() {
        return this.icon;
    }

    public Component getTitle() {
        return this.title;
    }

    public int getTitleColor() {
        return this.titleColor == null ? Color.WHITE.getRGB() : this.titleColor.getRGB();
    }


    public Component getSubtitle() {
        return this.subtitle;
    }

    public int getSubtitleColor() {
        return this.subtitleColor == null ? Color.WHITE.getRGB() : this.subtitleColor.getRGB();
    }

    public ResourceLocation getBackgroundSprite() {
        return BACKGROUND_SPRITE;
    }
    public AdvancementSoundEvent getSoundEvent() {
        return soundEvent;
    }

    public AdvancementTime getAdvancementTime() {
        return this.advancementTime;
    }

}
