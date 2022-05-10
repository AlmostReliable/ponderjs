package com.kotakotik.ponderjs.util;

import com.kotakotik.ponderjs.PonderErrorHelper;
import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.RhinoException;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Function;

public interface BlockStateFunction extends Function<BlockIDPredicate, BlockState> {
    static BlockStateFunction of(@Nullable Object o) {
        if (o instanceof dev.latvian.mods.rhino.Function function) {
            var ctx = Context.getCurrentContext();
            return (blockIDPredicate) -> {
                try {
                    Object result = function.call(ctx,
                            function.getParentScope(),
                            function,
                            new Object[]{ blockIDPredicate });
                    return BlockStateFunction.of(result).apply(blockIDPredicate);
                } catch (RhinoException e) {
                    PonderErrorHelper.yeet(e);
                }
                return Blocks.AIR.defaultBlockState();
            };
        }

        BlockStateSupplier supplier = BlockStateSupplier.of(o);
        return ($) -> supplier.get();
    }
}
