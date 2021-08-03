package com.kotakotik.pondermaker.crafttweaker.wrappers.implementing;

import com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces.ITextWindowElementBuilder;
import com.simibubi.create.foundation.ponder.elements.TextWindowElement;

public class TextWindowElementBuilderWrapper implements ITextWindowElementBuilder {
    private final TextWindowElement.Builder internal;

    public TextWindowElementBuilderWrapper(TextWindowElement.Builder internal) {
        this.internal = internal;
    }

    @Override
    public TextWindowElement.Builder getInternal() {
        return internal;
    }
}
