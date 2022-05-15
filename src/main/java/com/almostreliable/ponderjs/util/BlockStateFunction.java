package com.almostreliable.ponderjs.util;

import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.NativeJavaObject;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface BlockStateFunction extends Function<BlockIDPredicate, BlockState> {
    static BlockStateFunction of(@Nullable Object o) {
        if (o instanceof BaseFunction function) {
            //noinspection rawtypes
            Function f = (Function) NativeJavaObject.createInterfaceAdapter(Function.class, function);
            return blockIDPredicate -> {
                //noinspection unchecked
                Object result = f.apply(blockIDPredicate);
                return BlockStateFunction.of(result).apply(blockIDPredicate);
            };
        }

        BlockState blockState = Util.blockStateOf(o);
        return ($) -> blockState;
    }

    static UnaryOperator<BlockState> from(BlockStateFunction function) {
        return blockState -> {
            BlockIDPredicate predicate = Util.createBlockID(blockState);
            return function.apply(predicate);
        };
    }
}
