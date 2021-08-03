package com.kotakotik.pondermaker.crafttweaker.wrappers.implementing;

import com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces.IWorldInstructions;
import com.simibubi.create.foundation.ponder.SceneBuilder;

public class WorldInstructionsWrapper implements IWorldInstructions {
    private final SceneBuilder.WorldInstructions internal;

    public WorldInstructionsWrapper(SceneBuilder.WorldInstructions internal) {
        this.internal = internal;
    }

    @Override
    public SceneBuilder.WorldInstructions getInternal() {
        return internal;
    }
}
