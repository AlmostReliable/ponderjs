package com.kotakotik.ponderjs;

import com.google.common.collect.Multimap;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.PonderTagRegistry;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class PonderItemTagEventJS extends EventJS {
    public static Field tagField = ObfuscationReflectionHelper.findField(PonderTagRegistry.class, "tags");

    public PonderItemTagEventJS add(String id, Object toAdd) {
            PonderRegistry.TAGS.forItems(ListJS.orSelf(toAdd).stream()
                    .map(Object::toString)
                    .map(ResourceLocation::new).toArray(ResourceLocation[]::new))
                    .add(PonderJS.getTagByName(id).get());
            return this;
    }

    public PonderItemTagEventJS remove(String id, Object toRemove) throws IllegalAccessException {
            PonderTagRegistry r = PonderRegistry.TAGS;
                Multimap<ResourceLocation, PonderTag> tags = (Multimap<ResourceLocation, PonderTag>) tagField.get(r);
                for(ResourceLocation itemId : ListJS.orSelf(toRemove).stream()
                        .map(Object::toString)
                        .map(ResourceLocation::new).toArray(ResourceLocation[]::new)) {
                    if (!PonderRegistryEventJS.rerun && !tags.get(itemId)
                            .removeIf(t -> t.getId().equals(PonderJS.appendCreateToId(id)))) {
                        throw new NullPointerException("No tags found matching " + id + " in item " + itemId);
                    }
                }
            return this;
    }
}
