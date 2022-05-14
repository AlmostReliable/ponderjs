package com.almostreliable.ponderjs.util;

import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.RhinoException;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BlockStateSupplier extends Supplier<BlockState> {
    static BlockStateSupplier of(@Nullable Object o) {
        if (o == null) {
            throw new IllegalArgumentException("BlockStateSupplier cannot be null");
        }

        if (o instanceof BlockStateSupplier s) return s;
        if (o instanceof BlockState blockState) return () -> blockState;
        if (o instanceof Block block) return block::defaultBlockState;
        if (o instanceof BlockIDPredicate predicate) return predicate::getBlockState;

        if (o instanceof BaseFunction function) {
            //noinspection rawtypes
            Supplier s = (Supplier) NativeJavaObject.createInterfaceAdapter(Supplier.class, function);
            return () -> {
                try {
                    Object result = s.get();
                    return BlockStateSupplier.of(result).get();
                } catch (RhinoException e) {
                    PonderErrorHelper.yeet(e);
                }
                return Blocks.AIR.defaultBlockState();
            };
        }

        ItemStackJS itemStack = ItemStackJS.of(o);
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof BlockItem bi) {
            return bi.getBlock()::defaultBlockState;
        }

        ConsoleJS.CLIENT.error("Invalid blockstate supplier: " + o);
        return Blocks.AIR::defaultBlockState;
    }
}
