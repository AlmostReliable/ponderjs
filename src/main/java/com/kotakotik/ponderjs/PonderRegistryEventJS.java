package com.kotakotik.ponderjs;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;


public class PonderRegistryEventJS extends EventJS {

    public PonderBuilderJS create(String name, IngredientJS ingredient) {
        return new PonderBuilderJS(name, ingredient.getVanillaItems());
    }
}
