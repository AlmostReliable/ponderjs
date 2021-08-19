package com.kotakotik.pondermaker.kubejs.util;

import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import dev.latvian.kubejs.bindings.BlockWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class SceneBuildingUtilJS implements ISceneBuildingUtilJS {
    protected final SceneBuildingUtil internal;

    public SceneBuildingUtilJS(SceneBuildingUtil internal) {
        this.internal = internal;
    }

    @Override
    public SceneBuildingUtil getInternal() {
        return internal;
    }

    public RegistryObject<Block> block(ResourceLocation id) {
        return RegistryObject.of(id, ForgeRegistries.BLOCKS);
    }

    public RegistryObject<Item> item(ResourceLocation id) {
        return RegistryObject.of(id, ForgeRegistries.ITEMS);
    }

    // BlockWrapper.id(...).getBlockstate() throws an NPE because properties are null, so it's much easier to do it this way
    public BlockState getDefaultState(ResourceLocation id) {
        return BlockWrapper.getBlock(id).defaultBlockState();
    }
}
