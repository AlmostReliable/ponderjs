package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.extension.SceneBuilderExtension;
import com.almostreliable.ponderjs.particles.ParticleInstructions;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.createmod.ponder.api.scene.*;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PonderSceneBuilder.class)
public abstract class PonderSceneBuilderMixin implements SceneBuilderExtension {

    @Unique
    private final ParticleInstructions ponderjs$particles = new ParticleInstructions((SceneBuilder) (Object) this);

    @Shadow(remap = false) @Final protected PonderScene scene;

    @RemapForJS("getWorld")
    @Shadow(remap = false)
    public abstract WorldInstructions world();

    @RemapForJS("getDebug")
    @Shadow(remap = false)
    public abstract DebugInstructions debug();

    @RemapForJS("getOverlay")
    @Shadow(remap = false)
    public abstract OverlayInstructions overlay();

    @RemapForJS("getEffects")
    @Shadow(remap = false)
    public abstract EffectInstructions effects();

    @RemapForJS("getSpecial")
    @Shadow(remap = false)
    public abstract SpecialInstructions special();

    @Override
    public PonderScene ponderjs$getScene() {
        return scene;
    }

    @Override
    public ParticleInstructions ponderjs$getParticles() {
        return ponderjs$particles;
    }
}
