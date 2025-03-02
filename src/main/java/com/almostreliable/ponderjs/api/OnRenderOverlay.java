package com.almostreliable.ponderjs.api;

import net.createmod.ponder.api.element.PonderOverlayElement;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.ui.PonderUI;
import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface OnRenderOverlay {

    void render(RenderContext context);

    record RenderContext(PonderOverlayElement getElement, PonderScene getScene, PonderUI getScreen,
                         GuiGraphics getGraphics, float getPartialTicks, float getFade) {}
}
