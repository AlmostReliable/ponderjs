package com.almostreliable.ponderjs.api;

import net.createmod.ponder.api.element.PonderElement;
import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

@FunctionalInterface
public interface OnRenderWorld {

    void renderWorld(RenderContext context);

    @FunctionalInterface
    interface Layer {
        void renderLayer(RenderContext context);

        record RenderContext(PonderElement getElement, PonderLevel getWorld, MultiBufferSource getBuffer,
                             RenderType getType, GuiGraphics getGraphics, float getPartialTicks, float getFade) {}
    }


    record RenderContext(PonderElement getElement, PonderLevel getWorld, MultiBufferSource getBuffer,
                         GuiGraphics getGraphics, float getPartialTicks, float getFade) {}
}
