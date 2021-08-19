package com.kotakotik.pondermaker.crafttweaker;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.kotakotik.pondermaker.PonderMaker;
import com.kotakotik.pondermaker.common.AbstractPonderBuilder;
import com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.SceneBuilderWrapper;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

@ZenRegister(modDeps = {"create"})
@ZenCodeType.Name("mods.pondermaker.PonderBuilder")
public class PonderBuilder extends
        AbstractPonderBuilder<IItemStack, PonderBuilder, BiConsumer<SceneBuilderWrapper, SceneBuildingUtil>>{
    @ZenCodeType.Constructor
    public PonderBuilder(String name, List<IItemStack> items) {
        super(name, items);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }

    @Override
    @ZenCodeType.Method
    public String toString() {
        return "PonderBuilder{" +
                "name='" + name + '\'' +
                ", items=" + items +
                ", tags=" + t +
                '}';
    }

    @ZenCodeType.Method("register")
    public PonderBuilder register() {
        PonderTweaker.addFunctionToRegister(function);
        function = () -> {};
        return this;
    }

    @Override
    public PonderBuilder getSelf() {
        return this;
    }

    @Override
    protected ResourceLocation[] itemsToIdArray() {
        return items.stream().map(IItemStack::getRegistryName).toArray(ResourceLocation[]::new);
    }

    @Override
    protected ItemProviderEntry<?> getItemProviderEntry(IItemStack item) {
        return PonderMaker.createItemProvider(RegistryObject.of(item.getRegistryName(), ForgeRegistries.ITEMS));
    }

    @Override
    protected void programStoryBoard(BiConsumer<SceneBuilderWrapper, SceneBuildingUtil> scene, SceneBuilder builder, SceneBuildingUtil util) {
       scene.accept(new SceneBuilderWrapper(builder), util);
    }

//    @Override
//    protected PonderStoryBoardEntry.PonderStoryBoard storyBoard(BiConsumer<SceneBuilderWrapper, SceneBuildingUtil> scene) {
//        return (a, b) -> scene.accept(new SceneBuilderWrapper(a), b);
//    }

    @Override
    protected BiConsumer<SceneBuilderWrapper, SceneBuildingUtil> createConsumer(BiConsumer<SceneBuilder, SceneBuildingUtil> consumer) {
        return (a, b) -> consumer.accept(a.getInternal(), b);
    }

    List<String> t = new ArrayList<>();

    @ZenCodeType.Method
    @Override
    protected PonderBuilder tag(String... tags) {
        t.addAll(Arrays.asList(tags));
        return super.tag(tags);
    }

    @ZenCodeType.Method
    public PonderBuilder scene(String name, String displayName, String schematic, BiConsumer<SceneBuilderWrapper, SceneBuildingUtil> scene) {
        items.forEach(id -> addNamedStoryBoard(getName(name), displayName, id, schematic, (b, u) -> programStoryBoard(scene, b, u)));
        return this;
    }

    //    @ZenCodeType.Method
//    public PonderBuilder tag(String... tags) {
//        return step($ -> {
//            for(String tag : tags) {
//                if(!tag.contains(":")) tag = "create:" + tag;
//                ResourceLocation id = new ResourceLocation(tag);
//                PonderRegistry.TAGS.forItems(items.stream().map(IItemStack::getRegistryName).toArray(ResourceLocation[]::new))
//                        .add(PonderMaker.getTagByName(id).orElse(PonderTag.CREATIVE));
//            }
//        });
//    }
}
