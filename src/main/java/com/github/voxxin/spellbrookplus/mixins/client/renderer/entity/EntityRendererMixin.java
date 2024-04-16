package com.github.voxxin.spellbrookplus.mixins.client.renderer.entity;

import com.github.voxxin.spellbrookplus.SpellBrookPlus;
import com.github.voxxin.spellbrookplus.core.mixin.ext.EntityExtender;
import com.github.voxxin.spellbrookplus.core.mixin.ext.EntityRendererExtender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> implements EntityRendererExtender {

    @Shadow protected abstract boolean shouldShowName(T entity);
    @Shadow protected abstract void renderNameTag(T entity, Component displayName, PoseStack poseStack, MultiBufferSource buffer, int packedLight);
    @Shadow @Final protected EntityRenderDispatcher entityRenderDispatcher;
    @Shadow public abstract Font getFont();

    @Inject(at = @At("HEAD"), method = "getBlockLightLevel", cancellable = true)
    private void getBlockLightLevel(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (((EntityExtender)entity).sb$getCustomLightLevel() >= 0) {
            cir.setReturnValue(((EntityExtender) entity).sb$getCustomLightLevel());
        }
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!SpellBrookPlus.connected()) return;

        if (entity instanceof Player && ((Player) entity).getDisplayName().getString().contains(" ")) {
            renderNameTag(entity, (MutableComponent) entity.getDisplayName(), poseStack, buffer, packedLight);
        } else if (entity instanceof FishingHook && entity.shouldShowName()) {
            sp$renderNameTagNoBackground(entity, (MutableComponent) entity.getDisplayName(), poseStack, buffer, packedLight);
        }

        ci.cancel();
    }

    @Override
    public void sp$renderNameTagNoBackground(Entity entity, Component displayName, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        double d = this.entityRenderDispatcher.distanceToSqr(entity);
        if (!(d > 4096.0)) {
            boolean bl = !entity.isDiscrete();
            float f = entity.getNameTagOffsetY();
            poseStack.pushPose();
            poseStack.translate(0.0F, f, 0.0F);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = poseStack.last().pose();
            Font font = this.getFont();
            float h = (float)(-font.width(displayName) / 2);
            font.drawInBatch(displayName, h, (float)0, 553648127, false, matrix4f, buffer, bl ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, 0, packedLight);
            if (bl) {
                font.drawInBatch(displayName, h, (float)0, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, packedLight);
            }

            poseStack.popPose();
        }
    }
}

