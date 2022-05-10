package com.almostreliable.ponderjs.util;

import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import dev.latvian.mods.kubejs.bindings.BlockWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

// TODO move into Scene too
public class SceneBuildingUtilJS implements ISceneBuildingUtilJS {
    protected final SceneBuildingUtil internal;

    public SceneBuildingUtilJS(SceneBuildingUtil internal) {
        this.internal = internal;
    }

    @Override
    public SceneBuildingUtil getInternal() {
        return internal;
    }

    // BlockWrapper.id(...).getBlockstate() throws an NPE because properties are null, so it's much easier to do it this way
    public BlockState getDefaultState(ResourceLocation id) {
        return BlockWrapper.getBlock(id).defaultBlockState();
    }
}
