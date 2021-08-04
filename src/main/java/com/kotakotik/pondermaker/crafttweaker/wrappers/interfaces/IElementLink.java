package com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderElement;
import org.openzen.zencode.java.ZenCodeType;

import java.util.UUID;

@ZenRegister
@ZenCodeType.Name("pondermaker.IElementLink")
@ZenWrapper(wrappedClass = "com.simibubi.create.foundation.ponder.ElementLink", creationMethodFormat = "new ElementLinkWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.ElementLinkWrapper")
public interface IElementLink<T extends PonderElement> {
    ElementLink<T> getInternal();

    @ZenCodeType.Method
    default UUID getId() {
        return getInternal().getId();
    }

    @ZenCodeType.Method
    default T cast(PonderElement e) {
        return getInternal().cast(e);
    }
}
