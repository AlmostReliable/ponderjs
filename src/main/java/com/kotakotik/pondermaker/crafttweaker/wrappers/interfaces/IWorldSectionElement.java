package com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.elements.WorldSectionElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("pondermaker.IWorldSelectionElement")
@ZenWrapper(wrappedClass = "com.simibubi.create.foundation.ponder.elements.WorldSelectionElement", creationMethodFormat = "new WorldSelectionElementWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.WorldSelectionElementWrapper")
public interface IWorldSectionElement {
    WorldSectionElement getInternal();

    @ZenCodeType.Method
    default void mergeOnto(WorldSectionElement other) {
        getInternal().mergeOnto(other);
    }

    @ZenCodeType.Method
    default void set(Selection selection) {
        getInternal().set(selection);
    }

    @ZenCodeType.Method
    default void add(Selection toAdd) {
        getInternal().add(toAdd);
    }

    @ZenCodeType.Method
    default void erase(Selection toErase) {
        getInternal().erase(toErase);
    }

    @ZenCodeType.Method
    default void setCenterOfRotation(Vector3d center) {
        getInternal().setCenterOfRotation(center);
    }

    @ZenCodeType.Method
    default void stabilizeRotation(Vector3d anchor) {
        getInternal().stabilizeRotation(anchor);
    }

    @ZenCodeType.Method
    default void reset(PonderScene scene) {
        getInternal().reset(scene);
    }

    @ZenCodeType.Method
    default void selectBlock(BlockPos pos) {
        getInternal().selectBlock(pos);
    }

    @ZenCodeType.Method
    default void resetSelectedBlock() {
        getInternal().resetSelectedBlock();
    }

    @ZenCodeType.Method
    default void resetAnimatedTransform() {
        getInternal().resetAnimatedTransform();
    }

    @ZenCodeType.Method
    default boolean isEmpty() {
        return getInternal().isEmpty();
    }

    @ZenCodeType.Method
    default void setEmpty() {
        getInternal().setEmpty();
    }

    @ZenCodeType.Method
    default boolean isVisible() {
        return getInternal().isVisible();
    }

    @ZenCodeType.Method
    default void forceApplyFade(float fade) {
        getInternal().forceApplyFade(fade);
    }

    @ZenCodeType.Method
    default void setFade(float fade) {
        getInternal().setFade(fade);
    }

    @ZenCodeType.Method
    default void setVisible(boolean visible) {
        getInternal().setVisible(visible);
    }
}
