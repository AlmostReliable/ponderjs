package com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import net.minecraft.dispenser.IPosition;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("pondermaker.IBlockPos")
@ZenWrapper(wrappedClass = "net.minecraft.util.math.BlockPos", creationMethodFormat = "new BlockPosWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.BlockPosWrapper")
public interface IBlockPos {
    BlockPos getInternal();

    @OnlyIn(Dist.CLIENT)
    @ZenCodeType.Method
    default String toShortString() {
        return getInternal().toShortString();
    }

    @ZenCodeType.Method
    default long asLong() {
        return getInternal().asLong();
    }

    @ZenCodeType.Method
    default BlockPos offset(double p_177963_1_, double p_177963_3_, double p_177963_5_) {
        return getInternal().offset(p_177963_1_, p_177963_3_, p_177963_5_);
    }

    @ZenCodeType.Method
    default BlockPos offset(int p_177982_1_, int p_177982_2_, int p_177982_3_) {
        return getInternal().offset(p_177982_1_, p_177982_2_, p_177982_3_);
    }

    @ZenCodeType.Method
    default BlockPos subtract(Vector3i p_177973_1_) {
        return getInternal().subtract(p_177973_1_);
    }

    @ZenCodeType.Method
    default BlockPos above() {
        return getInternal().above();
    }

    @ZenCodeType.Method
    default BlockPos above(int p_177981_1_) {
        return getInternal().above(p_177981_1_);
    }

    @ZenCodeType.Method
    default BlockPos below() {
        return getInternal().below();
    }

    @ZenCodeType.Method
    default BlockPos below(int p_177979_1_) {
        return getInternal().below(p_177979_1_);
    }

    @ZenCodeType.Method
    default BlockPos north() {
        return getInternal().north();
    }

    @ZenCodeType.Method
    default BlockPos north(int p_177964_1_) {
        return getInternal().north(p_177964_1_);
    }

    @ZenCodeType.Method
    default BlockPos south() {
        return getInternal().south();
    }

    @ZenCodeType.Method
    default BlockPos south(int p_177970_1_) {
        return getInternal().south(p_177970_1_);
    }

    @ZenCodeType.Method
    default BlockPos west() {
        return getInternal().west();
    }

    @ZenCodeType.Method
    default BlockPos west(int p_177985_1_) {
        return getInternal().west(p_177985_1_);
    }

    @ZenCodeType.Method
    default BlockPos east() {
        return getInternal().east();
    }

    @ZenCodeType.Method
    default BlockPos east(int p_177965_1_) {
        return getInternal().east(p_177965_1_);
    }

    @ZenCodeType.Method
    default BlockPos relative(Direction p_177972_1_) {
        return getInternal().relative(p_177972_1_);
    }

    @ZenCodeType.Method
    default BlockPos relative(Direction p_177967_1_, int p_177967_2_) {
        return getInternal().relative(p_177967_1_, p_177967_2_);
    }

    @ZenCodeType.Method
    default BlockPos relative(Direction.Axis p_241872_1_, int p_241872_2_) {
        return getInternal().relative(p_241872_1_, p_241872_2_);
    }

    @ZenCodeType.Method
    default BlockPos rotate(Rotation p_190942_1_) {
        return getInternal().rotate(p_190942_1_);
    }

    @ZenCodeType.Method
    default BlockPos cross(Vector3i p_177955_1_) {
        return getInternal().cross(p_177955_1_);
    }

    @ZenCodeType.Method
    default BlockPos immutable() {
        return getInternal().immutable();
    }

    @ZenCodeType.Method
    default BlockPos.Mutable mutable() {
        return getInternal().mutable();
    }

    @ZenCodeType.Method
    default int compareTo(Vector3i p_compareTo_1_) {
        return getInternal().compareTo(p_compareTo_1_);
    }

    @ZenCodeType.Method
    default int getX() {
        return getInternal().getX();
    }

    @ZenCodeType.Method
    default int getY() {
        return getInternal().getY();
    }

    @ZenCodeType.Method
    default int getZ() {
        return getInternal().getZ();
    }

    @ZenCodeType.Method
    default boolean closerThan(Vector3i p_218141_1_, double p_218141_2_) {
        return getInternal().closerThan(p_218141_1_, p_218141_2_);
    }

    @ZenCodeType.Method
    default boolean closerThan(IPosition p_218137_1_, double p_218137_2_) {
        return getInternal().closerThan(p_218137_1_, p_218137_2_);
    }

    @ZenCodeType.Method
    default double distSqr(Vector3i p_177951_1_) {
        return getInternal().distSqr(p_177951_1_);
    }

    @ZenCodeType.Method
    default double distSqr(IPosition p_218138_1_, boolean p_218138_2_) {
        return getInternal().distSqr(p_218138_1_, p_218138_2_);
    }

    @ZenCodeType.Method
    default double distSqr(double p_218140_1_, double p_218140_3_, double p_218140_5_, boolean p_218140_7_) {
        return getInternal().distSqr(p_218140_1_, p_218140_3_, p_218140_5_, p_218140_7_);
    }

    @ZenCodeType.Method
    default int distManhattan(Vector3i p_218139_1_) {
        return getInternal().distManhattan(p_218139_1_);
    }

    @ZenCodeType.Method
    default int get(Direction.Axis p_243648_1_) {
        return getInternal().get(p_243648_1_);
    }
}
