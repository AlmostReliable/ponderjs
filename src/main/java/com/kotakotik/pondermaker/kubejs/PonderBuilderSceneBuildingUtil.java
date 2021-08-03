package com.kotakotik.pondermaker.kubejs;

import com.google.gson.JsonObject;
import com.mojang.datafixers.types.Func;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.content.DeployerScenes;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.simibubi.create.foundation.ponder.elements.EntityElement;
import com.simibubi.create.foundation.ponder.elements.ParrotElement;
import dev.latvian.kubejs.BuiltinKubeJSPlugin;
import dev.latvian.kubejs.KubeJSRegistries;
import dev.latvian.kubejs.bindings.BlockWrapper;
import dev.latvian.kubejs.block.BlockJS;
import dev.latvian.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.kubejs.docs.MinecraftClass;
import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.player.PlayerDataJS;
import dev.latvian.kubejs.player.PlayerJS;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.kubejs.util.JsonUtilsJS;
import dev.latvian.kubejs.util.MapJS;
import dev.latvian.kubejs.util.NBTUtilsJS;
import dev.latvian.kubejs.world.WorldJS;
import me.shedaniel.architectury.utils.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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
    public EntityJS createEntity(World world, ResourceLocation id, Vector3d pos) {
        Entity entity = getEntity(id).create(world);
        entity.setPos(pos.x, pos.y, pos.z);
        return createEntityJS(world, entity);
    }

    public EntityJS createEntityJS(World world, Entity entity) {
        return new EntityJS(new WorldJS(world) {
            @Override
            public ScriptType getSide() {
                return ScriptType.STARTUP;
            }

            @Override
            public PlayerDataJS<?, ?> getPlayerData(PlayerEntity playerEntity) {
                return null;
            }
        }, entity);
    }

    public ElementLink<EntityElement> addEntity(ResourceLocation id, Vector3d pos) {
        return scene.world.createEntity(w -> createEntity(w, id, pos).minecraftEntity);
    }

    public ElementLink<EntityElement> addEntity(ResourceLocation id, BlockPos pos) {
        return addEntity(id, new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    // BlockWrapper.id(...).getBlockstate() throws an NPE because properties are null, so it's much easier to do it this way
    public BlockState getDefaultState(ResourceLocation id) {
        return BlockWrapper.getBlock(id).defaultBlockState();
    }

    public void modifyEntity(ElementLink<EntityElement> entity, Consumer<EntityJS> mod) {
        scene.world.modifyEntity(entity, (e) -> mod.accept(createEntityJS(e.level, e)));
    }

    public BlockIDPredicate getBlockStateJS(BlockState state) {
        BlockIDPredicate predicate = BlockWrapper.id(state.getBlock().getRegistryName());
        state.getValues().forEach((p, c) -> predicate.with(p.getName(), state.getValue(p).toString()));
        return predicate;
    }

    public void modifyBlock(BlockPos pos, boolean addParticles, Consumer<BlockIDPredicate> mod) {
        scene.world.modifyBlock(pos, (state) -> {
            BlockIDPredicate predicate = getBlockStateJS(state);
            mod.accept(predicate);
            return predicate.getBlockState();
        }, addParticles);
    }

    public void modifyBlock(BlockPos pos, Consumer<BlockIDPredicate> mod) {
        modifyBlock(pos, false, mod);
    }
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

    public void modifyTileNBT(Selection selection, UnaryOperator<Object> sup) {
        scene.world.modifyTileNBT(selection, TileEntity.class, nbt -> {
            CompoundNBT n = MapJS.nbt(sup.apply(MapJS.of(nbt)));
            nbt.merge(n);
        });
    }

    public void modifyTileNBT(BlockPos pos, UnaryOperator<Object>  obj) {
        modifyTileNBT(util.select.position(pos), obj);
    }

    public ItemStackJS stack(ResourceLocation item) {
        return ItemStackJS.of(item);
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
