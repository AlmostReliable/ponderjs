package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.util.PonderPlatform;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PonderRegistryEventJS extends EventJS {

    private final PonderSceneRegistrationHelper<ResourceLocation> helper;

    public PonderRegistryEventJS(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        this.helper = helper;
    }

    public PonderBuilderJS create(Ingredient ingredient) {
        if (ingredient.isEmpty()) {
            throw new IllegalArgumentException("Provided items must not be empty!");
        }

        Set<ResourceLocation> itemIds = Arrays
                .stream(ingredient.getItems())
                .map(ItemStack::getItem)
                .map(BuiltInRegistries.ITEM::getKey)
                .collect(
                        Collectors.toSet());
        return new PonderBuilderJS(itemIds, helper);
    }

    public void printParticleNames() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("### Particles ###").append("\n");
        PonderPlatform.getParticleTypes()
                .filter(SimpleParticleType.class::isInstance)
                .map(PonderPlatform::getParticleTypeName)
                .sorted()
                .forEach(id -> sb.append(" - ").append(id).append("\n"));
        ConsoleJS.CLIENT.info(sb.toString());
    }
}
