package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.extension.OverlayInstructionExtension;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.createmod.ponder.api.element.TextElementBuilder;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PonderSceneBuilder.PonderOverlayInstructions.class)
public abstract class PonderOverlayInstructionsMixin implements OverlayInstructionExtension {

    @Shadow(remap = false) @Final PonderSceneBuilder this$0;

    @Shadow
    @HideFromJS
    public abstract TextElementBuilder showText(int duration);

    @Override
    public PonderSceneBuilder ponderjs$builder() {
        return this$0;
    }
}
