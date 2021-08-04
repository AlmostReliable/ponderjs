package com.kotakotik.pondermaker.crafttweaker.wrappers.implementing;

import com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces.IPonderElement;
import com.simibubi.create.foundation.ponder.PonderElement;

public class PonderElementWrapper implements IPonderElement {
    private final PonderElement internal;

    public PonderElementWrapper(PonderElement internal) {
        this.internal = internal;
    }

    @Override
    public PonderElement getInternal() {
        return internal;
    }
}
