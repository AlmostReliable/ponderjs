package com.kotakotik.ponderjs;

import com.kotakotik.ponderjs.api.AbstractPonderBuilder;
import com.kotakotik.ponderjs.util.SceneBuilderJS;
import com.kotakotik.ponderjs.util.SceneBuildingUtilJS;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;

public class PonderBuilderJS extends
        AbstractPonderBuilder<PonderBuilderJS, PonderBuilderJS.SceneConsumer> {
    public PonderBuilderJS(String name, Set<Item> items) {
        super(PonderJS.appendKubeToId(name), items);
        String namespace = this.name.getNamespace();
        if (!PonderJS.namespaces.contains(namespace)) {
            PonderJS.namespaces.add(namespace);
        }
    }

//    public PonderBuilderJS<T> scene(List<ResourceLocation> items, List<List<Object>> storyBoards, BiConsumer<SceneBuilder, SceneBuildingUtil> scene) {
//        return step(i -> {
//           PonderRegistry.MultiSceneBuilder multiSceneBuilder = PonderRegistry.forComponents(items.stream().map((id) -> PonderRegistryEventJS.createItemProvider(RegistryObject.of(id, ForgeRegistries.ITEMS))).collect(Collectors.toList()));
//                        multiSceneBuilder.addStoryBoard("test", scene::accept);
//        });
//    }]

    public static HashMap<String, SceneConsumer> scenes = new HashMap<>();

    public PonderBuilderJS scene(String name, String displayName, String schematic, SceneConsumer scene) {
        String fullName = getName(name);
        if (PonderRegistryEventJS.rerun && !scenes.containsKey(fullName)) {
            ScriptType.CLIENT.console.error("Tried to register ponder scene " + fullName + " in a reload, you'll have to restart!");
            return this;
        }
        scenes.put(fullName, scene);
        if (!PonderRegistryEventJS.rerun) {
            String pathOnlyName = getPathOnlyName(name);
            Couple<String> sceneId = Couple.create(this.name.getNamespace(), pathOnlyName);
            if (!PonderJS.scenes.contains(sceneId)) {
                PonderJS.scenes.add(sceneId);
            }
            for (var id : items)
                addNamedStoryBoard(pathOnlyName, displayName, id, PonderJS.appendKubeToId(schematic), (b, u) -> programStoryBoard(fullName, b, u));
        }
        return this;
    }

    @Override
    public PonderBuilderJS getSelf() {
        return this;
    }

    @Override
    protected ItemProviderEntry<?> getItemProviderEntry(Item item) {
        return new ItemProviderEntry<>(Create.registrate(), RegistryObject.create(item.getRegistryName(), ForgeRegistries.ITEMS));
    }

    @Override
    protected void programStoryBoard(SceneConsumer scene, SceneBuilder builder, SceneBuildingUtil util) {
        scene.accept(new SceneBuilderJS(builder), new SceneBuildingUtilJS(util));
    }

    protected void programStoryBoard(String name, SceneBuilder builder, SceneBuildingUtil util) {
        try {
            programStoryBoard(scenes.get(name), builder, util);
        } catch (Throwable t) {
            t.printStackTrace();
            ScriptType.CLIENT.console.error("Error occurred in ponder " + name, t);
        }
    }

    @Override
    protected PonderBuilderJS.SceneConsumer createConsumer(BiConsumer<SceneBuilder, SceneBuildingUtil> consumer) {
        return (b, u) -> consumer.accept(b.getInternal(), u.getInternal());
    }

    @FunctionalInterface
    public interface SceneConsumer extends BiConsumer<SceneBuilderJS, SceneBuildingUtilJS> {
    }
}
