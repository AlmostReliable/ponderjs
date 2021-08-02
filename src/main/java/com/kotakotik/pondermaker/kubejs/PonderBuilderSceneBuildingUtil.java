package com.kotakotik.pondermaker.kubejs;

import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.simibubi.create.foundation.ponder.elements.ParrotElement;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
