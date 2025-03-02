package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.extension.WorldInstructionExtension;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.UnaryOperator;

@Mixin(PonderSceneBuilder.PonderWorldInstructions.class)
public abstract class PonderWorldInstructionMixin implements WorldInstructionExtension {

    @Shadow(remap = false) @Final PonderSceneBuilder this$0;

    @HideFromJS
    @Shadow(remap = false)
    public abstract void modifyBlocks(Selection selection, UnaryOperator<BlockState> stateFunc, boolean spawnParticles);

    @HideFromJS
    @Shadow(remap = false)
    public abstract void modifyBlock(BlockPos pos, UnaryOperator<BlockState> stateFunc, boolean spawnParticles);

    @Override
    public PonderSceneBuilder ponderjs$builder() {
        return this$0;
    }

}
