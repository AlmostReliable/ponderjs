package com.kotakotik.ponderjs.api;

import com.kotakotik.ponderjs.PonderJS;
import com.kotakotik.ponderjs.PonderJSMod;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Also known as type param hell
 *
 * @param <S> Self
 * @param <C> The scene bi-consumer
 */
public abstract class AbstractPonderBuilder<
        S extends AbstractPonderBuilder<S, C>,
        C extends BiConsumer<?, ?>> {
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

    protected String getName(String scene) {
        return name.toString().replace(":", ".") + "." + scene;
    }

    protected String getPathOnlyName(String scene) {
        return name.getPath() + "." + scene;
    }

    protected String getName(String scene, Item item) {
        return getName(scene) + "." + item.getDescriptionId();
    }

    /**
     * @param tags The list of tags to add
     * @return Self
     */
    protected S tag(String... tags) {
            for(String tag : tags) {
                PonderTag ponderTag = PonderJS.getTagByName(tag).orElseThrow();
                PonderTagRegistry.TagBuilder tagBuilder = PonderRegistry.TAGS.forTag(ponderTag);
                items.forEach(tagBuilder::add);
            }
            return getSelf();
    }

    /**
     * @return The item provider.
     */
    protected abstract ItemProviderEntry<?> getItemProviderEntry(Item item);

    protected abstract void programStoryBoard(C scene, SceneBuilder builder, SceneBuildingUtil util);

    protected S addStoryBoard(Item item, ResourceLocation schematic, PonderStoryBoardEntry.PonderStoryBoard scene) {
        new PonderRegistrationHelper(name.getNamespace())
                .forComponents(getItemProviderEntry(item))
                .addStoryBoard(schematic, scene);
        return getSelf();
    }

    protected static List<String> added = new ArrayList<>();

    protected S addNamedStoryBoard(String name, String displayName, Item item, ResourceLocation schematic, PonderStoryBoardEntry.PonderStoryBoard scene) {
        String cacheName = getName(name, item);
        if(added.contains(cacheName)) return getSelf();
        added.add(cacheName);
        return addStoryBoard(item, schematic, (builder, util) -> {
                        builder.title(name, displayName);
                        scene.program(builder, util);
                    });
    }

    protected abstract C createConsumer(BiConsumer<SceneBuilder, SceneBuildingUtil> consumer);
}
