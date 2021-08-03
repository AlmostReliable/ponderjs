package com.kotakotik.pondermaker.crafttweaker.wrappers.implementing;

import com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces.IBlockPos;
import net.minecraft.util.math.BlockPos;

public class BlockPosWrapper implements IBlockPos {
    private final BlockPos internal;

    public BlockPosWrapper(BlockPos internal) {
        this.internal = internal;
    }

    @Override
    public BlockPos getInternal() {
        return internal;
    }
}
