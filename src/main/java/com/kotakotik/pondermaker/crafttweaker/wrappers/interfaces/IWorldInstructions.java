package com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import com.simibubi.create.content.logistics.block.mechanicalArm.ArmTileEntity;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.elements.BeltItemElement;
import com.simibubi.create.foundation.ponder.elements.EntityElement;
import com.simibubi.create.foundation.ponder.elements.WorldSectionElement;
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
import org.openzen.zencode.java.ZenCodeType;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@ZenRegister
@ZenCodeType.Name("pondermaker.ISceneBuilder")
@ZenWrapper(wrappedClass = "com.simibubi.create.foundation.ponder.SceneBuilder.WorldInstructions", creationMethodFormat = "new WorldInstructionsWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.WorldInstructionsWrapper")
public interface IWorldInstructions {
    // thank jetbrains for the delegated method generation
    SceneBuilder.WorldInstructions getInternal();

    @ZenCodeType.Method
    default void incrementBlockBreakingProgress(BlockPos pos) {
        getInternal().incrementBlockBreakingProgress(pos);
    }

    @ZenCodeType.Method
    default void showSection(Selection selection, Direction fadeInDirection) {
        getInternal().showSection(selection, fadeInDirection);
    }

    @ZenCodeType.Method
    default void showSectionAndMerge(Selection selection, Direction fadeInDirection, ElementLink<WorldSectionElement> link) {
        getInternal().showSectionAndMerge(selection, fadeInDirection, link);
    }

    @ZenCodeType.Method
    default void glueBlockOnto(BlockPos position, Direction fadeInDirection, ElementLink<WorldSectionElement> link) {
        getInternal().glueBlockOnto(position, fadeInDirection, link);
    }

    @ZenCodeType.Method
    default ElementLink<WorldSectionElement> showIndependentSection(Selection selection, Direction fadeInDirection) {
        return getInternal().showIndependentSection(selection, fadeInDirection);
    }

    @ZenCodeType.Method
    default ElementLink<WorldSectionElement> showIndependentSectionImmediately(Selection selection) {
        return getInternal().showIndependentSectionImmediately(selection);
    }

    @ZenCodeType.Method
    default void hideSection(Selection selection, Direction fadeOutDirection) {
        getInternal().hideSection(selection, fadeOutDirection);
    }

    @ZenCodeType.Method
    default void hideIndependentSection(ElementLink<WorldSectionElement> link, Direction fadeOutDirection) {
        getInternal().hideIndependentSection(link, fadeOutDirection);
    }

    @ZenCodeType.Method
    default void restoreBlocks(Selection selection) {
        getInternal().restoreBlocks(selection);
    }

    @ZenCodeType.Method
    default ElementLink<WorldSectionElement> makeSectionIndependent(Selection selection) {
        return getInternal().makeSectionIndependent(selection);
    }

    @ZenCodeType.Method
    default void rotateSection(ElementLink<WorldSectionElement> link, double xRotation, double yRotation, double zRotation, int duration) {
        getInternal().rotateSection(link, xRotation, yRotation, zRotation, duration);
    }

    @ZenCodeType.Method
    default void configureCenterOfRotation(ElementLink<WorldSectionElement> link, Vector3d anchor) {
        getInternal().configureCenterOfRotation(link, anchor);
    }

    @ZenCodeType.Method
    default void configureStabilization(ElementLink<WorldSectionElement> link, Vector3d anchor) {
        getInternal().configureStabilization(link, anchor);
    }

    @ZenCodeType.Method
    default void moveSection(ElementLink<WorldSectionElement> link, Vector3d offset, int duration) {
        getInternal().moveSection(link, offset, duration);
    }

    @ZenCodeType.Method
    default void rotateBearing(BlockPos pos, float angle, int duration) {
        getInternal().rotateBearing(pos, angle, duration);
    }

    @ZenCodeType.Method
    default void movePulley(BlockPos pos, float distance, int duration) {
        getInternal().movePulley(pos, distance, duration);
    }

    @ZenCodeType.Method
    default void moveDeployer(BlockPos pos, float distance, int duration) {
        getInternal().moveDeployer(pos, distance, duration);
    }

    @ZenCodeType.Method
    default void setBlocks(Selection selection, BlockState state, boolean spawnParticles) {
        getInternal().setBlocks(selection, state, spawnParticles);
    }

    @ZenCodeType.Method
    default void destroyBlock(BlockPos pos) {
        getInternal().destroyBlock(pos);
    }

    @ZenCodeType.Method
    default void setBlock(BlockPos pos, BlockState state, boolean spawnParticles) {
        getInternal().setBlock(pos, state, spawnParticles);
    }

    @ZenCodeType.Method
    default void replaceBlocks(Selection selection, BlockState state, boolean spawnParticles) {
        getInternal().replaceBlocks(selection, state, spawnParticles);
    }

    @ZenCodeType.Method
    default void modifyBlock(BlockPos pos, UnaryOperator<BlockState> stateFunc, boolean spawnParticles) {
        getInternal().modifyBlock(pos, stateFunc, spawnParticles);
    }

    @ZenCodeType.Method
    default void cycleBlockProperty(BlockPos pos, Property<?> property) {
        getInternal().cycleBlockProperty(pos, property);
    }

    @ZenCodeType.Method
    default void modifyBlocks(Selection selection, UnaryOperator<BlockState> stateFunc, boolean spawnParticles) {
        getInternal().modifyBlocks(selection, stateFunc, spawnParticles);
    }

    @ZenCodeType.Method
    default void toggleRedstonePower(Selection selection) {
        getInternal().toggleRedstonePower(selection);
    }

    @ZenCodeType.Method
    default <T extends Entity> void modifyEntities(Class<T> entityClass, Consumer<T> entityCallBack) {
        getInternal().modifyEntities(entityClass, entityCallBack);
    }

    @ZenCodeType.Method
    default <T extends Entity> void modifyEntitiesInside(Class<T> entityClass, Selection area, Consumer<T> entityCallBack) {
        getInternal().modifyEntitiesInside(entityClass, area, entityCallBack);
    }

    @ZenCodeType.Method
    default void modifyEntity(ElementLink<EntityElement> link, Consumer<Entity> entityCallBack) {
        getInternal().modifyEntity(link, entityCallBack);
    }

    @ZenCodeType.Method
    default ElementLink<EntityElement> createEntity(Function<World, Entity> factory) {
        return getInternal().createEntity(factory);
    }

    @ZenCodeType.Method
    default ElementLink<EntityElement> createItemEntity(Vector3d location, Vector3d motion, ItemStack stack) {
        return getInternal().createItemEntity(location, motion, stack);
    }

    @ZenCodeType.Method
    default ElementLink<EntityElement> createGlueEntity(BlockPos pos, Direction face) {
        return getInternal().createGlueEntity(pos, face);
    }

    @ZenCodeType.Method
    default void createItemOnBeltLike(BlockPos location, Direction insertionSide, ItemStack stack) {
        getInternal().createItemOnBeltLike(location, insertionSide, stack);
    }

    @ZenCodeType.Method
    default ElementLink<BeltItemElement> createItemOnBelt(BlockPos beltLocation, Direction insertionSide, ItemStack stack) {
        return getInternal().createItemOnBelt(beltLocation, insertionSide, stack);
    }

    @ZenCodeType.Method
    default void removeItemsFromBelt(BlockPos beltLocation) {
        getInternal().removeItemsFromBelt(beltLocation);
    }

    @ZenCodeType.Method
    default void stallBeltItem(ElementLink<BeltItemElement> link, boolean stalled) {
        getInternal().stallBeltItem(link, stalled);
    }

    @ZenCodeType.Method
    default void changeBeltItemTo(ElementLink<BeltItemElement> link, ItemStack newStack) {
        getInternal().changeBeltItemTo(link, newStack);
    }

    @ZenCodeType.Method
    default void setKineticSpeed(Selection selection, float speed) {
        getInternal().setKineticSpeed(selection, speed);
    }

    @ZenCodeType.Method
    default void multiplyKineticSpeed(Selection selection, float modifier) {
        getInternal().multiplyKineticSpeed(selection, modifier);
    }

    @ZenCodeType.Method
    default void modifyKineticSpeed(Selection selection, UnaryOperator<Float> speedFunc) {
        getInternal().modifyKineticSpeed(selection, speedFunc);
    }

    @ZenCodeType.Method
    default void propagatePipeChange(BlockPos pos) {
        getInternal().propagatePipeChange(pos);
    }

    @ZenCodeType.Method
    default void setFilterData(Selection selection, Class<? extends TileEntity> teType, ItemStack filter) {
        getInternal().setFilterData(selection, teType, filter);
    }

    @ZenCodeType.Method
    default void modifyTileNBT(Selection selection, Class<? extends TileEntity> teType, Consumer<CompoundNBT> consumer) {
        getInternal().modifyTileNBT(selection, teType, consumer);
    }

    @ZenCodeType.Method
    default <T extends TileEntity> void modifyTileEntity(BlockPos position, Class<T> teType, Consumer<T> consumer) {
        getInternal().modifyTileEntity(position, teType, consumer);
    }

    @ZenCodeType.Method
    default void modifyTileNBT(Selection selection, Class<? extends TileEntity> teType, Consumer<CompoundNBT> consumer, boolean reDrawBlocks) {
        getInternal().modifyTileNBT(selection, teType, consumer, reDrawBlocks);
    }

    @ZenCodeType.Method
    default void instructArm(BlockPos armLocation, ArmTileEntity.Phase phase, ItemStack heldItem, int targetedPoint) {
        getInternal().instructArm(armLocation, phase, heldItem, targetedPoint);
    }

    @ZenCodeType.Method
    default void flapFunnel(BlockPos position, boolean outward) {
        getInternal().flapFunnel(position, outward);
    }

    @ZenCodeType.Method
    default void setCraftingResult(BlockPos crafter, ItemStack output) {
        getInternal().setCraftingResult(crafter, output);
    }

    @ZenCodeType.Method
    default void connectCrafterInvs(BlockPos position1, BlockPos position2) {
        getInternal().connectCrafterInvs(position1, position2);
    }
}
