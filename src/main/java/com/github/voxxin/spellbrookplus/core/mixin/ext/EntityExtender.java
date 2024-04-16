package com.github.voxxin.spellbrookplus.core.mixin.ext;

import net.minecraft.util.ColorRGBA;

import java.awt.*;

public interface EntityExtender {
    void sb$setSharedFlagIsGlowing(boolean isGlowing);

    void sb$setFakeTeamColor(Color color);

    int sb$getCustomLightLevel();
    void sb$setCustomLightLevel(int lightLevel);
}
