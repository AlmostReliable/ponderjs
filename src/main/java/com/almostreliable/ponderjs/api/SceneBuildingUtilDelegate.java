package com.almostreliable.ponderjs.api;

import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SceneBuildingUtilDelegate {
    private final SceneBuildingUtil util;

    public SceneBuildingUtilDelegate(SceneBuildingUtil util) {
        this.util = util;
    }

    public SceneBuildingUtil.PositionUtil getGrid() {
        return util.grid;
    }

    public SceneBuildingUtil.SelectionUtil getSelect() {
        return util.select;
    }

    public SceneBuildingUtil.VectorUtil getVector() {
        return util.vector;
    }


    public BlockState getDefaultState(Block block) {
        return block.defaultBlockState();
    }
}
