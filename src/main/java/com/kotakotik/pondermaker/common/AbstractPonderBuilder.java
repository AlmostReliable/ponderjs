package com.kotakotik.pondermaker.common;

import com.kotakotik.pondermaker.PonderMaker;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Also known as type param hell
 *
 * @param <T> The Item type
 * @param <S> Self
 * @param <C> The scene bi-consumer
 */
public abstract class AbstractPonderBuilder<T,
        S extends AbstractPonderBuilder<T, S, C>,
        C extends BiConsumer<?, ?>> {
    protected String name;
    protected List<T> items;

    protected abstract S getSelf();

    public AbstractPonderBuilder(String name, List<T> items) {
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
        if(s != null) PonderMaker.LOGGER.info(s);
    };

    protected String getName(String scene) {
        return name + "." + scene;
    }

    protected String getName(String scene, T item) {
        return getName(scene) + "." + itemToString(item);
    }

    protected abstract ResourceLocation[] itemsToIdArray();

    /**
     * @param tags The list of tags to add
     * @return Self
     */
    protected S tag(String... tags) {
            for(String tag : tags) {
                PonderRegistry.TAGS.forItems(itemsToIdArray()).add(PonderMaker.getTagByName(tag).get());
            }
            return getSelf();
    }

    /**
     * @return The item provider. There's a utility method for that: {@link PonderMaker#createItemProvider(RegistryObject)}
     */
    protected abstract ItemProviderEntry<?> getItemProviderEntry(T item);
//    protected abstract PonderStoryBoardEntry.PonderStoryBoard storyBoard(C scene);
    protected abstract void programStoryBoard(C scene, SceneBuilder builder, SceneBuildingUtil util);

    protected S addStoryBoard(T item, String schematic, PonderStoryBoardEntry.PonderStoryBoard scene) {
            new PonderRegistrationHelper("kubejs")
                .forComponents(getItemProviderEntry(item))
                .addStoryBoard(schematic, scene);
//            PonderRegistry.addStoryBoard(getItemProviderEntry(item), schematic, scene)
        return getSelf();
    }

    protected static List<String> added = new ArrayList<>();

    protected S addNamedStoryBoard(String name, String displayName, T item, String schematic, PonderStoryBoardEntry.PonderStoryBoard scene) {
        String cacheName = getName(name, item);
        if(added.contains(cacheName)) return getSelf();
        added.add(cacheName);
        return addStoryBoard(item, schematic, (builder, util) -> {
                        builder.title(name, displayName);
                        scene.program(builder, util);
//                        programStoryBoard(scene, builder, util);
//                        storyBoard(scene).program(builder, util);
                    });
    }

    protected abstract String itemToString(T item);
    protected abstract C createConsumer(BiConsumer<SceneBuilder, SceneBuildingUtil> consumer);
}
