package com.almostreliable.ponderjs.api;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.element.AnimatedSceneElementBase;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class CustomPonderSceneElement extends AnimatedSceneElementBase {
    protected OnRenderWorld onRenderFirst = (ctx) -> {};
    protected OnRenderWorld.Layer onRenderWorldLayer = (ctx) -> {};
    protected OnRenderWorld onRenderLast = (ctx) -> {};
    protected OnElementAction onWhileSkipping = (ctx) -> {};
    protected OnElementAction onTick = (ctx) -> {};
    protected OnElementAction onReset = (ctx) -> {};
    private int currentTick = 0;

    public int getCurrentTick() {
        return currentTick;
    }

    public CustomPonderSceneElement onSkipping(OnElementAction onWhileSkipping) {
        this.onWhileSkipping = onWhileSkipping;
        return this;
    }

    public CustomPonderSceneElement onTick(OnElementAction onTick) {
        this.onTick = onTick;
        return this;
    }

    public CustomPonderSceneElement onReset(OnElementAction onReset) {
        this.onReset = onReset;
        return this;
    }

    public CustomPonderSceneElement onRenderFirst(OnRenderWorld onRenderWorld) {
        this.onRenderFirst = onRenderWorld;
        return this;
    }

    public CustomPonderSceneElement onRender(OnRenderWorld.Layer onRenderWorldLayer) {
        this.onRenderWorldLayer = onRenderWorldLayer;
        return this;
    }

    public CustomPonderSceneElement onRenderLast(OnRenderWorld onRenderWorld) {
        this.onRenderLast = onRenderWorld;
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


    @HideFromJS
    @Override
    public void renderFirst(PonderLevel world, MultiBufferSource buffer, GuiGraphics graphics, float fade, float pt) {
        super.renderFirst(world, buffer, graphics, fade, pt);
        var ctx = new OnRenderWorld.RenderContext(this, world, buffer, graphics, pt, fade);
        onRenderFirst.renderWorld(ctx);
    }

    @HideFromJS
    @Override
    public void renderLayer(PonderLevel world, MultiBufferSource buffer, RenderType type, GuiGraphics graphics, float fade, float pt) {
        super.renderLayer(world, buffer, type, graphics, fade, pt);
        var ctx = new OnRenderWorld.Layer.RenderContext(this, world, buffer, type, graphics, pt, fade);
        onRenderWorldLayer.renderLayer(ctx);
    }

    @HideFromJS
    @Override
    public void renderLast(PonderLevel world, MultiBufferSource buffer, GuiGraphics graphics, float fade, float pt) {
        super.renderLast(world, buffer, graphics, fade, pt);
        var ctx = new OnRenderWorld.RenderContext(this, world, buffer, graphics, pt, fade);
        onRenderLast.renderWorld(ctx);
    }
}
