package com.kotakotik.ponderjs.kubejs.util;

import com.simibubi.create.foundation.ponder.SceneBuildingUtil;

/**
 * Boilerplate code for {@link SceneBuildingUtilJS}
 */
public interface ISceneBuildingUtilJS {
    SceneBuildingUtil getInternal();

    default SceneBuildingUtil.SelectionUtil select() {
        return getInternal().select;
    }
    default SceneBuildingUtil.SelectionUtil getSelect() {return select();}
    default SceneBuildingUtil.VectorUtil vector() {
        return getInternal().vector;
    }
    default SceneBuildingUtil.VectorUtil getVector(){return vector();}
    default SceneBuildingUtil.PositionUtil grid() {
        return getInternal().grid;
    }
    default SceneBuildingUtil.PositionUtil getGrid(){return grid();}
}
