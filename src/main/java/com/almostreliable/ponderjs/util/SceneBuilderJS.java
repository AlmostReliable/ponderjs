package com.almostreliable.ponderjs.util;

import com.jozufozu.flywheel.util.Lazy;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.bindings.BlockWrapper;
import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class SceneBuilderJS implements ISceneBuilderJS {
    public final SceneBuilder internal;
    //    public final PonderPaletteWrapper palette = new PonderPaletteWrapper();
    public Lazy<WorldInstructionsJS> worldInstructionsJS = new Lazy<>(() ->
            new WorldInstructionsJS(getInternal().world, this));

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
//    public JsonObject nbtToJson(CompoundTag nbt) {
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
    public Lazy<SpecialInstructionsJS> specialInstructionsJS = new Lazy<>(() ->
            new SpecialInstructionsJS(getInternal().special));

    public SceneBuilderJS(SceneBuilder scene) {
        this.internal = scene;
    }

    @Override
    public SceneBuilder getInternal() {
        return internal;
    }

    @Override
    public SceneBuilder.OverlayInstructions getOverlay() {
        return getInternal().overlay;
    }

    @Override
    public WorldInstructionsJS getWorld() {
        return worldInstructionsJS.get();
    }

    @Override
    public SceneBuilder.DebugInstructions getDebug() {
        return getInternal().debug;
    }

    @Override
    public SceneBuilder.EffectInstructions getEffects() {
        return getInternal().effects;
    }

    @Override
    public SpecialInstructionsJS getSpecial() {
        return specialInstructionsJS.get();
    }

    public static class SpecialInstructionsJS implements ISpecialInstructionsJS {

        private SceneBuilder.SpecialInstructions internal;

        public SpecialInstructionsJS(SceneBuilder.SpecialInstructions internal) {
            this.internal = internal;
        }

        @Override
        public SceneBuilder.SpecialInstructions getInternal() {
            return internal;
        }
    }

    public static class WorldInstructionsJS implements ISceneBuilderJS.IWorldInstructionsJS {

        private final SceneBuilder.WorldInstructions internal;
        private final SceneBuilderJS sceneBuilder;

        public WorldInstructionsJS(SceneBuilder.WorldInstructions internal, SceneBuilderJS sceneBuilder) {
            this.internal = internal;
            this.sceneBuilder = sceneBuilder;
        }

        public Consumer<CompoundTag> mapJsConsumerToNBT(Object pos, UnaryOperator<CompoundTag> sup) {
            return nbt -> {
                Objects.requireNonNull(nbt, "Could not find NBT in selection " +
                                            pos + ", your selection might include non-tiles!");
                CompoundTag n = Objects.requireNonNull(sup.apply(Objects.requireNonNull(nbt)),
                        "Null returned for tile NBT");
                nbt.merge(n);
            };
        }

        public void updateTileNBT(Selection selection, UnaryOperator<CompoundTag> sup) {
            modifyTileNBT(selection, BlockEntity.class, mapJsConsumerToNBT(selection, sup));
        }

        public void modifyTileNBT(Selection selection, CompoundTag obj) {
            updateTileNBT(selection, $ -> obj);
        }

        public void updateTileNBT(Selection selection, Class<? extends BlockEntity> teType, UnaryOperator<CompoundTag> sup, boolean reDrawBlocks) {
//            TypeToken<?> t = TypeSetKubeJSRegistries.blockEntities().get(teType);
            modifyTileNBT(selection, teType, mapJsConsumerToNBT(selection, sup), reDrawBlocks);
        }

        public void updateTileNBT(Selection selection, UnaryOperator<CompoundTag> sup, boolean reDrawBlocks) {
            updateTileNBT(selection, BlockEntity.class, sup, reDrawBlocks);
        }

        public void modifyTileNBT(Selection selection, Class<? extends BlockEntity> teType, CompoundTag obj, boolean reDrawBlocks) {
            updateTileNBT(selection, teType, $ -> obj, reDrawBlocks);
        }

        public void modifyTileNBT(Selection selection, CompoundTag obj, boolean reDrawBlocks) {
            modifyTileNBT(selection, BlockEntity.class, obj, reDrawBlocks);
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

        public void modifyEntity(ElementLink<EntityElement> entity, Consumer<EntityJS> mod) {
            internal.modifyEntity(entity, (e) ->
                    mod.accept(new EntityJS(
                            UtilsJS.getLevel(e.level), e)));
        }

        public ElementLink<EntityElement> createEntity(ResourceLocation id, Vec3 pos, UnaryOperator<EntityJS> mod) {
            return internal.createEntity(w -> mod.apply(createEntityJS(w, id, pos)).minecraftEntity);
        }

        public ElementLink<EntityElement> createEntity(ResourceLocation id, Vec3 pos) {
            return createEntity(id, pos, e -> e);
        }

        public EntityJS createEntityJS(Level world, ResourceLocation id, Vec3 pos) {
            Entity entity = getEntity(id).create(world);
            entity.setPos(pos.x, pos.y, pos.z);
            return new EntityJS(UtilsJS.getLevel(world), entity);
        }

        public EntityType<?> getEntity(ResourceLocation id) {
            return KubeJSRegistries.entityTypes().get(id);
        }
    }
}
