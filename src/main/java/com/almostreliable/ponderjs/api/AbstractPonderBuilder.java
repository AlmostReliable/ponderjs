package com.almostreliable.ponderjs.api;

import com.almostreliable.ponderjs.PonderJS;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.PonderTagRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Also known as type param hell
 *
 * @param <S> Self
 */
public abstract class AbstractPonderBuilder<S extends AbstractPonderBuilder<S>> {
    protected static List<String> added = new ArrayList<>();
    protected ResourceLocation name;
    protected Set<Item> items;

    public AbstractPonderBuilder(ResourceLocation name, Set<Item> items) {
        this.name = name;
        this.items = items;
    }

    protected abstract S getSelf();

    protected String createTitleTranslationKey(String scene) {
        return name.getPath() + "." + scene;
    }

    /**
     * @param tags The list of tags to add
     * @return Self
     */
    public S tag(PonderTag... tags) {
        for (PonderTag tag : tags) {
            PonderTagRegistry.TagBuilder tagBuilder = PonderRegistry.TAGS.forTag(tag);
            items.forEach(tagBuilder::add);
        }
        return getSelf();
    }

    protected S addStoryBoard(Item item, ResourceLocation schematic, PonderStoryBoardEntry.PonderStoryBoard scene) {
        PonderStoryBoardEntry entry = new PonderStoryBoardEntry(scene,
                name.getNamespace(),
                schematic,
                item.getRegistryName());
        PonderRegistry.addStoryBoard(entry);
        PonderJS.STORIES_MANAGER.add(entry);
        return getSelf();
    }

    protected S addNamedStoryBoard(String name, String displayName, Item item, ResourceLocation schematic, PonderStoryBoardEntry.PonderStoryBoard scene) {
        return addStoryBoard(item, schematic, (builder, util) -> {
            builder.title(name, displayName);
            scene.program(builder, util);
        });
    }
}
