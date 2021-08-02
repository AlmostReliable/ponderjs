package com.kotakotik.pondermaker.kubejs;

import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.simibubi.create.foundation.ponder.elements.ParrotElement;
import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.KubeJSRegistries;
import dev.latvian.kubejs.docs.MinecraftClass;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

public class PonderBuilderSceneBuildingUtil {
    public final SceneBuilder scene;
    public final SceneBuildingUtil util;
    public final PonderPaletteWrapper palette = new PonderPaletteWrapper();

    public PonderBuilderSceneBuildingUtil(SceneBuilder scene, SceneBuildingUtil util) {
        this.scene = scene;
        this.util = util;
    }

    public RegistryObject<Block> block(ResourceLocation id) {
        return RegistryObject.of(id, ForgeRegistries.BLOCKS);
    }

    public RegistryObject<Item> item(ResourceLocation id) {
        return RegistryObject.of(id, ForgeRegistries.ITEMS);
    }

    @MinecraftClass // not sure what this does but kubejs uses it so
    public EntityType<?> getEntity(ResourceLocation id) {
        return KubeJSRegistries.entityTypes().get(id);
    }

    @MinecraftClass
    public Entity createEntity(World world, ResourceLocation id, Vector3d pos) {
        Entity entity = getEntity(id).create(world);
        entity.setPos(pos.x, pos.y, pos.z);
        return entity;
    }

    @MinecraftClass
    public Entity createEntity(World world, ResourceLocation id, BlockPos pos) {
        return createEntity(world, id, new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    public ParrotElement.DancePose dancePose() {
        return new ParrotElement.DancePose();
    }

    public ParrotElement.FacePointOfInterestPose pointOfInterestPose() {
        return new ParrotElement.FacePointOfInterestPose();
    }

    public static class PonderPaletteWrapper {
        public final PonderPalette white = PonderPalette.WHITE;
        public final PonderPalette black = PonderPalette.BLACK;
        public final PonderPalette red = PonderPalette.RED;
        public final PonderPalette green =  PonderPalette.GREEN;
        public final PonderPalette blue = PonderPalette.BLUE;
        public final PonderPalette slow = PonderPalette.SLOW;
        public final PonderPalette medium = PonderPalette.MEDIUM;
        public final PonderPalette fast = PonderPalette.FAST;
        public final PonderPalette input = PonderPalette.INPUT;
        public final PonderPalette output = PonderPalette.OUTPUT;
    }
}
