package com.github.voxxin.spellbrookplus.mixins.world.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Display.class)
public abstract class DisplayMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
//        Display.ItemDisplay displayEntity = (Display.ItemDisplay) (Object) this;
//
//        if (displayEntity.itemRenderState() == null) return;
//        ItemStack itemStack = displayEntity.itemRenderState().itemStack();
//        if (itemStack.hasTag()) {
//            CompoundTag tag = itemStack.getTag();
//            System.out.println(tag);
//        }
    }
}
