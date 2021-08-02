package com.kotakotik.pondermaker.kubejs;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.simibubi.create.foundation.ponder.elements.ParrotElement;
import dev.latvian.kubejs.BuiltinKubeJSPlugin;
import dev.latvian.kubejs.KubeJSObjects;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.client.KubeJSClientResourcePack;
import dev.latvian.kubejs.client.KubeJSResourcePackFinder;
import dev.latvian.kubejs.script.BindingsEvent;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.kubejs.util.BuilderBase;
import dev.latvian.kubejs.util.ClassFilter;
import me.shedaniel.architectury.hooks.PackRepositoryHooks;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PonderJS extends KubeJSPlugin {
    private static PonderJS INSTANCE;
    public PonderRegistryEventJS ponderEvent;

    public PonderJS() {
        INSTANCE = this;
        ponderEvent = new PonderRegistryEventJS();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ponderEvent::register);
        DeferredWorkQueue.runLater(() -> ponderEvent.post(ScriptType.CLIENT, "ponder.registry"));
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("PonderPalette", PonderPalette.class);
        event.addFunction("DancePose", ($) -> new ParrotElement.DancePose());
        super.addBindings(event);
    }

    public static PonderJS get() {
        return INSTANCE;
    }

    public static final HashMap<String, String> LANG = new HashMap<>();

    public static void addLang(String key, String value) {
        LANG.put(key, value);
    }

    public static void generatePonderLang() {
        JsonObject json = new JsonObject();
        PonderLocalization.generateSceneLang();
        PonderLocalization.record("kubejs", json);
        json.entrySet().forEach(e -> {
            // kubejs.ponder.ponder_builder.test.header
            addLang(e.getKey(), e.getValue().getAsString());
        });
        Minecraft mc = Minecraft.getInstance();
        ResourcePackList list = mc.getResourcePackRepository();
        PonderMakerResourcePack pack = new PonderMakerResourcePack();
        PackRepositoryHooks.addSource(list, pack);
//        list.reload();
//        List<IResourcePack> list1 = list.openAllSelected();
//        CompletableFuture<Unit> completablePain = CompletableFuture.completedFuture(Unit.INSTANCE);
//        ((IReloadableResourceManager) mc.getResourceManager()).createFullReload(Util.backgroundExecutor(), mc,completablePain , list1).done().complete(Unit.INSTANCE);
//        completablePain.complete(Unit.INSTANCE);
        Minecraft.getInstance().reloadResourcePacks();
//        list.reload();
//        Collection<String> s = list.getSelectedIds();
//        List<String> sl = new ArrayList<>(s);
//        sl.add(pack.id);
//        list.setSelected(sl);
//        Minecraft.getInstance().getLanguageManager().onResourceManagerReload(Minecraft.getInstance().getResourceManager());
    }

//    public static class LangBuilderJS extends BuilderBase {
//        protected static int i = 0;
//
//        protected LangBuilderJS(String key, String value) {
//            super("pondermaker_lang_" + i++);
//            translationKey = key;
//            displayName = value;
//        }
//
//        @Override
//        public String getBuilderType() {
//            return "lang";
//        }
//    }
}
