package com.github.voxxin.spellbrookplus.core.client.gui.conifg;

import com.github.voxxin.api.config.option.AbstractOption;
import com.github.voxxin.api.config.option.BooleanConfigOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class ConfigScreen extends Screen {
    private final Screen parent;

    private final TabManager tabManager = new TabManager(this::addRenderableWidget, guiEventListener -> this.removeWidget((GuiEventListener)guiEventListener));
    @Nullable
    private TabNavigationBar tabNavigationBar;
    @Nullable
    private GridLayout bottomButtons;
    public static final ResourceLocation FOOTER_SEPARATOR = new ResourceLocation("textures/gui/footer_separator.png");

    public ConfigScreen(Screen parent) {
        super(Component.translatable("config.spellbrookplus.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width).addTabs(new GeneralTab()).build();
        this.addRenderableWidget(this.tabNavigationBar);

        this.bottomButtons = (new GridLayout()).columnSpacing(10);
        GridLayout.RowHelper rowHelper = this.bottomButtons.createRowHelper(2);
        rowHelper.addChild(Button.builder(CommonComponents.GUI_DONE, (button) -> {
            this.minecraft.setScreen(this.parent);
        }).build());
        this.bottomButtons.visitWidgets((widget) -> {
            widget.setTabOrderGroup(1);
            this.addRenderableWidget(widget);
        });
        this.tabNavigationBar.selectTab(0, false);
        this.repositionElements();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderDirtBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(FOOTER_SEPARATOR, 0, Mth.roundToward(this.height - 36 - 2, 2), 0.0F, 0.0F, this.width, 2, 32, 2);
    }
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public void repositionElements() {
        if (this.tabNavigationBar != null && this.bottomButtons != null) {
            this.tabNavigationBar.setWidth(this.width);
            this.tabNavigationBar.arrangeElements();
            this.bottomButtons.arrangeElements();
            FrameLayout.centerInRectangle(this.bottomButtons, 0, this.height - 36, this.width, 36);
            int i = this.tabNavigationBar.getRectangle().bottom();
            ScreenRectangle screenRectangle = new ScreenRectangle(0, i, this.width, this.bottomButtons.getY() - i);
            this.tabManager.setTabArea(screenRectangle);
        }
    }

    @Environment(value= EnvType.CLIENT)
    static class GeneralTab
            extends GridLayoutTab {
        private static final Component TITLE = Component.translatable(ConfigManager.generalCategory.getTranslationKey());
        GeneralTab() {
            super(TITLE);
            this.layout.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter();
            GridLayout.RowHelper rowHelper = this.layout.createRowHelper(2);

            for (AbstractOption option : ConfigManager.generalCategory.getOptions()) {
                if (option instanceof BooleanConfigOption booleanOption) {
                    MutableComponent label = Component.translatable(booleanOption.getTranslationKey()).append(": ");

                    rowHelper.addChild(Button.builder(label.copy().append(booleanOption.getValue() ? "ON" : "OFF"), button -> {
                        booleanOption.toggleValue();
                        ConfigManager.updateConfig();
                        button.setMessage(label.copy().append(booleanOption.getValue() ? "ON" : "OFF"));
                    }).build());
                }
            }
            this.layout.arrangeElements();
        }
    }

}
