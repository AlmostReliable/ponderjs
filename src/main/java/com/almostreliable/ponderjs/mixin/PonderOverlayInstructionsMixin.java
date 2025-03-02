package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.extension.OverlayInstructionExtension;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PonderSceneBuilder.PonderOverlayInstructions.class)
public class PonderOverlayInstructionsMixin implements OverlayInstructionExtension {

    @Shadow(remap = false) @Final PonderSceneBuilder this$0;

    @Override
    public PonderSceneBuilder ponderjs$builder() {
        return this$0;
    }
}
