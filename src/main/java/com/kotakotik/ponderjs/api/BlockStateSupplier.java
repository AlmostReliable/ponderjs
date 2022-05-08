package com.kotakotik.ponderjs.api;

import com.kotakotik.ponderjs.FunctionJS;
import com.kotakotik.ponderjs.PonderErrorHelper;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.RhinoException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public interface BlockStateSupplier extends Supplier<BlockState> {
    static BlockStateSupplier of(@Nullable Object o) {
        if (o == null) {
            throw new IllegalArgumentException("BlockIdPredicateFunction cannot be null");
        }

        if (o instanceof BlockStateSupplier s) return s;
        if (o instanceof BlockState blockState) return () -> blockState;
        if (o instanceof Block block) return block::defaultBlockState;
        if (o instanceof BlockIDPredicate predicate) return predicate::getBlockState;

        ResourceLocation location = ResourceLocation.tryParse(o.toString());
        if (location != null) {
            Block block = KubeJSRegistries.blocks().get(location);
            if (block != null) return block::defaultBlockState;
        }

        if (o instanceof dev.latvian.mods.rhino.Function function) {
                var ctx = Context.getCurrentContext();
                return () -> {
                    try {
                        Object result = function.call(ctx, function.getParentScope(), function, new Object[]{});
                        var sup = (BlockStateSupplier) Context.jsToJava(result, BlockStateSupplier.class);
                        return sup.get();
                    } catch (RhinoException e) {
                        PonderErrorHelper.yeet(e);
                    }
                    return Blocks.AIR.defaultBlockState();
                };
//            FunctionJS<BlockStateFunction> func = new FunctionJS<>(function, BlockStateFunction.class, $ -> Blocks.AIR.defaultBlockState(), Context.getCurrentContext());
//            return (bip) -> func.call().apply(bip);
//            }
//            FunctionJS<BlockStateSupplier> func = new FunctionJS<>(function, BlockStateSupplier.class, Blocks.AIR::defaultBlockState, Context.getCurrentContext());
//            return () -> func.call().get();
        }

        throw new IllegalArgumentException("Block or BlockState does not exist");
    }
}
