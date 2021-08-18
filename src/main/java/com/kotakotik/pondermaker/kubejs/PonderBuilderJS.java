package com.kotakotik.pondermaker.kubejs;

import com.kotakotik.pondermaker.PonderMaker;
import com.kotakotik.pondermaker.common.AbstractPonderBuilder;
import com.kotakotik.pondermaker.kubejs.util.SceneBuilderJS;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class PonderBuilderJS extends
        AbstractPonderBuilder<ResourceLocation, PonderBuilderJS, PonderBuilderJS.SceneConsumer> {
    public PonderBuilderJS(String name, ResourceLocation... ids) {
        super(name, Arrays.asList(ids));
    }

//    public PonderBuilderJS<T> scene(List<ResourceLocation> items, List<List<Object>> storyBoards, BiConsumer<SceneBuilder, SceneBuildingUtil> scene) {
//        return step(i -> {
//           PonderRegistry.MultiSceneBuilder multiSceneBuilder = PonderRegistry.forComponents(items.stream().map((id) -> PonderRegistryEventJS.createItemProvider(RegistryObject.of(id, ForgeRegistries.ITEMS))).collect(Collectors.toList()));
//                        multiSceneBuilder.addStoryBoard("test", scene::accept);
//        });
//    }

    public PonderBuilderJS addItem(ResourceLocation... ids) {
        items.addAll(Arrays.asList(ids));
        return this;
    }

    public PonderBuilderJS addItems(ResourceLocation... ids) {
        return addItem(ids);
    }

    public PonderBuilderJS scene(String name, String displayName, String schematic, SceneConsumer scene) {
        items.forEach(id -> addNamedStoryBoard(getName(name), displayName, id, schematic, scene));
        return this;
    }

    @Override
    protected PonderBuilderJS getSelf() {
        return this;
    }

    @Override
    protected ResourceLocation[] itemsToIdArray() {
        return items.toArray(new ResourceLocation[0]);
    }

    @Override
    protected ItemProviderEntry<?> getItemProviderEntry(ResourceLocation item) {
        return PonderMaker.createItemProvider(RegistryObject.of(item, ForgeRegistries.ITEMS));
    }

    @Override
    protected PonderStoryBoardEntry.PonderStoryBoard storyBoard(PonderBuilderJS.SceneConsumer scene) {
        return scene::run;
    }

    @Override
    protected PonderBuilderJS.SceneConsumer createConsumer(BiConsumer<SceneBuilder, SceneBuildingUtil> consumer) {
        return (b, u) -> consumer.accept(b.getInternal(), u);
    }

    @Override // expose protected method
    public PonderBuilderJS tag(String... tags) {
        return super.tag(tags);
    }

    @FunctionalInterface
    public interface SceneConsumer extends BiConsumer<SceneBuilderJS, SceneBuildingUtil> {
        default void run(SceneBuilder b, SceneBuildingUtil u) {
            accept(new SceneBuilderJS(b), u);
        }
    }
}
