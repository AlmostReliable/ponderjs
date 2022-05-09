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
    @Shadow(remap = false)
    @Final
    public SceneBuildingUtil.PositionUtil grid;

    @Shadow(remap = false)
    @Final
    public SceneBuildingUtil.SelectionUtil select;

    @Shadow(remap = false)
    @Final
    public SceneBuildingUtil.VectorUtil vector;

    @Shadow(remap = false)
    @Final
    private BoundingBox sceneBounds;

    @RemapForJS("getGrid")
    public SceneBuildingUtil.PositionUtil ponderjs$getGrid() {
        return grid;
    }

    @RemapForJS("getSelect")
    public SceneBuildingUtil.SelectionUtil ponderjs$getSelect() {
        return select;
    }

    @RemapForJS("getVector")
    public SceneBuildingUtil.VectorUtil ponderjs$getVector() {
        return vector;
    }

    @RemapForJS("getSceneBounds")
    public BoundingBox ponderjs$getSceneBounds() {
        return sceneBounds;
    }

    @RemapForJS("getDefaultState")
    public BlockState ponderjs$getDefaultState(Block block) {
        return block.defaultBlockState();
    }
}
