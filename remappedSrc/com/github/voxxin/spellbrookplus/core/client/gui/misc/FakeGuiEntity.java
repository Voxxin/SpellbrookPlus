package com.github.voxxin.spellbrookplus.core.client.gui.misc;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FakeGuiEntity {
    private final Minecraft minecraft;
    private final EntityRenderDispatcher entityRenderDispatcher;

    public FakeGuiEntity() {
        this.minecraft = Minecraft.getInstance();
        this.entityRenderDispatcher = this.minecraft.getEntityRenderDispatcher();
    }

    /**
     * Renders the given living entity in an inventory-like GUI.
     *
     * @param graphics     The graphics context to render with.
     * @param x            The x-coordinate of the entity's position.
     * @param y            The y-coordinate of the entity's position.
     * @param scale        The scale factor for the entity.
     * @param offset       The offset from the entity's position.
     * @param baseRotation The base rotation to apply to the entity.
     * @param rotation     The additional rotation to apply to the entity (optional).
     * @param entity       The living entity to render.
     */
    public void renderEntity(GuiGraphics graphics, float x, float y, float scale, Vector3f offset, Quaternionf baseRotation, @Nullable Quaternionf rotation, LivingEntity entity) {
        Matrix4f scaleMatrix = new Matrix4f().scaling(12.0F * scale, 12.0F * scale, -12.0F * scale);

        graphics.pose().pushPose();
        graphics.pose().translate((double)x, (double)y, 50.0);
        graphics.pose().mulPoseMatrix(scaleMatrix);
        graphics.pose().translate(offset.x(), offset.y(), offset.z());

        baseRotation.rotateX((float)Math.PI);

        graphics.pose().mulPose(baseRotation);

        Lighting.setupForEntityInInventory();
        if (rotation != null) {
            rotation.conjugate();
            this.entityRenderDispatcher.overrideCameraOrientation(rotation);
        }
        this.entityRenderDispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> {
            this.entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, graphics.pose(), graphics.bufferSource(), 15728880);
        });
        graphics.flush();
        this.entityRenderDispatcher.setRenderShadow(true);
        graphics.pose().popPose();
        Lighting.setupFor3DItems();
    }
}


