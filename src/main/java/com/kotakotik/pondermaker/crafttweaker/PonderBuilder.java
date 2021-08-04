package com.kotakotik.pondermaker.crafttweaker;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.kotakotik.pondermaker.PonderMaker;
import com.kotakotik.pondermaker.common.AbstractPonderBuilder;
import com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.SceneBuilderWrapper;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.util.TriConsumer;
import org.openzen.zencode.java.ZenCodeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ZenRegister(modDeps = {"create"})
@ZenCodeType.Name("mods.pondermaker.PonderBuilder")
public class PonderBuilder extends
        AbstractPonderBuilder<IItemStack, PonderBuilder, TriConsumer<SceneBuilderWrapper, SceneBuildingUtil, Unit>, Unit>{
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
        function = ($) -> {};
        return this;
    }

    @Override
    protected PonderBuilder getSelf() {
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
    protected PonderStoryBoardEntry.PonderStoryBoard storyBoard(TriConsumer<SceneBuilderWrapper, SceneBuildingUtil, Unit> scene) {
        return (a, b) -> scene.accept(new SceneBuilderWrapper(a), b, Unit.INSTANCE);
    }

    @Override
    protected TriConsumer<SceneBuilderWrapper, SceneBuildingUtil, Unit> createConsumer(TriConsumer<SceneBuilder, SceneBuildingUtil, Unit> consumer) {
        return (a, b, c) -> consumer.accept(a.getInternal(), b, c);
    }

    List<String> t = new ArrayList<>();

    @ZenCodeType.Method
    @Override
    protected PonderBuilder tag(String... tags) {
        t.addAll(Arrays.asList(tags));
        return super.tag(tags);
    }

    @ZenCodeType.Method
    public PonderBuilder scene(String name, String displayName, String schematic, TriConsumer<SceneBuilderWrapper, SceneBuildingUtil, Unit> scene) {
        items.forEach(id -> addNamedStoryBoard(getName(name), displayName, id, schematic, scene));
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
