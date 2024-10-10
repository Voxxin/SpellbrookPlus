package com.github.voxxin.spellbrookplus.mixins.client.gui.components;

import com.github.voxxin.spellbrookplus.SpellbrookPlus;
import com.github.voxxin.spellbrookplus.core.mixin.extenders.ChatComponentExtender;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatComponent.class)
public class ChatComponentMixin implements ChatComponentExtender {
    @Unique
    private Component previousDeletedMessage = Component.empty();
    @Unique
    private int previousDeletedTime = 0;

    @Inject(method="tick()V", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (previousDeletedTime>0) previousDeletedTime--;

        if (SpellbrookPlus.connected() && previousDeletedTime == 0) {
            previousDeletedMessage = Component.empty();
            previousDeletedTime = -1;
        }
    }

    @Inject(method = "Lnet/minecraft/client/gui/components/ChatComponent;addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V", at = @At("HEAD"), cancellable = true)
    private void addMessage(Component chatComponent, MessageSignature headerSignature, int addedTime, GuiMessageTag tag, boolean onlyTrim, CallbackInfo ci) {
        List<String> ignoreMessages = List.of(
                "Spell not found",
                "Your vessel has not unlocked this combo!",
                "Nothing to target!",
                "You do not have the required level to cast this!",
                "The Magic Guardians have rejected your spell in this realm!"
        );

        if (!SpellbrookPlus.connected()
                || headerSignature != null
                || !ignoreMessages.contains(chatComponent.getString())) {
            previousDeletedMessage = Component.empty();
            return;
        }

        previousDeletedTime = 2;
        previousDeletedMessage = chatComponent;

        ci.cancel();
    }

    @Override
    public Component sp$previousDeletedMessage() {
        return this.previousDeletedMessage;
    }
}
