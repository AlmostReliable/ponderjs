package com.almostreliable.ponderjs.mixin;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PonderSceneBuilder.PonderSpecialInstructions.class)
public abstract class PonderSpecialInstructionsMixin {

    @Shadow(remap = false)
    @HideFromJS
    public abstract void movePointOfInterest(BlockPos location);
}
