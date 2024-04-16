package com.github.voxxin.spellbrookplus.mixins.client.gui.screens;

import com.github.voxxin.spellbrookplus.SpellBrookPlus;
import com.github.voxxin.spellbrookplus.core.client.gui.conifg.ConfigScreen;
import com.github.voxxin.spellbrookplus.core.mixin.asr.GridLayoutAccessor;
import com.github.voxxin.spellbrookplus.core.utilities.Chars;
import com.github.voxxin.spellbrookplus.core.utilities.Constants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {
    protected PauseScreenMixin(Component title) {
        super(title);
    }

    @Unique
    private LayoutElement returnToGameRightButton;

    @Inject(method = "createPauseMenu",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", ordinal = 1, shift = At.Shift.BEFORE)
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void saveLanButton(CallbackInfo ci, GridLayout gridLayout, GridLayout.RowHelper rowHelper) {
        gridLayout.visitChildren(element -> returnToGameRightButton = element);
    }

    @Inject(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout;visitWidgets(Ljava/util/function/Consumer;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void createPauseMenuButton(CallbackInfo ci, GridLayout gridLayout, GridLayout.RowHelper rowHelper, Component component) {
        int x, y;

        x = returnToGameRightButton.getX() + returnToGameRightButton.getWidth() + 4;
        y = returnToGameRightButton.getY();

        if (gridLayout != null && !Constants.MOD_MENU_PRESENT) {
            final List<LayoutElement> buttons = ((GridLayoutAccessor) gridLayout).getChildren();
            if (SpellBrookPlus.connected()) {
                buttons.add(
                        Button.builder(
                                        Chars.styledUnicode("\uEF00"),
                                        button -> {
                                            this.minecraft.setScreen(new ConfigScreen(this));
                                        })
                                .build()
                );
            }
        }
    }
}
