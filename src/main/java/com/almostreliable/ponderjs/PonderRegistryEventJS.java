package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.mixin.PonderSceneRegistryAccessor;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import net.createmod.ponder.foundation.registration.PonderSceneRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PonderRegistryEventJS implements KubeEvent {

    private final PonderSceneRegistry sceneRegistry;

    public PonderRegistryEventJS(PonderSceneRegistry sceneRegistry) {
        this.sceneRegistry = sceneRegistry;
    }

    public void remove(Ingredient ingredient) {
        var scenes = ((PonderSceneRegistryAccessor) sceneRegistry).ponderjs$scenes();
        Set<ResourceLocation> itemIds = Arrays
                .stream(ingredient.getItems())
                .map(ItemStack::getItem)
                .map(BuiltInRegistries.ITEM::getKey)
                .collect(Collectors.toSet());

        for (var itemId : itemIds) {
            scenes.removeAll(itemId);
        }
    }

    public PonderBuilderJS create(Ingredient ingredient) {
        if (ingredient.isEmpty()) {
            throw new IllegalArgumentException("Provided items must not be empty!");
        }

        Set<ResourceLocation> itemIds = Arrays
                .stream(ingredient.getItems())
                .map(ItemStack::getItem)
                .map(BuiltInRegistries.ITEM::getKey)
                .collect(Collectors.toSet());
        return new PonderBuilderJS(itemIds, sceneRegistry);
    }

    @SuppressWarnings("DataFlowIssue")
    public void printParticleNames() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("### Particles ###").append("\n");
        BuiltInRegistries.PARTICLE_TYPE.stream()
                .filter(SimpleParticleType.class::isInstance)
                .map(BuiltInRegistries.PARTICLE_TYPE::getKey)
                .sorted()
                .forEach(id -> sb.append(" - ").append(id).append("\n"));
        ConsoleJS.CLIENT.info(sb.toString());
    }
}
