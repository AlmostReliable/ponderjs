package com.kotakotik.pondermaker.crafttweaker.wrappers.implementing;

import com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces.IOverlayInstructions;
import com.simibubi.create.foundation.ponder.SceneBuilder;

public class OverlayInstructionsWrapper implements IOverlayInstructions {
    private final SceneBuilder.OverlayInstructions internal;

    public OverlayInstructionsWrapper(SceneBuilder.OverlayInstructions internal) {
        this.internal = internal;
    }

    @Override
    public SceneBuilder.OverlayInstructions getInternal() {
        return internal;
    }
}
