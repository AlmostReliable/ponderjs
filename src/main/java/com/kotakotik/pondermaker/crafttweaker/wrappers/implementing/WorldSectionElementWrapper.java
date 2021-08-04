package com.kotakotik.pondermaker.crafttweaker.wrappers.implementing;

import com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces.IWorldSectionElement;
import com.simibubi.create.foundation.ponder.elements.WorldSectionElement;

public class WorldSectionElementWrapper implements IWorldSectionElement {
    private final WorldSectionElement internal;

    public WorldSectionElementWrapper(WorldSectionElement internal) {
        this.internal = internal;
    }

    @Override
    public WorldSectionElement getInternal() {
        return internal;
    }
}
