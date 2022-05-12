package com.almostreliable.ponderjs;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PonderRegistryEventJS extends EventJS {

    public PonderRegistryEventJS() {
        PonderJS.STORIES_MANAGER.clear();
    }

    public PonderBuilderJS create(String name, IngredientJS ingredient) {
        if (ingredient.isEmpty()) {
            throw new IllegalArgumentException("Provided items must not be empty!");
        }
        return new PonderBuilderJS(name, ingredient.getVanillaItems());
    }

    public void printParticleNames() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("### Particles ###").append("\n");
        //noinspection ConstantConditions
        ForgeRegistries.PARTICLE_TYPES.getValues()
                .stream()
                .filter(SimpleParticleType.class::isInstance)
                .map(ForgeRegistryEntry::getRegistryName)
                .sorted()
                .forEach(id -> sb.append(" - ").append(id).append("\n"));
        ConsoleJS.CLIENT.info(sb.toString());
    }
}
