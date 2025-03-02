package com.almostreliable.ponderjs.mixin;

import dev.latvian.mods.rhino.util.RemapForJS;
import net.createmod.ponder.api.scene.PositionUtil;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.SelectionUtil;
import net.createmod.ponder.api.scene.VectorUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SceneBuildingUtil.class)
public abstract class SceneBuildingUtilMixin {

    @Shadow(remap = false)
    @RemapForJS("getGrid")
    public abstract PositionUtil grid();

    @Shadow(remap = false)
    @RemapForJS("getSelect")
    public abstract SelectionUtil select();

    @Shadow(remap = false)
    @RemapForJS("getVector")
    public abstract VectorUtil vector();

    @Unique
    @RemapForJS("getDefaultState")
    public BlockState ponderjs$getDefaultState(Block block) {
        return block.defaultBlockState();
    }

}
