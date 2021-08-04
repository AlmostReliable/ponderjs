package com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import com.simibubi.create.foundation.ponder.PonderElement;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("pondermaker.IPonderElement")
@ZenWrapper(wrappedClass = "com.simibubi.create.foundation.ponder.PonderElement", creationMethodFormat = "new PonderElementWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.PonderElementWrapper")
public interface IPonderElement {
    PonderElement getInternal();

    @ZenCodeType.Method
    default boolean isVisible() {
        return getInternal().isVisible();
    }

    @ZenCodeType.Method
    default void setVisible(boolean visible) {
        getInternal().setVisible(visible);
    }
}
