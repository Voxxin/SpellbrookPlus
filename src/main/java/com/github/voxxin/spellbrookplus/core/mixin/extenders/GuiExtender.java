package com.github.voxxin.spellbrookplus.core.mixin.extenders;

import net.minecraft.network.chat.Component;

public interface GuiExtender {
    /*
     * Action Bar
     */
    Component sp$getOverlayMessage();
    Component sp$getTitleMessage();
    Component sp$getSubtitleMessage();

    void sp$setOverlayMessage(Component message);
    void sp$setTitleMessage(Component message);
    void sp$setSubtitleMessage(Component message);
}
