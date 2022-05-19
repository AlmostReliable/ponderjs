package com.almostreliable.ponderjs.mixin;

import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SceneBuilder.class)
public interface SceneBuilderAccessor {
    @Accessor(value = "scene", remap = false)
    PonderScene ponderjs$getPonderScene();

    @Accessor(value = "world", remap = false)
    @Mutable
    void ponderjs$setWorldInstructions(SceneBuilder.WorldInstructions worldInstructions);

    @Accessor(value = "special", remap = false)
    @Mutable
    void ponderjs$setSpecialInstructions(SceneBuilder.SpecialInstructions specialInstructions);
}
