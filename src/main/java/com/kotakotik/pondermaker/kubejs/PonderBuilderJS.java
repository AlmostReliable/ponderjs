package com.kotakotik.pondermaker.kubejs;

import com.kotakotik.pondermaker.PonderMaker;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.content.MechanicalSawScenes;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import com.simibubi.create.foundation.ponder.content.PonderTagRegistry;
import com.simibubi.create.foundation.ponder.elements.ParrotElement;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import dev.latvian.kubejs.BuiltinKubeJSPlugin;
import dev.latvian.kubejs.bindings.BlockWrapper;
import dev.latvian.kubejs.client.KubeJSClientResourcePack;
import dev.latvian.kubejs.recipe.RecipeEventJS;
import dev.latvian.kubejs.util.BuilderBase;
import dev.latvian.mods.rhino.Function;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.ScriptableObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.util.TriConsumer;

import java.security.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PonderBuilderJS<T extends Item> {
    protected String name;

    public List<ResourceLocation> items = new ArrayList<>();

    protected Consumer<Unit> function = ($) -> {
        PonderMaker.LOGGER.info("Starting ponder registration \"" + name + "\" with items " + Arrays.toString(items.toArray()));
    };

    public PonderBuilderJS(String name, ResourceLocation... ids) {
//        this.block = block;
        this.name = name;
        items.addAll(Arrays.asList(ids));
    }

    public PonderBuilderJS<T> step(Consumer<Unit> step) {
        function = function.andThen(step);
        return this;
    }

//    public PonderBuilderJS<T> scene(List<ResourceLocation> items, List<List<Object>> storyBoards, BiConsumer<SceneBuilder, SceneBuildingUtil> scene) {
//        return step(i -> {
//           PonderRegistry.MultiSceneBuilder multiSceneBuilder = PonderRegistry.forComponents(items.stream().map((id) -> PonderRegistryEventJS.createItemProvider(RegistryObject.of(id, ForgeRegistries.ITEMS))).collect(Collectors.toList()));
//                        multiSceneBuilder.addStoryBoard("test", scene::accept);
//        });
//    }

    protected String getName(String scene) {
        return name + "." + scene;
    }

    protected PonderBuilderJS<T> addStoryBoard(String name, ResourceLocation item, String schematic, SceneConsumer scene) {
        return step(($) -> PonderRegistry.addStoryBoard(PonderRegistryEventJS.createItemProvider(RegistryObject.of(item, ForgeRegistries.ITEMS)), schematic,(b, u) -> scene.accept(b, u, name)));
    }

    protected static List<String> added = new ArrayList<>();

    protected PonderBuilderJS<T> addStoryBoard(String name, String displayName, ResourceLocation item, String schematic, SceneConsumer scene) {
        String n = getName(name);
        if(added.contains(n)) return this;
        added.add(n);
        return addStoryBoard(n, item, schematic, (builder, util, jsUtil) -> {
            builder.title(name, displayName);
            scene.accept(builder, util, jsUtil);
        });
    }

    public PonderBuilderJS<T> addItem(ResourceLocation... ids) {
        items.addAll(Arrays.asList(ids));
        return this;
    }

    public PonderBuilderJS<T> addItems(ResourceLocation... ids) {
        return addItem(ids);
    }

    public PonderBuilderJS<T> scene(String name, String displayName, String schematic, SceneConsumer scene) {
        items.forEach(id -> addStoryBoard(getName(name), displayName, id, schematic, scene));
        return this;
    }

    public PonderBuilderJS<T> tag(String... tags) {
        step(($) -> {
            for(String tag : tags) {
                if(!tag.contains(":")) tag = "create:" + tag;
                ResourceLocation id = new ResourceLocation(tag);
                PonderRegistry.TAGS.forItems(items.toArray(new ResourceLocation[0])).add(PonderMaker.getTagByName(id).orElse(PonderTag.CREATIVE));
            }
        });
        return this;
    }

    public PonderBuilderJS<T> execute() {
        function.accept(Unit.INSTANCE);
        return this;
    }

    @FunctionalInterface
    public interface SceneConsumer extends TriConsumer<SceneBuilder, SceneBuildingUtil, PonderBuilderSceneBuildingUtil> {
        default void accept(SceneBuilder b, SceneBuildingUtil u, String name) {
            accept(b, u, new PonderBuilderSceneBuildingUtil(b, u));
        }
    }
}
