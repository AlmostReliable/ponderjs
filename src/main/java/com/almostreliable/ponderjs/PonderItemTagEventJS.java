package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.util.PonderPlatform;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PonderItemTagEventJS extends EventJS {

    private final PonderTagRegistrationHelper<ResourceLocation> helper;

    public PonderItemTagEventJS(PonderTagRegistrationHelper<ResourceLocation> helper) {
        this.helper = helper;
    }

    public void createTag(String id, Item displayItem, String title, String description, @Nullable Ingredient ingredient) {
        ResourceLocation idWithNamespace = PonderJS.appendKubeToId(id);
        helper
                .registerTag(idWithNamespace)
                .item(displayItem)
                .title(title)
                .description(description).register();

        if (ingredient != null) {
            var tags = helper.addToTag(idWithNamespace);
            for (ItemStack item : ingredient.getItems()) {
                ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item.getItem());
                tags.add(itemId);
            }
        }

        PonderJS.NAMESPACES.add(idWithNamespace.getNamespace());
    }

    public void createTag(String id, Item displayItem, String title, String description) {
        createTag(id, displayItem, title, description, null);
    }

    public void add(PonderTag tag, Ingredient ingredient) {
        if (ingredient.isEmpty()) return;

        var tagBuilder = helper.addToTag(tag.getId());
        for (ItemStack item : ingredient.getItems()) {
            var id = BuiltInRegistries.ITEM.getKey(item.getItem());
            tagBuilder.add(id);
        }
    }

    public void removeTag(PonderTag... tagsToRemove) {
        if (tagsToRemove.length == 0) return;

        var reg = PonderJS.getTagRegistryAccessor();
        for (var tag : tagsToRemove) {
            if (tag.equals(reg.getMissing())) continue;

            reg.getRegisteredTags().remove(tag.getId());
            reg.getListedTags().remove(tag);
            remove(tag, PonderJS.getTagRegistry().getItems(tag));
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
        var reg = PonderJS.getTagRegistryAccessor();

        for (ResourceLocation item : items) {
            if (reg.getComponentTagMap().get(item).remove(tag.getId())) {
                ConsoleJS.CLIENT.info("Removed ponder tag " + tag.getId() + " from item " + item);
            }
        }
    }
}
