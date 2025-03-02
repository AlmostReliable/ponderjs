package com.almostreliable.ponderjs.extension;

import com.almostreliable.ponderjs.api.CustomPonderSceneElement;
import com.almostreliable.ponderjs.util.BlockStateFunction;
import com.almostreliable.ponderjs.util.PonderPlatform;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.api.scene.WorldInstructions;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import net.createmod.ponder.foundation.instruction.FadeInOutInstruction;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@RemapPrefixForJS("ponderjs$")
public interface WorldInstructionExtension {

    @HideFromJS
    PonderSceneBuilder ponderjs$builder();

    default CustomPonderSceneElement ponderjs$addElement(int ticks) {
        var element = new CustomPonderSceneElement();
        element.setVisible(false);
        ponderjs$builder().addInstruction(new FadeInOutInstruction(ticks) {
            @Override
            protected void show(PonderScene scene) {
                scene.addElement(element);
                element.setVisible(true);
            }

            @Override
            protected void hide(PonderScene scene) {
                element.setVisible(false);
            }

            @Override
            protected void applyFade(PonderScene scene, float fade) {
                element.setFade(fade);
            }
        });

        return element;
    }

    default CustomPonderSceneElement ponderjs$addElement() {
        var element = new CustomPonderSceneElement();
        ponderjs$builder().addInstruction(ponderScene -> {
            ponderScene.addElement(element);
            element.setVisible(true);
        });

        return element;
    }

    /**
     * Create a new entity with some default behavior. The entity will be rotated to face north.
     *
     * @param entityType The type of entity to create.
     * @param position   The position to create the entity at.
     * @param consumer   Callback to modify the entity.
     * @return An entity link which can be used later on.
     */
    default ElementLink<EntityElement> ponderjs$createEntity(EntityType<?> entityType, Vec3 position, Consumer<Entity> consumer) {
        return ponderjs$builder().world().createEntity(level -> {
            Entity entity = entityType.create(level);
            Objects.requireNonNull(entity, "Could not create entity of type " +
                                           PonderPlatform.getEntityTypeName(entityType));
            entity.setPosRaw(position.x, position.y, position.z);
            entity.setOldPosAndRot();
            entity.lookAt(EntityAnchorArgument.Anchor.FEET, position.add(0, 0, -1));
            consumer.accept(entity);
            return entity;
        });
    }

    default ElementLink<EntityElement> ponderjs$createEntity(EntityType<?> entityType, Vec3 position) {
        return ponderjs$createEntity(entityType, position, entity -> {
        });
    }

    /**
     * Short version for modify blocks with default spawn particles.
     *
     * @param pos      the position to modify
     * @param function the function to apply
     */
    default void ponderjs$modifyBlocks(Selection pos, BlockStateFunction function) {
        ponderjs$modifyBlocks(pos, true, function);
    }

    /**
     * Wrapper for {@link WorldInstructions#modifyBlock(BlockPos, UnaryOperator, boolean)}
     * <p>
     * NOTE: Will probably be removed in the future, exist earlier for backwards compatibility
     */
    default void ponderjs$modifyBlocks(Selection selection, boolean spawnParticles, BlockStateFunction function) {
        ponderjs$builder().world().modifyBlocks(selection, BlockStateFunction.from(function), spawnParticles);
    }

    /**
     * Wrapper for {@link WorldInstructions#modifyBlock(BlockPos, UnaryOperator, boolean)} with TypeWrapper for {@link UnaryOperator< BlockState >}
     *
     * @param selection      selection
     * @param function       Wrapper function for BlockState
     * @param spawnParticles spawn particles
     */
    default void ponderjs$modifyBlocks(Selection selection, BlockStateFunction function, boolean spawnParticles) {
        ponderjs$builder().world().modifyBlocks(selection, BlockStateFunction.from(function), spawnParticles);
    }

    /**
     * Wrapper for {@link WorldInstructions#modifyBlock(BlockPos, UnaryOperator, boolean)} with TypeWrapper for {@link UnaryOperator<BlockState>}
     *
     * @param pos            position
     * @param function       Wrapper function for BlockState
     * @param spawnParticles spawn particles
     */
    default void ponderjs$modifyBlock(BlockPos pos, BlockStateFunction function, boolean spawnParticles) {
        ponderjs$builder().world().modifyBlock(pos, BlockStateFunction.from(function), spawnParticles);
    }

    /**
     * Set blocks with default particle spawning
     *
     * @param selection  selection
     * @param blockState block state
     */
    default void ponderjs$setBlocks(Selection selection, BlockState blockState) {
        ponderjs$setBlocks(selection, true, blockState);
    }

    /**
     * Wrapper for {@link WorldInstructions#setBlocks(Selection, BlockState, boolean)}
     * <p>
     * NOTE: Will probably be removed in the future, exist earlier for backwards compatibility
     */
    default void ponderjs$setBlocks(Selection selection, boolean spawnParticles, BlockState blockState) {
        ponderjs$builder().world().setBlocks(selection, blockState, spawnParticles);
    }


    default void ponderjs$modifyBlockEntityNBT(Selection selection, Consumer<CompoundTag> consumer) {
        ponderjs$modifyBlockEntityNBT(selection, false, consumer);
    }

    default void ponderjs$modifyBlockEntityNBT(Selection selection, boolean reDrawBlocks, Consumer<CompoundTag> consumer) {
        ponderjs$builder().world().modifyBlockEntityNBT(selection, BlockEntity.class, consumer, reDrawBlocks);
    }

    default void ponderjs$removeEntity(ElementLink<EntityElement> link) {
        ponderjs$builder().addInstruction(scene -> {
            var resolve = scene.resolve(link);
            if (resolve != null) {
                resolve.ifPresent(Entity::discard);
            }
        });
    }
}
