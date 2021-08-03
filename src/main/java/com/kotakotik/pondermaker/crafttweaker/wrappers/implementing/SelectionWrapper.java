package com.kotakotik.pondermaker.crafttweaker.wrappers.implementing;

import com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces.ISelection;
import com.simibubi.create.foundation.ponder.Selection;

public class SelectionWrapper implements ISelection {
    private final Selection internal;

    public SelectionWrapper(Selection internal) {
        this.internal = internal;
    }

    @Override
    public Selection getInternal() {
        return internal;
    }
}
