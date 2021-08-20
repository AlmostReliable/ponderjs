package com.kotakotik.pondermaker.kubejs;

import com.kotakotik.pondermaker.PonderMaker;
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
    public PonderTagRegistryEventJS create(String name, ResourceLocation displayItem, String title, String description, Object defaultItems) {
        try {
            ResourceLocation id = new ResourceLocation("kubejs", name);
            PonderTag tag = new PonderTag(id)
                    .item(KubeJSRegistries.items().get(displayItem))
                    .defaultLang(title, description);
            PonderRegistry.TAGS.listTag(tag);
            PonderJS.get().tagItemEvent.add(id.toString(), defaultItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
            return this;
    }

    public PonderTagRegistryEventJS remove(String... id) {
            List<ResourceLocation> res = Arrays.stream(id)
                    .map(PonderMaker::appendCreateToId)
                    .collect(Collectors.toList());
            if(!PonderRegistry.TAGS.getListedTags()
                    .removeIf(tag -> res.contains(tag.getId()))) {
                throw new NullPointerException("No tags found matching " + id);
            }
            return this;
    }

    public PonderTagRegistryEventJS create(String name, ResourceLocation displayItem, String title, String description) {
        return create(name, displayItem, title, description, new ListJS());
    }
}
