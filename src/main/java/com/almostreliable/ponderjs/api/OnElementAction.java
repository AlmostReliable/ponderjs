package com.almostreliable.ponderjs.api;

import net.createmod.ponder.api.element.PonderElement;
import net.createmod.ponder.foundation.PonderScene;

@FunctionalInterface
public interface OnElementAction {

    void accept(Context context);

    record Context(PonderElement getElement, PonderScene getScene) {}
}
