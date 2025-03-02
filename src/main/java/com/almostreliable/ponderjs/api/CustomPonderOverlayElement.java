package com.almostreliable.ponderjs.api;


import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.element.AnimatedOverlayElementBase;
import net.createmod.ponder.foundation.ui.PonderUI;
import net.minecraft.client.gui.GuiGraphics;

public class CustomPonderOverlayElement extends AnimatedOverlayElementBase {
    protected OnRenderOverlay onRender = (ctx) -> {};
    protected OnElementAction onWhileSkipping = (ctx) -> {};
    protected OnElementAction onTick = (ctx) -> {};
    protected OnElementAction onReset = (ctx) -> {};
    private int currentTick = 0;

    public int getCurrentTick() {
        return currentTick;
    }

    public CustomPonderOverlayElement onSkipping(OnElementAction onWhileSkipping) {
        this.onWhileSkipping = onWhileSkipping;
        return this;
    }

    public CustomPonderOverlayElement onTick(OnElementAction onTick) {
        this.onTick = onTick;
        return this;
    }

    public CustomPonderOverlayElement onReset(OnElementAction onReset) {
        this.onReset = onReset;
        return this;
    }

    public CustomPonderOverlayElement onRender(OnRenderOverlay onRender) {
        this.onRender = onRender;
        return this;
    }

    @Override
    public void whileSkipping(PonderScene scene) {
        super.whileSkipping(scene);
        onWhileSkipping.accept(new OnElementAction.Context(this, scene));
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        currentTick++;
        onTick.accept(new OnElementAction.Context(this, scene));
    }

    @Override
    public void reset(PonderScene scene) {
        super.reset(scene);
        currentTick = 0;
        onReset.accept(new OnElementAction.Context(this, scene));
    }

    @Override
    public void render(PonderScene scene, PonderUI screen, GuiGraphics graphics, float partialTicks, float fade) {
        var ctx = new OnRenderOverlay.RenderContext(this, scene, screen, graphics, partialTicks, fade);
        onRender.render(ctx);
    }
}
