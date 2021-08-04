package com.kotakotik.pondermaker.crafttweaker.wrappers.implementing;

import com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces.IElementLink;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderElement;

public class ElementLinkWrapper<T extends PonderElement> implements IElementLink<T> {
    private final ElementLink<T> internal;

    public ElementLinkWrapper(ElementLink<T> internal) {
        this.internal = internal;
    }

    @Override
    public ElementLink<T> getInternal() {
        return internal;
    }
}
