package com.kotakotik.ponderjs.util;

import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import dev.latvian.mods.kubejs.bindings.BlockWrapper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
