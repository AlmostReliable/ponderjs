package com.kotakotik.pondermaker.crafttweaker.wrappers.implementing;

import com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces.IInputWindowElement;
import com.simibubi.create.foundation.ponder.elements.InputWindowElement;

public class InputWindowElementWrapper implements IInputWindowElement {
    private final InputWindowElement internal;

    public InputWindowElementWrapper(InputWindowElement internal) {
        this.internal = internal;
    }

    @Override
    public InputWindowElement getInternal() {
        return internal;
    }
}
