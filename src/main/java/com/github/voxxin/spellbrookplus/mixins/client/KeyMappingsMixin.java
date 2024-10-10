package com.github.voxxin.spellbrookplus.mixins.client;

import com.github.voxxin.spellbrookplus.core.mixin.accessors.KeyMappingAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyMapping.class)
public class KeyMappingsMixin implements KeyMappingAccessor {
    @Shadow private InputConstants.Key key;

    @Override
    public InputConstants.Key sb$getKey() {
        return this.key;
    }
}
