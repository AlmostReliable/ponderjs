package com.almostreliable.ponderjs.util;

import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.RhinoException;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Function;

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

        BlockStateSupplier supplier = BlockStateSupplier.of(o);
        return ($) -> supplier.get();
    }
}
