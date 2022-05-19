package com.almostreliable.ponderjs.api;

import com.almostreliable.ponderjs.mixin.SceneBuilderAccessor;
import com.almostreliable.ponderjs.particles.ParticleInstructions;
import com.almostreliable.ponderjs.util.BlockStateFunction;
import com.almostreliable.ponderjs.util.PonderPlatform;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.TextWindowElement;
import com.simibubi.create.foundation.ponder.instruction.ShowInputInstruction;
import com.simibubi.create.foundation.utility.Pointing;
import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ExtendedSceneBuilder extends SceneBuilder {
    private final ParticleInstructions particles;
    private final PonderScene ponderScene;

    public ExtendedSceneBuilder(PonderScene ponderScene) {
        super(ponderScene);
        this.ponderScene = ponderScene;
        ((SceneBuilderAccessor) this).ponderjs$setWorldInstructions(new ExtendedWorldInstructions());
        ((SceneBuilderAccessor) this).ponderjs$setSpecialInstructions(new ExtendedSpecialInstructions());
        this.particles = new ParticleInstructions(this);
    }

    public ExtendedWorldInstructions getWorld() {
        return (ExtendedWorldInstructions) world;
    }

    public ExtendedWorldInstructions getLevel() {
        return (ExtendedWorldInstructions) world;
    }

    public SceneBuilder.DebugInstructions getDebug() {
        return debug;
    }

    public SceneBuilder.OverlayInstructions getOverlay() {
        return overlay;
    }

    public SceneBuilder.EffectInstructions getEffects() {
        return effects;
    }

    public SceneBuilder.SpecialInstructions getSpecial() {
        return special;
    }

    public ParticleInstructions getParticles() {
        return particles;
    }

    public void showStructure() {
        showStructure(ponderScene.getBasePlateSize() * 2);
    }

    public void showStructure(int height) {
        BlockPos start = new BlockPos(ponderScene.getBasePlateOffsetX(), 0, ponderScene.getBasePlateOffsetZ());
        Vec3i size = new Vec3i(ponderScene.getBasePlateSize() - 1, height, ponderScene.getBasePlateSize() - 1);
        Selection selection = ponderScene.getSceneBuildingUtil().select.cuboid(start, size);
        world.showSection(selection, Direction.UP);
    }

    public TextWindowElement.Builder text(int duration, String text) {
        return overlay.showText(duration).text(text);
    }

    public TextWindowElement.Builder text(int duration, String text, Vec3 position) {
        return overlay.showText(duration).text(text).pointAt(position);
    }

    public TextWindowElement.Builder sharedText(int duration, ResourceLocation key) {
        return overlay.showText(duration).sharedText(key);
    }

    public TextWindowElement.Builder sharedText(int duration, ResourceLocation key, Vec3 position) {
        return overlay.showText(duration).sharedText(key).pointAt(position).colored(PonderPalette.BLUE);
    }

    public InputWindowElement showControls(int duration, Vec3 pos, Pointing pointing) {
        InputWindowElement element = new InputWindowElement(pos, pointing);
        // we use own instruction to avoid `element.clone()`
        addInstruction(new ShowInputInstruction(element, duration));
        return element;
    }

    public class ExtendedWorldInstructions extends WorldInstructions {
        /**
         * Create a new entity with some default behavior. The entity will be rotated to face north.
         *
         * @param entityType The type of entity to create.
         * @param position   The position to create the entity at.
         * @param consumer   Callback to modify the entity.
         * @return An entity link which can be used later on.
         */
        public ElementLink<EntityElement> createEntity(EntityType<?> entityType, Vec3 position, Consumer<EntityJS> consumer) {
            return createEntity(level -> {
                Entity entity = entityType.create(level);
                Objects.requireNonNull(entity, "Could not create entity of type " +
                                               PonderPlatform.getEntityTypeName(entityType));
                entity.setPosRaw(position.x, position.y, position.z);
                entity.setOldPosAndRot();
                entity.lookAt(EntityAnchorArgument.Anchor.FEET, position.add(0, 0, -1));
                EntityJS entityJS = UtilsJS.getLevel(level).getEntity(entity);
                consumer.accept(entityJS);
                return entity;
            });
        }

        public ElementLink<EntityElement> createEntity(EntityType<?> entityType, Vec3 position) {
            return createEntity(entityType, position, entity -> {
            });
        }

        /**
         * Short version for modify blocks with default spawn particles.
         *
         * @param pos      the position to modify
         * @param function the function to apply
         */
        public void modifyBlocks(Selection pos, BlockStateFunction function) {
            modifyBlocks(pos, true, function);
        }

        /**
         * Wrapper for {@link WorldInstructions#modifyBlock(BlockPos, UnaryOperator, boolean)}
         * <p>
         * NOTE: Will probably be removed in the future, exist earlier for backwards compatibility
         */
        public void modifyBlocks(Selection selection, boolean spawnParticles, BlockStateFunction function) {
            modifyBlocks(selection, BlockStateFunction.from(function), spawnParticles);
        }

        /**
         * Wrapper for {@link WorldInstructions#modifyBlock(BlockPos, UnaryOperator, boolean)} with TypeWrapper for {@link UnaryOperator< BlockState >}
         *
         * @param selection      selection
         * @param function       Wrapper function for BlockState
         * @param spawnParticles spawn particles
         */
        public void modifyBlocks(Selection selection, BlockStateFunction function, boolean spawnParticles) {
            modifyBlocks(selection, BlockStateFunction.from(function), spawnParticles);
        }

        /**
         * Wrapper for {@link WorldInstructions#modifyBlock(BlockPos, UnaryOperator, boolean)} with TypeWrapper for {@link UnaryOperator<BlockState>}
         *
         * @param pos            position
         * @param function       Wrapper function for BlockState
         * @param spawnParticles spawn particles
         */
        public void modifyBlock(BlockPos pos, BlockStateFunction function, boolean spawnParticles) {
            modifyBlock(pos, BlockStateFunction.from(function), spawnParticles);
        }

        /**
         * Set blocks with default particle spawning
         *
         * @param selection  selection
         * @param blockState block state
         */
        public void setBlocks(Selection selection, BlockState blockState) {
            setBlocks(selection, true, blockState);
        }

        /**
         * Wrapper for {@link WorldInstructions#setBlocks(Selection, BlockState, boolean)}
         * <p>
         * NOTE: Will probably be removed in the future, exist earlier for backwards compatibility
         */
        public void setBlocks(Selection selection, boolean spawnParticles, BlockState blockState) {
            setBlocks(selection, blockState, spawnParticles);
        }

        public void modifyTileNBT(Selection selection, Consumer<CompoundTag> consumer) {
            modifyTileNBT(selection, BlockEntity.class, consumer, false);
        }

        public void modifyTileNBT(Selection selection, Consumer<CompoundTag> consumer, boolean reDrawBlocks) {
            modifyTileNBT(selection, BlockEntity.class, consumer, reDrawBlocks);
        }

        @Override
        @HideFromJS
        public void modifyBlocks(Selection selection, UnaryOperator<BlockState> stateFunc, boolean spawnParticles) {
            super.modifyBlocks(selection, stateFunc, spawnParticles);
        }

        @Override
        @HideFromJS
        public void modifyBlock(BlockPos pos, UnaryOperator<BlockState> stateFunc, boolean spawnParticles) {
            super.modifyBlock(pos, stateFunc, spawnParticles);
        }
    }

    public class ExtendedSpecialInstructions extends SpecialInstructions {
        @Override
        @HideFromJS
        public void movePointOfInterest(BlockPos location) {
            super.movePointOfInterest(location);
        }
    }
}
