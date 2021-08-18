package com.kotakotik.pondermaker.kubejs.util;

import com.simibubi.create.foundation.ponder.SceneBuildingUtil;

public class SceneBuildingUtilJS implements ISceneBuildingUtilJS {
    protected final SceneBuildingUtil internal;

    public SceneBuildingUtilJS(SceneBuildingUtil internal) {
        this.internal = internal;
    }

    @Override
    public SceneBuildingUtil getInternal() {
        return internal;
    }


}
