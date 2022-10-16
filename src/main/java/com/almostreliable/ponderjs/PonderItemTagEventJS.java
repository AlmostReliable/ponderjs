package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.mixin.PonderTagRegistryAccessor;
import com.almostreliable.ponderjs.util.PonderPlatform;
import com.google.common.collect.Multimap;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.PonderTagRegistry;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class PonderItemTagEventJS extends EventJS {
    public void createTag(String id, ItemStack displayItem, String title, String description, @Nullable Ingredient ingredient) {
        PonderJS.getTagByName(id).ifPresent(tag -> {
            throw new IllegalArgumentException("Tag " + id + " already exists");
        });

        ResourceLocation idWithNamespace = PonderJS.appendKubeToId(id);
        PonderTag ponderTag = new PonderTag(idWithNamespace)
                .item(displayItem.getItem())
                .defaultLang(title, description);
        PonderRegistry.TAGS.listTag(ponderTag);

        if(ingredient != null) {
            add(ponderTag, ingredient);
        }
        PonderJS.NAMESPACES.add(idWithNamespace.getNamespace());
    }

    public void createTag(String id, ItemStack displayItem, String title, String description) {
        createTag(id, displayItem, title, description, null);
    }

    public void removeTag(PonderTag... tags) {
        for (PonderTag tag : tags) {
            Set<ResourceLocation> items = PonderRegistry.TAGS.getItems(tag);
            PonderRegistry.TAGS.getListedTags().remove(tag);
            remove(tag, items);
        }
    }

    public void add(PonderTag tag, Ingredient ingredient) {
        if (ingredient.isEmpty()) return;
        PonderTagRegistry.TagBuilder tagBuilder = PonderRegistry.TAGS.forTag(tag);
        for (ItemStack item : ingredient.getItems()) {
            tagBuilder.add(item.getItem());
        }
    }

    public void remove(PonderTag tag, Ingredient ingredient) {
        if (ingredient.isEmpty()) return;
        Set<ResourceLocation> ids = Arrays.stream(ingredient.getItems())
                .map(ItemStack::getItem)
                .map(PonderPlatform::getItemName)
                .collect(Collectors.toSet());
        remove(tag, ids);
    }

    private void remove(PonderTag tag, Set<ResourceLocation> items) {
        Multimap<ResourceLocation, PonderTag> tagMap = ((PonderTagRegistryAccessor) PonderRegistry.TAGS).getTags();
        for (ResourceLocation item : items) {
            Collection<PonderTag> tagsForItem = tagMap.get(item);
            if (tagsForItem.remove(tag)) {
                ConsoleJS.CLIENT.info("Removed ponder tag " + tag.getId() + " from item " + item);
            }
        }
    }
}
