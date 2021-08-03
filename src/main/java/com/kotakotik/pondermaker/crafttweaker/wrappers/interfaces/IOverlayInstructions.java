package com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.simibubi.create.foundation.ponder.elements.InputWindowElement;
import com.simibubi.create.foundation.ponder.elements.TextWindowElement;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("pondermaker.IOverlayInstructions")
@ZenWrapper(wrappedClass = "com.simibubi.create.foundation.ponder.SceneBuilder.OverlayInstructions", creationMethodFormat = "new OverlayInstructionsWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.OverlayInstructionsWrapper")
public interface IOverlayInstructions {
    SceneBuilder.OverlayInstructions getInternal();

    @ZenCodeType.Method
    default TextWindowElement.Builder showText(int duration) {
        return getInternal().showText(duration);
    }

    @ZenCodeType.Method
    default TextWindowElement.Builder showSelectionWithText(Selection selection, int duration) {
        return getInternal().showSelectionWithText(selection, duration);
    }

    @ZenCodeType.Method
    default void showControls(InputWindowElement element, int duration) {
        getInternal().showControls(element, duration);
    }

    @ZenCodeType.Method
    default void chaseBoundingBoxOutline(PonderPalette color, Object slot, AxisAlignedBB boundingBox, int duration) {
        getInternal().chaseBoundingBoxOutline(color, slot, boundingBox, duration);
    }

    @ZenCodeType.Method
    default void showCenteredScrollInput(BlockPos pos, Direction side, int duration) {
        getInternal().showCenteredScrollInput(pos, side, duration);
    }

    @ZenCodeType.Method
    default void showScrollInput(Vector3d location, Direction side, int duration) {
        getInternal().showScrollInput(location, side, duration);
    }

    @ZenCodeType.Method
    default void showRepeaterScrollInput(BlockPos pos, int duration) {
        getInternal().showRepeaterScrollInput(pos, duration);
    }

    @ZenCodeType.Method
    default void showFilterSlotInput(Vector3d location, int duration) {
        getInternal().showFilterSlotInput(location, duration);
    }

    @ZenCodeType.Method
    default void showLine(PonderPalette color, Vector3d start, Vector3d end, int duration) {
        getInternal().showLine(color, start, end, duration);
    }

    @ZenCodeType.Method
    default void showOutline(PonderPalette color, Object slot, Selection selection, int duration) {
        getInternal().showOutline(color, slot, selection, duration);
    }
}
