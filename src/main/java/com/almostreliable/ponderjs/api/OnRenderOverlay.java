package com.almostreliable.ponderjs.api;

import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.element.PonderOverlayElement;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface OnRenderOverlay {

    void render(RenderContext context);

    record RenderContext(PonderOverlayElement getElement, PonderScene getScene, PonderUI getScreen,
                         GuiGraphics getGraphics, float getPartialTicks, float getFade) {}
}
