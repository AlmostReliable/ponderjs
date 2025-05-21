package com.almostreliable.ponderjs.util;

import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface BlockStateFunction extends Function<BlockIDPredicate, BlockState> {

    TypeInfo FUNCTION_TYPE = TypeInfo.of(Function.class);
    TypeInfo BLOCK_STATE_TYPE = TypeInfo.of(BlockState.class);

    static BlockStateFunction of(Context ctx, @Nullable Object o) {
        if (o instanceof BaseFunction function) {
            //noinspection rawtypes
            Function f = (Function) ctx.createInterfaceAdapter(FUNCTION_TYPE, function);
            return blockIDPredicate -> {
                //noinspection unchecked
                Object result = f.apply(blockIDPredicate);
                return BlockStateFunction.of(ctx, result).apply(blockIDPredicate);
            };
        }

        BlockState blockState = (BlockState) ctx.jsToJava(o, BLOCK_STATE_TYPE);
        return ($) -> blockState;
    }

    static UnaryOperator<BlockState> from(BlockStateFunction function) {
        return blockState -> {
            BlockIDPredicate predicate = Util.createBlockID(blockState);
            return function.apply(predicate);
        };
    }
}
