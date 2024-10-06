package com.github.voxxin.spellbrookplus.core.mixin.extenders;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public interface EntityRendererExtender <T extends Entity> {
    void sp$renderNameTagNoBackground(T entity, Component displayName, PoseStack poseStack, MultiBufferSource buffer, int packedLight);
}
