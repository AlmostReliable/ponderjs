package com.kotakotik.ponderjs.mixin;

import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SceneBuildingUtil.class)
public class SceneBuildingUtilMixin {
    @Shadow
    @Final
    public SceneBuildingUtil.PositionUtil grid;

    @Shadow
    @Final
    public SceneBuildingUtil.SelectionUtil select;

    @Shadow
    @Final
    public SceneBuildingUtil.VectorUtil vector;

    @Shadow
    @Final
    private BoundingBox sceneBounds;

    @RemapForJS("grid")
    public SceneBuildingUtil.PositionUtil ponderjs$getGrid() {
        return grid;
    }

    @RemapForJS("select")
    public SceneBuildingUtil.SelectionUtil ponderjs$getSelect() {
        return select;
    }

    @RemapForJS("vector")
    public SceneBuildingUtil.VectorUtil ponderjs$getVector() {
        return vector;
    }

    @RemapForJS("sceneBounds")
    public BoundingBox ponderjs$getSceneBounds() {
        return sceneBounds;
    }

    @RemapForJS("getDefaultState")
    public BlockState ponderjs$getDefaultState(Block block) {
        return block.defaultBlockState();
    }
}
