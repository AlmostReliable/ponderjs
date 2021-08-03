package com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.utility.outliner.Outline;
import com.simibubi.create.foundation.utility.outliner.Outliner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.openzen.zencode.java.ZenCodeType;

import java.util.function.Consumer;

@ZenRegister
@ZenCodeType.Name("pondermaker.ISelection")
@ZenWrapper(wrappedClass = "com.simibubi.create.foundation.ponder.Selection", creationMethodFormat = "new SelectionWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.SelectionWrapper")
public interface ISelection {
    Selection getInternal();

    @ZenCodeType.Method
    default Selection add(Selection selection) {
        return getInternal().add(selection);
    }

    @ZenCodeType.Method
    default Selection substract(Selection selection) {
        return getInternal().substract(selection);
    }

    @ZenCodeType.Method
    default Selection copy() {
        return getInternal().copy();
    }

    @ZenCodeType.Method
    default Vector3d getCenter() {
        return getInternal().getCenter();
    }

    @ZenCodeType.Method
    default void forEach(Consumer<BlockPos> consumer) {
        getInternal().forEach(consumer);
    }

    @ZenCodeType.Method
    default Outline.OutlineParams makeOutline(Outliner outliner, Object o) {
        return getInternal().makeOutline(outliner, o);
    }

    @ZenCodeType.Method
    default Outline.OutlineParams makeOutline(Outliner outliner) {
        return getInternal().makeOutline(outliner);
    }
}
