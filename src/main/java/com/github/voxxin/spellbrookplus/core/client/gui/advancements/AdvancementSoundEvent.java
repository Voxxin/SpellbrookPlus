package com.github.voxxin.spellbrookplus.core.client.gui.advancements;

import net.minecraft.sounds.SoundEvent;

public class AdvancementSoundEvent {
    public final SoundEvent sound;
    public final float pitch;
    public final float volume;

    public AdvancementSoundEvent(SoundEvent sound, int pitch, int volume) {
        this.sound = sound;
        this.pitch = pitch;
        this.volume = volume;
    }

    public AdvancementSoundEvent(SoundEvent sound) {
        this(sound, 1, 1);
    }
}
