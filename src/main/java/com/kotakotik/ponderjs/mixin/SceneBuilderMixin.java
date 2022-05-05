package com.kotakotik.ponderjs.mixin;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@Mixin(SceneBuilder.class)
public class SceneBuilderMixin {

    @Shadow
    @Final
    public SceneBuilder.WorldInstructions world;

    @Shadow
    @Final
    public SceneBuilder.OverlayInstructions overlay;

    @Shadow
    @Final
    public SceneBuilder.DebugInstructions debug;

    @Shadow
    @Final
    public SceneBuilder.EffectInstructions effects;

    @Shadow
    @Final
    public SceneBuilder.SpecialInstructions special;

    @RemapForJS("world")
    public SceneBuilder.WorldInstructions ponderjs$getWorld() {
        return world;
    }

    @RemapForJS("debug")
    public SceneBuilder.DebugInstructions ponderjs$getDebug() {
        return debug;
    }

    @RemapForJS("overlay")
    public SceneBuilder.OverlayInstructions ponderjs$getOverlay() {
        return overlay;
    }

    @RemapForJS("effects")
    public SceneBuilder.EffectInstructions ponderjs$getEffects() {
        return effects;
    }

    @RemapForJS("special")
    public SceneBuilder.SpecialInstructions ponderjs$getSpecial() {
        return special;
    }

    @Mixin(SceneBuilder.WorldInstructions.class)
    private abstract static class WorldInstructionsMixin {
        @Shadow
        public abstract ElementLink<EntityElement> createEntity(Function<Level, Entity> factory);

        @Shadow
        public abstract void modifyBlock(BlockPos pos, UnaryOperator<BlockState> stateFunc, boolean spawnParticles);

        @Shadow
        public abstract void modifyTileNBT(Selection selection, Class<? extends BlockEntity> teType, Consumer<CompoundTag> consumer, boolean reDrawBlocks);

        @RemapForJS("createEntity")
        public ElementLink<EntityElement> ponderjs$createEntity(EntityType<?> entityType, Vec3 position, Consumer<EntityJS> consumer) {
            return createEntity(level -> {
                Entity entity = entityType.create(level);
                Objects.requireNonNull(entity, "Could not create entity of type " + entityType.getRegistryName());
                entity.setPos(position);
                EntityJS entityJS = UtilsJS.getLevel(level).getEntity(entity);
                consumer.accept(entityJS);
                return entity;
            });
        }

        @RemapForJS("createEntity")
        public ElementLink<EntityElement> ponderjs$createEntity(EntityType<?> entityType, Vec3 position) {
            return ponderjs$createEntity(entityType, position, entity -> {
            });
        }

        @RemapForJS("modifyBlock")
        public void ponderjs$modifyBlock(BlockPos pos, Consumer<BlockIDPredicate> mod) {
            ponderjs$modifyBlock(pos, false, mod);
        }

        @RemapForJS("modifyBlock")
        public void ponderjs$modifyBlock(BlockPos pos, boolean addParticles, Consumer<BlockIDPredicate> mod) {
            modifyBlock(pos, blockState -> {
                BlockIDPredicate predicate = new BlockIDPredicate(blockState.getBlock().getRegistryName());
                mod.accept(predicate);
                return predicate.getBlockState();
            }, false);
        }

        @RemapForJS("modifyTileNBT")
        public void ponderjs$modifyTileNBT(Selection selection, Consumer<CompoundTag> consumer) {
            modifyTileNBT(selection, BlockEntity.class, consumer, false);
        }

        @RemapForJS("modifyTileNBT")
        public void ponderjs$modifyTileNBT(Selection selection, Consumer<CompoundTag> consumer, boolean reDrawBlocks) {
            modifyTileNBT(selection, BlockEntity.class, consumer, reDrawBlocks);
        }
    }

    @Mixin(SceneBuilder.SpecialInstructions.class)
    private static abstract class SpecialInstructionsMixin {
        @HideFromJS
        @Shadow
        public abstract void movePointOfInterest(BlockPos location);
    }
}
