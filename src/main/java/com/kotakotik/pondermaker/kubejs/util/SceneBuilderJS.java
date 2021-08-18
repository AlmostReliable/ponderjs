package com.kotakotik.pondermaker.kubejs.util;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.elements.EntityElement;
import dev.latvian.kubejs.KubeJSRegistries;
import dev.latvian.kubejs.bindings.BlockWrapper;
import dev.latvian.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.util.MapJS;
import dev.latvian.kubejs.util.UtilsJS;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class SceneBuilderJS implements ISceneBuilderJS {
    public final SceneBuilder internal;
//    public final PonderPaletteWrapper palette = new PonderPaletteWrapper();

    public SceneBuilderJS(SceneBuilder scene) {
        this.internal = scene;
    }

    public RegistryObject<Block> block(ResourceLocation id) {
        return RegistryObject.of(id, ForgeRegistries.BLOCKS);
    }

    public RegistryObject<Item> item(ResourceLocation id) {
        return RegistryObject.of(id, ForgeRegistries.ITEMS);
    }

//    public EntityJS createEntityJS(World world, Entity entity) {
//        return new EntityJS(new WorldJS(world) {
//            @Override
//            public ScriptType getSide() {
//                return ScriptType.STARTUP;
//            }
//
//            @Override
//            public PlayerDataJS<?, ?> getPlayerData(PlayerEntity playerEntity) {
//                return null;
//            }
//        }, entity);
//    }

// aaaaaa i could have just used MapJS.of(nbt)
//    public JsonObject nbtToJson(CompoundNBT nbt) {
//        BuiltinKubeJSPlugin
//        JsonObject json = new JsonObject();
//        for(String key : nbt.getAllKeys()) {
//            INBT val = nbt.get(key);
//            if(val instanceof ByteNBT) {
//                json.addProperty(key, nbt.getBoolean(key));
//            } else if(val instanceof NumberNBT) {
//                json.addProperty(key, ((NumberNBT) val).getAsNumber());
//            } /* else if(val instanceof StringNBT) {
//                json.addProperty(key, val.toString());
//            } */ else {
//                json.addProperty(key, val.toString());
//            }
////            json.addProperty(key, );
//        }
//        return json;
//    }


    @Override
    public SceneBuilderJS asSceneBuilderJS() {
        return this;
    }

    @Override
    public SceneBuilder getInternal() {
        return internal;
    }

    public static class WorldInstructionsJS implements ISceneBuilderJS.IWorldInstructionsJS {

        private final SceneBuilder.WorldInstructions internal;
        private final SceneBuilderJS sceneBuilder;

        public WorldInstructionsJS(SceneBuilder.WorldInstructions internal, SceneBuilderJS sceneBuilder) {
            this.internal = internal;
            this.sceneBuilder = sceneBuilder;
        }

        public void modifyTileNBT(Selection selection, UnaryOperator<MapJS> sup) {
            modifyTileNBT(selection, TileEntity.class, nbt -> {
                CompoundNBT n = MapJS.nbt(sup.apply(MapJS.of(nbt)));
                nbt.merge(n);
            });
        }

        @Override
        public SceneBuilder.WorldInstructions getInternal() {
            return internal;
        }

        public BlockIDPredicate getBlockStateJS(BlockState state) {
            BlockIDPredicate predicate = BlockWrapper.id(state.getBlock().getRegistryName());
            state.getValues().forEach((p, c) -> predicate.with(p.getName(), state.getValue(p).toString()));
            return predicate;
        }

        public void modifyBlock(BlockPos pos, boolean addParticles, Consumer<BlockIDPredicate> mod) {
            modifyBlock(pos, (state) -> {
                BlockIDPredicate predicate = getBlockStateJS(state);
                mod.accept(predicate);
                return predicate.getBlockState();
            }, addParticles);
        }

        public void modifyBlock(BlockPos pos, Consumer<BlockIDPredicate> mod) {
            modifyBlock(pos, false, mod);
        }

        // BlockWrapper.id(...).getBlockstate() throws an NPE because properties are null, so it's much easier to do it this way
        public BlockState getDefaultState(ResourceLocation id) {
            return BlockWrapper.getBlock(id).defaultBlockState();
        }

        public void modifyEntity(ElementLink<EntityElement> entity, Consumer<EntityJS> mod) {
            internal.modifyEntity(entity, (e) ->
                    mod.accept(new EntityJS(
                            UtilsJS.getWorld(e.level), e)));
        }

        public ElementLink<EntityElement> createEntity(ResourceLocation id, Vector3d pos) {
            return internal.createEntity(w -> createEntity(w, id, pos).minecraftEntity);
        }

        public EntityJS createEntity(World world, ResourceLocation id, Vector3d pos) {
            Entity entity = getEntity(id).create(world);
            entity.setPos(pos.x, pos.y, pos.z);
            return new EntityJS(UtilsJS.getWorld(world), entity);
        }

        public EntityType<?> getEntity(ResourceLocation id) {
            return KubeJSRegistries.entityTypes().get(id);
        }
    }
}
