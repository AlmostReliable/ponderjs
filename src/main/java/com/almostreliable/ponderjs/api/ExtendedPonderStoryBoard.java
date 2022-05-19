package com.almostreliable.ponderjs.api;

import com.simibubi.create.foundation.ponder.SceneBuildingUtil;

@FunctionalInterface
public interface ExtendedPonderStoryBoard {
    void program(ExtendedSceneBuilder scene, SceneBuildingUtilDelegate util);
}
