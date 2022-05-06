package com.kotakotik.ponderjs.api;

import com.kotakotik.ponderjs.PonderJSMod;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Also known as type param hell
 *
 * @param <S> Self
 * @param <C> The scene bi-consumer
 */
public abstract class AbstractPonderBuilder<S extends AbstractPonderBuilder<S>> {
    protected ResourceLocation name;
    protected Set<Item> items;

    protected abstract S getSelf();

    public AbstractPonderBuilder(ResourceLocation name, Set<Item> items) {
        this.name = name;
        this.items = items;
    }

    /**
     * @return The message to print in console at the start of the functions, if null nothing is printed
     */
    protected String getStartMessage() {
        return "Starting ponder registration \"" + name + "\" with items " + Arrays.toString(items.toArray());
    }

    protected Runnable function = () -> {
        String s = getStartMessage();
        if (s != null) PonderJSMod.LOGGER.info(s);
    };

    protected String createSceneId(String scene) {
        return name.toString().replace(":", ".") + "." + scene;
    }

    protected String getPathOnlyName(String scene) {
        return name.getPath() + "." + scene;
    }

    protected String createSceneId(String scene, Item item) {
        return createSceneId(scene) + "." + item.getDescriptionId();
    }

    /**
     * @param tags The list of tags to add
     * @return Self
     */
    public S tag(PonderTag... tags) {
            for(PonderTag tag : tags) {
                PonderTagRegistry.TagBuilder tagBuilder = PonderRegistry.TAGS.forTag(tag);
                items.forEach(tagBuilder::add);
            }
            return getSelf();
    }

    /**
     * @return The item provider.
     */
    protected abstract ItemProviderEntry<?> getItemProviderEntry(Item item);

    protected S addStoryBoard(Item item, ResourceLocation schematic, PonderStoryBoardEntry.PonderStoryBoard scene) {
        new PonderRegistrationHelper(name.getNamespace())
                .forComponents(getItemProviderEntry(item))
                .addStoryBoard(schematic, scene);
        return getSelf();
    }

    protected static List<String> added = new ArrayList<>();

    protected S addNamedStoryBoard(String name, String displayName, Item item, ResourceLocation schematic, PonderStoryBoardEntry.PonderStoryBoard scene) {
        String cacheName = createSceneId(name, item);
        if(added.contains(cacheName)) return getSelf();
        added.add(cacheName);
        return addStoryBoard(item, schematic, (builder, util) -> {
                        builder.title(name, displayName);
                        scene.program(builder, util);
                    });
    }
}
