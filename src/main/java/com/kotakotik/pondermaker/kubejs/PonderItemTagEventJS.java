package com.kotakotik.pondermaker.kubejs;

import com.google.common.collect.Multimap;
import com.kotakotik.pondermaker.PonderMaker;
import com.kotakotik.pondermaker.common.IDelegatedWithSteps;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import com.simibubi.create.foundation.ponder.content.PonderTagRegistry;
import dev.latvian.kubejs.event.EventJS;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class PonderItemTagEventJS extends EventJS implements IDelegatedWithSteps<PonderItemTagEventJS> {
    public static Field tagField = ObfuscationReflectionHelper.findField(PonderTagRegistry.class, "tags");

    private static Runnable function = () -> {};

    public PonderItemTagEventJS add(String id, Object toAdd) {
        return step(() -> {
            PonderRegistry.TAGS.forItems(ListJS.orSelf(toAdd).stream()
                    .map(Object::toString)
                    .map(ResourceLocation::new).toArray(ResourceLocation[]::new))
                    .add(PonderMaker.getTagByName(id).get());
        });
    }

    public PonderItemTagEventJS remove(String id, Object toRemove) {
        return step(() -> {
            PonderTagRegistry r = PonderRegistry.TAGS;
            try {
                Multimap<ResourceLocation, PonderTag> tags = (Multimap<ResourceLocation, PonderTag>) tagField.get(r);
                for(ResourceLocation itemId : ListJS.orSelf(toRemove).stream()
                        .map(Object::toString)
                        .map(ResourceLocation::new).toArray(ResourceLocation[]::new)) {
                    if(!tags.get(itemId)
                            .removeIf(t -> t.getId().equals(PonderMaker.appendCreateToId(id)))) {
                        throw new NullPointerException("No tags found matching " + id + " in item " + itemId);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Runnable getSteps() {
        return function;
    }

    @Override
    public void setSteps(Runnable steps) {
        function = steps;
    }

    @Override
    public PonderItemTagEventJS getSelf() {
        return this;
    }
}
