package com.kotakotik.ponderjs.kubejs;

import com.kotakotik.ponderjs.PonderJS;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import dev.latvian.kubejs.KubeJSRegistries;
import dev.latvian.kubejs.event.EventJS;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PonderTagRegistryEventJS extends EventJS {
    public static boolean rerun = false;

    public PonderTagRegistryEventJS create(String name, ResourceLocation displayItem, String title, String description, Object defaultItems) {
        try {
            ResourceLocation id = PonderJS.appendKubeToId(name);
            if (!PonderJSPlugin.tags.contains(id)) {
                PonderJSPlugin.tags.add(id);
            }
            if (!PonderJSPlugin.namespaces.contains(id.getNamespace())) {
                PonderJSPlugin.namespaces.add(id.getNamespace());
            }
            PonderTag tag = new PonderTag(id)
                    .item(KubeJSRegistries.items().get(displayItem))
                    .defaultLang(title, description);
            PonderRegistry.TAGS.listTag(tag);
            PonderJSPlugin.get().tagItemEvent.add(id.toString(), defaultItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
            return this;
    }

    public PonderTagRegistryEventJS remove(boolean clearItems, String... id) {
            List<ResourceLocation> res = Arrays.stream(id)
                    .map(PonderJS::appendCreateToId)
                    .collect(Collectors.toList());
            if(!rerun && !PonderRegistry.TAGS.getListedTags()
                    .removeIf(tag -> {
                        if(res.contains(tag.getId())) {
                            if(clearItems) {
                                try {
                                    PonderJSPlugin.get().tagItemEvent.remove(tag.getId().toString(), ListJS.of(PonderRegistry.TAGS.getItems(tag)));
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

    public PonderTagRegistryEventJS create(String name, ResourceLocation displayItem, String title, String description) {
        return create(name, displayItem, title, description, new ListJS());
    }
}
