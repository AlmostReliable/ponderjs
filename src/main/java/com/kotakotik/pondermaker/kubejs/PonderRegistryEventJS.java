package com.kotakotik.pondermaker.kubejs;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kotakotik.pondermaker.PonderMaker;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.content.PonderIndex;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import com.simibubi.create.repack.registrate.util.NonNullLazyValue;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import com.simibubi.create.repack.registrate.util.nullness.NonnullType;
import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.command.KubeJSCommands;
import dev.latvian.kubejs.event.EventJS;
import dev.latvian.kubejs.forge.KubeJSForge;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.antlr.v4.runtime.misc.Triple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PonderRegistryEventJS extends EventJS {
    public static ArrayList<PonderBuilderJS<?>> ALL = new ArrayList<>();

    public PonderBuilderJS<?> create(String name, ResourceLocation... items) {
        PonderBuilderJS<?> b = new PonderBuilderJS<>(name, items);
        ALL.add(b);
        return b;
    }

    public void register(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            PonderRegistry.startRegistration("kubejs");
            for(PonderBuilderJS<?> b : ALL) {
                b.execute();
//                PonderRegistry.forComponents(itemProvider)
//                        .addStoryBoard("test", b.function::accept);
//                PonderRegistry.TAGS.forTag(PonderTag.KINETIC_RELAYS)
//                        .add(itemProvider);
            }
            PonderRegistry.endRegistration();
            if(PonderJS.Settings.instance.autoGenerateLang) {
                JsonObject json = new JsonObject();
                PonderLocalization.generateSceneLang();
                PonderLocalization.record("kubejs", json);
                Triple<Boolean, ITextComponent, Integer> result = PonderJS.generateJsonLang(new Gson().fromJson(json, HashMap.class));
                boolean success = result.a;
                int count = result.c;
                if(success) {
                    if(count > 0) {
                        KubeJS.PROXY.reloadLang();
                        Minecraft.getInstance().reloadResourcePacks();
                    }
                } else {
                    PonderJS.generatePonderLang();
                }
            } else {
                PonderJS.generatePonderLang();
            }
        });
    }

    public static <T extends IForgeRegistryEntry<? super T> & IItemProvider> ItemProviderEntry<T> createItemProvider(RegistryObject<T> item) {
        return new ItemProviderEntry<>(Create.registrate(), item);
    }
}
