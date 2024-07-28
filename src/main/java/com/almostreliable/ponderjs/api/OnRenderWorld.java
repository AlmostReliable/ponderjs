package com.almostreliable.ponderjs.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.element.PonderElement;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

@FunctionalInterface
public interface OnRenderWorld {

    void renderWorld(RenderContext context);

    @FunctionalInterface
    interface Layer {
        void renderLayer(RenderContext context);

        record RenderContext(PonderElement getElement, PonderWorld getWorld, MultiBufferSource getBuffer,
                             RenderType getType, PoseStack getPoseStack, float getPartialTicks, float getFade) {}
    }


    record RenderContext(PonderElement getElement, PonderWorld getWorld, MultiBufferSource getBuffer,
                         PoseStack getPoseStack, float getPartialTicks, float getFade) {}
}
