package com.almostreliable.ponderjs;

import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PonderItemTagEventJS implements KubeEvent {

    private final PonderTagRegistrationHelper<ResourceLocation> helper;

    public PonderItemTagEventJS(PonderTagRegistrationHelper<ResourceLocation> helper) {
        this.helper = helper;
    }

    public void createTag(String id, Consumer<Builder> onCreate) {
        var idWithNameSpace = PonderJS.appendKubeToId(id);
        PonderJS.NAMESPACES.add(idWithNameSpace.getNamespace());

        var builder = new Builder(idWithNameSpace);
        onCreate.accept(builder);
        builder.fin(helper);
    }

    public void createTag(String id, Item displayItem, String title, String description, @Nullable Ingredient ingredient) {
        createTag(id, builder -> {
            builder.icon(displayItem);
            builder.title(title);
            builder.description(description);
            if (ingredient != null) builder.items(ingredient);
        });
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
                .map(BuiltInRegistries.ITEM::getKey)
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

    @SuppressWarnings("UnusedReturnValue")
    public static class Builder {

        private final LinkedHashSet<ResourceLocation> itemIds = new LinkedHashSet<>();
        private final ResourceLocation id;
        @Nullable
        private String title;
        @Nullable
        private String description;
        private Item itemIcon = Items.BARRIER;
        private boolean noIndex = false;
        private boolean addItemIconToItems = false;

        private Builder(ResourceLocation id) {
            this.id = id;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder icon(Item item) {
            this.itemIcon = item;
            return this;
        }

        public Builder noIndex() {
            this.noIndex = true;
            return this;
        }

        public Builder addIconToItems() {
            addItemIconToItems = true;
            return this;
        }

        public Builder items(Ingredient ingredient) {
            for (ItemStack item : ingredient.getItems()) {
                var itemId = BuiltInRegistries.ITEM.getKey(item.getItem());
                itemIds.add(itemId);
            }

            return this;
        }

        private void fin(PonderTagRegistrationHelper<ResourceLocation> helper) {
            Preconditions.checkNotNull(title, "Title cannot be null for tag " + id);
            Preconditions.checkNotNull(description, "Description cannot be null for tag " + id);

            var tagBuilder = helper.registerTag(id).title(title).description(description);
            if (!noIndex) tagBuilder.addToIndex();
            tagBuilder.item(itemIcon, true, addItemIconToItems);

            tagBuilder.register();

            for (var itemId : itemIds) {
                helper.addTagToComponent(itemId, id);
            }
        }
    }
}
