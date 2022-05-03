package com.kotakotik.ponderjs;

import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PonderTagRegistryEventJS extends EventJS {
    public PonderTagRegistryEventJS create(String name, ResourceLocation displayItem, String title, String description) {
        try {
            ResourceLocation id = PonderJS.appendKubeToId(name);
            if (!PonderJS.tags.contains(id)) {
                PonderJS.tags.add(id);
            }
            if (!PonderJS.namespaces.contains(id.getNamespace())) {
                PonderJS.namespaces.add(id.getNamespace());
            }
            PonderTag tag = new PonderTag(id)
                    .item(KubeJSRegistries.items().get(displayItem))
                    .defaultLang(title, description);
            PonderRegistry.TAGS.listTag(tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
            return this;
    }

    public PonderTagRegistryEventJS remove(boolean clearItems, String... id) {
            List<ResourceLocation> res = Arrays.stream(id)
                    .map(PonderJS::appendCreateToId).toList();
            if (!PonderRegistry.TAGS.getListedTags()
                    .removeIf(tag -> {
                        if (res.contains(tag.getId())) {
                            if (clearItems) {
                                try {
                                    PonderJS.tagItemEvent.remove(tag.getId().toString(), ListJS.of(PonderRegistry.TAGS.getItems(tag)));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                            return true;
                        }
                        return false;
                    })) {
                throw new NullPointerException("No tags found matching " + id);
            }
            return this;
    }

    public PonderTagRegistryEventJS remove(String... id) {
        return remove(true, id);
    }
}
