package com.github.voxxin.spellbrookplus.mixins.client.gui.screens.inventory;

import com.github.voxxin.spellbrookplus.core.client.gui.conifg.ConfigManager;
import com.github.voxxin.spellbrookplus.core.mixin.accessors.AbstractContainerScreenAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.ZonedDateTime;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin extends Screen implements AbstractContainerScreenAccessor {
    @Shadow protected int titleLabelY;

    @Shadow protected int imageWidth;

    @Shadow @Final protected AbstractContainerMenu menu;

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "renderLabels")
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (ConfigManager.assignmentsResetTimer.getValue() && this.title.getString().contains("\uE02D")) {
            String resetTimeString = String.format(Component.translatable("gui.spellbrookplus.academy_quests.next_reset").getString(), calculateResetTime());
            int textSize = (this.imageWidth - 2 - this.font.width(resetTimeString)) / 2;
            int textPosY = this.titleLabelY - 14;
            guiGraphics.drawString(this.font, resetTimeString, textSize + 1, textPosY + 1, 0, true);
            guiGraphics.drawString(this.font, resetTimeString, textSize + 1, textPosY, 16777215, true);
        }
    }

    @Unique
    private String calculateResetTime() {
        ItemStack magicAssignmentItem = this.menu.slots.stream()
                .map(Slot::getItem)
                .filter(item -> item.is(Items.WRITABLE_BOOK)) // Map to ItemStack
                .findFirst()
                .orElse(ItemStack.EMPTY); // Return an empty ItemStack if not found

        if (magicAssignmentItem.isEmpty()) return "00:00:00";


        TextColor itemColor = magicAssignmentItem.getHoverName().getStyle().getColor();
        assert itemColor != null;

        long unixTime = ZonedDateTime.now().toEpochSecond();
        long lastTimeChecked = 0L;
        if (itemColor.getValue() == 3454929) lastTimeChecked = ConfigManager.firstAssignmentsTimeWater.getValueAsLong();
        else if (itemColor.getValue() == 9548840) lastTimeChecked = ConfigManager.firstAssignmentsTimeNature.getValueAsLong();
        else if (itemColor.getValue() == 15101251) lastTimeChecked = ConfigManager.firstAssignmentsTimeFire.getValueAsLong();

        if (lastTimeChecked <= 0L) {
            if (itemColor.getValue() == 3454929) ConfigManager.firstAssignmentsTimeWater.setValue(unixTime);
            else if (itemColor.getValue() == (9548840)) ConfigManager.firstAssignmentsTimeNature.setValue(unixTime);
            else if (itemColor.getValue() == 15101251) ConfigManager.firstAssignmentsTimeFire.setValue(unixTime);
            ConfigManager.updateConfig();
            lastTimeChecked = unixTime;
        }

        long timeUntilReset = Math.max((lastTimeChecked + 86400L) - unixTime, 0);

        return String.format("%02d:%02d:%02d",
                timeUntilReset / 3600,
                (timeUntilReset % 3600) / 60,
                timeUntilReset % 60
        );
    }

}
