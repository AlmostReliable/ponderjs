package com.almostreliable.ponderjs.api;

import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.element.PonderElement;

@FunctionalInterface
public interface OnElementAction {

    void accept(Context context);

    record Context(PonderElement getElement, PonderScene getScene) {}
}
