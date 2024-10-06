package com.github.voxxin.spellbrookplus.unique.resource_loading.mixin;

import com.github.voxxin.spellbrookplus.unique.resource_loading.impl.ModResource;
import com.github.voxxin.spellbrookplus.unique.resource_loading.impl.PackSourceTracker;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Resource.class)
class ResourceMixin implements ModResource {
    @Override
    public PackSource getModPackSource() {
        Resource self = (Resource) (Object) this;
        return PackSourceTracker.getSource((self.source());
    }
}
