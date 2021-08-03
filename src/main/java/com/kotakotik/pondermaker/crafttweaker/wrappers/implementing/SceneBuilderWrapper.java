package com.kotakotik.pondermaker.crafttweaker.wrappers.implementing;

import com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces.ISceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuilder;

public class SceneBuilderWrapper implements ISceneBuilder {
    private final SceneBuilder internal;

    public SceneBuilderWrapper(SceneBuilder internal) {
        this.internal = internal;
    }

    @Override
    public SceneBuilder getInternal() {
        return internal;
    }
}
