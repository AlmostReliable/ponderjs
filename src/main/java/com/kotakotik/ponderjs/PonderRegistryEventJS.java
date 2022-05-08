package com.kotakotik.ponderjs;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;


public class PonderRegistryEventJS extends EventJS {

    public PonderRegistryEventJS() {
        PonderJS.storiesManager.clear();
    }

    public PonderBuilderJS create(String name, IngredientJS ingredient) {
        if(ingredient.isEmpty()) {
            throw new IllegalArgumentException("Provided items must not be empty!");
        }
        return new PonderBuilderJS(name, ingredient.getVanillaItems());
    }
}
