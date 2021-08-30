package com.kotakotik.ponderjs.kubejs.util;

import com.simibubi.create.content.logistics.block.mechanicalArm.ArmTileEntity;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.elements.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.Property;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Boilerplate code for {@link SceneBuilderJS}
 */
public interface ISceneBuilderJS {
    SceneBuilder getInternal();

    default void title(String sceneId, String title) {
        getInternal().title(sceneId, title);
    }

    default void configureBasePlate(int xOffset, int zOffset, int basePlateSize) {
        getInternal().configureBasePlate(xOffset, zOffset, basePlateSize);
    }

    default void scaleSceneView(float factor) {
        getInternal().scaleSceneView(factor);
    }

    default void setSceneOffsetY(float yOffset) {
        getInternal().setSceneOffsetY(yOffset);
    }

    default void showBasePlate() {
        getInternal().showBasePlate();
    }

    default void idle(int ticks) {
        getInternal().idle(ticks);
    }

    default void idleSeconds(int seconds) {
        getInternal().idleSeconds(seconds);
    }

    default void markAsFinished() {
        getInternal().markAsFinished();
    }

    default void rotateCameraY(float degrees) {
        getInternal().rotateCameraY(degrees);
    }

    default void addKeyframe() {
        getInternal().addKeyframe();
    }

    default void addLazyKeyframe() {
        getInternal().addLazyKeyframe();
    }

    interface ISpecialInstructionsJS {
        SceneBuilder.SpecialInstructions getInternal();

        default ElementLink<ParrotElement> birbOnTurntable(BlockPos pos) {
            return getInternal().birbOnTurntable(pos);
        }

        default ElementLink<ParrotElement> birbOnSpinnyShaft(BlockPos pos) {
            return getInternal().birbOnSpinnyShaft(pos);
        }

        default ElementLink<ParrotElement> createBirb(Vector3d location, Supplier<? extends ParrotElement.ParrotPose> pose) {
            return getInternal().createBirb(location, pose);
        }

        default void changeBirbPose(ElementLink<ParrotElement> birb, Supplier<? extends ParrotElement.ParrotPose> pose) {
            getInternal().changeBirbPose(birb, pose);
        }

        default void movePointOfInterest(Vector3d location) {
            getInternal().movePointOfInterest(location);
        }

        default void rotateParrot(ElementLink<ParrotElement> link, double xRotation, double yRotation, double zRotation, int duration) {
            getInternal().rotateParrot(link, xRotation, yRotation, zRotation, duration);
        }

        default void moveParrot(ElementLink<ParrotElement> link, Vector3d offset, int duration) {
            getInternal().moveParrot(link, offset, duration);
        }

        default ElementLink<MinecartElement> createCart(Vector3d location, float angle, MinecartElement.MinecartConstructor type) {
            return getInternal().createCart(location, angle, type);
        }

        default void rotateCart(ElementLink<MinecartElement> link, float yRotation, int duration) {
            getInternal().rotateCart(link, yRotation, duration);
        }

        default void moveCart(ElementLink<MinecartElement> link, Vector3d offset, int duration) {
            getInternal().moveCart(link, offset, duration);
        }

        default <T extends AnimatedSceneElement> void hideElement(ElementLink<T> link, Direction direction) {
            getInternal().hideElement(link, direction);
        }
    }

    /**
     * Boilerplate code for {@link SceneBuilderJS.WorldInstructionsJS}
     */
    interface IWorldInstructionsJS {
        SceneBuilder.WorldInstructions getInternal();

        default void incrementBlockBreakingProgress(BlockPos pos) {
            getInternal().incrementBlockBreakingProgress(pos);
        }

        default void showSection(Selection selection, Direction fadeInDirection) {
            getInternal().showSection(selection, fadeInDirection);
        }

        default void showSectionAndMerge(Selection selection, Direction fadeInDirection, ElementLink<WorldSectionElement> link) {
            getInternal().showSectionAndMerge(selection, fadeInDirection, link);
        }

        default void glueBlockOnto(BlockPos position, Direction fadeInDirection, ElementLink<WorldSectionElement> link) {
            getInternal().glueBlockOnto(position, fadeInDirection, link);
        }

        default ElementLink<WorldSectionElement> showIndependentSection(Selection selection, Direction fadeInDirection) {
            return getInternal().showIndependentSection(selection, fadeInDirection);
        }

        default ElementLink<WorldSectionElement> showIndependentSectionImmediately(Selection selection) {
            return getInternal().showIndependentSectionImmediately(selection);
        }

        default void hideSection(Selection selection, Direction fadeOutDirection) {
            getInternal().hideSection(selection, fadeOutDirection);
        }

        default void hideIndependentSection(ElementLink<WorldSectionElement> link, Direction fadeOutDirection) {
            getInternal().hideIndependentSection(link, fadeOutDirection);
        }

        default void restoreBlocks(Selection selection) {
            getInternal().restoreBlocks(selection);
        }

        default ElementLink<WorldSectionElement> makeSectionIndependent(Selection selection) {
            return getInternal().makeSectionIndependent(selection);
        }

        default void rotateSection(ElementLink<WorldSectionElement> link, double xRotation, double yRotation, double zRotation, int duration) {
            getInternal().rotateSection(link, xRotation, yRotation, zRotation, duration);
        }

        default void configureCenterOfRotation(ElementLink<WorldSectionElement> link, Vector3d anchor) {
            getInternal().configureCenterOfRotation(link, anchor);
        }

        default void configureStabilization(ElementLink<WorldSectionElement> link, Vector3d anchor) {
            getInternal().configureStabilization(link, anchor);
        }

        default void moveSection(ElementLink<WorldSectionElement> link, Vector3d offset, int duration) {
            getInternal().moveSection(link, offset, duration);
        }

        default void rotateBearing(BlockPos pos, float angle, int duration) {
            getInternal().rotateBearing(pos, angle, duration);
        }

        default void movePulley(BlockPos pos, float distance, int duration) {
            getInternal().movePulley(pos, distance, duration);
        }

        default void moveDeployer(BlockPos pos, float distance, int duration) {
            getInternal().moveDeployer(pos, distance, duration);
        }

        default void setBlocks(Selection selection, BlockState state, boolean spawnParticles) {
            getInternal().setBlocks(selection, state, spawnParticles);
        }

        default void destroyBlock(BlockPos pos) {
            getInternal().destroyBlock(pos);
        }

        default void setBlock(BlockPos pos, BlockState state, boolean spawnParticles) {
            getInternal().setBlock(pos, state, spawnParticles);
        }

        default void replaceBlocks(Selection selection, BlockState state, boolean spawnParticles) {
            getInternal().replaceBlocks(selection, state, spawnParticles);
        }

        default void modifyBlock(BlockPos pos, UnaryOperator<BlockState> stateFunc, boolean spawnParticles) {
            getInternal().modifyBlock(pos, stateFunc, spawnParticles);
        }

        default void cycleBlockProperty(BlockPos pos, Property<?> property) {
            getInternal().cycleBlockProperty(pos, property);
        }

        default void modifyBlocks(Selection selection, UnaryOperator<BlockState> stateFunc, boolean spawnParticles) {
            getInternal().modifyBlocks(selection, stateFunc, spawnParticles);
        }

        default void toggleRedstonePower(Selection selection) {
            getInternal().toggleRedstonePower(selection);
        }

        default <T extends Entity> void modifyEntities(Class<T> entityClass, Consumer<T> entityCallBack) {
            getInternal().modifyEntities(entityClass, entityCallBack);
        }

        default <T extends Entity> void modifyEntitiesInside(Class<T> entityClass, Selection area, Consumer<T> entityCallBack) {
            getInternal().modifyEntitiesInside(entityClass, area, entityCallBack);
        }

//        default void modifyEntity(ElementLink<EntityElement> link, Consumer<Entity> entityCallBack) {
//            getInternal().modifyEntity(link, entityCallBack);
//        }

        default ElementLink<EntityElement> createEntity(Function<World, Entity> factory) {
            return getInternal().createEntity(factory);
        }

        default ElementLink<EntityElement> createItemEntity(Vector3d location, Vector3d motion, ItemStack stack) {
            return getInternal().createItemEntity(location, motion, stack);
        }

        default ElementLink<EntityElement> createGlueEntity(BlockPos pos, Direction face) {
            return getInternal().createGlueEntity(pos, face);
        }

        default void createItemOnBeltLike(BlockPos location, Direction insertionSide, ItemStack stack) {
            getInternal().createItemOnBeltLike(location, insertionSide, stack);
        }

        default ElementLink<BeltItemElement> createItemOnBelt(BlockPos beltLocation, Direction insertionSide, ItemStack stack) {
            return getInternal().createItemOnBelt(beltLocation, insertionSide, stack);
        }

        default void removeItemsFromBelt(BlockPos beltLocation) {
            getInternal().removeItemsFromBelt(beltLocation);
        }

        default void stallBeltItem(ElementLink<BeltItemElement> link, boolean stalled) {
            getInternal().stallBeltItem(link, stalled);
        }

        default void changeBeltItemTo(ElementLink<BeltItemElement> link, ItemStack newStack) {
            getInternal().changeBeltItemTo(link, newStack);
        }

        default void setKineticSpeed(Selection selection, float speed) {
            getInternal().setKineticSpeed(selection, speed);
        }

        default void multiplyKineticSpeed(Selection selection, float modifier) {
            getInternal().multiplyKineticSpeed(selection, modifier);
        }

        default void modifyKineticSpeed(Selection selection, UnaryOperator<Float> speedFunc) {
            getInternal().modifyKineticSpeed(selection, speedFunc);
        }

        default void propagatePipeChange(BlockPos pos) {
            getInternal().propagatePipeChange(pos);
        }

        default void setFilterData(Selection selection, Class<? extends TileEntity> teType, ItemStack filter) {
            getInternal().setFilterData(selection, teType, filter);
        }

        default void modifyTileNBT(Selection selection, Class<? extends TileEntity> teType, Consumer<CompoundNBT> consumer) {
            getInternal().modifyTileNBT(selection, teType, consumer);
        }

        default <T extends TileEntity> void modifyTileEntity(BlockPos position, Class<T> teType, Consumer<T> consumer) {
            getInternal().modifyTileEntity(position, teType, consumer);
        }

        default void modifyTileNBT(Selection selection, Class<? extends TileEntity> teType, Consumer<CompoundNBT> consumer, boolean reDrawBlocks) {
            getInternal().modifyTileNBT(selection, teType, consumer, reDrawBlocks);
        }

        default void instructArm(BlockPos armLocation, ArmTileEntity.Phase phase, ItemStack heldItem, int targetedPoint) {
            getInternal().instructArm(armLocation, phase, heldItem, targetedPoint);
        }

        default void flapFunnel(BlockPos position, boolean outward) {
            getInternal().flapFunnel(position, outward);
        }

        default void setCraftingResult(BlockPos crafter, ItemStack output) {
            getInternal().setCraftingResult(crafter, output);
        }

        default void connectCrafterInvs(BlockPos position1, BlockPos position2) {
            getInternal().connectCrafterInvs(position1, position2);
        }
    }

    SceneBuilder.OverlayInstructions getOverlay();
    default SceneBuilder.OverlayInstructions overlay() {
        return getOverlay();
    }
    SceneBuilderJS.WorldInstructionsJS getWorld();
    default SceneBuilderJS.WorldInstructionsJS world() {
        return getWorld();
    }
    SceneBuilder.DebugInstructions getDebug();
    default SceneBuilder.DebugInstructions debug() {
        return getDebug();
    }
    SceneBuilder.EffectInstructions getEffects();
    default SceneBuilder.EffectInstructions effects() {
        return getEffects();
    }
    SceneBuilderJS.SpecialInstructionsJS getSpecial();
    default SceneBuilderJS.SpecialInstructionsJS special() {
        return getSpecial();
    }
}
