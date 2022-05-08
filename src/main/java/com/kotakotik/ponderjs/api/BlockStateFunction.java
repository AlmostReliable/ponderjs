package com.kotakotik.ponderjs.api;

import com.kotakotik.ponderjs.FunctionJS;
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
                        Object result = function.call(ctx, function.getParentScope(), function, new Object[]{blockIDPredicate});
                        var sup = (BlockStateFunction) Context.jsToJava(result, BlockStateFunction.class);
                        return sup.apply(blockIDPredicate);
                    } catch (RhinoException e) {
                        PonderErrorHelper.yeet(e);
                    }
                    return Blocks.AIR.defaultBlockState();
                };
//            FunctionJS<BlockStateFunction> func = new FunctionJS<>(function, BlockStateFunction.class, $ -> Blocks.AIR.defaultBlockState(), Context.getCurrentContext());
//            return (bip) -> func.call().apply(bip);
        }

        BlockStateSupplier supplier = BlockStateSupplier.of(o);
        return ($) -> supplier.get();
    }
}
