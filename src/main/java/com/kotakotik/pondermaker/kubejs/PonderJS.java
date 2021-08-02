package com.kotakotik.pondermaker.kubejs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.kotakotik.pondermaker.PonderMaker;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.simibubi.create.foundation.ponder.elements.ParrotElement;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.client.KubeJSClientResourcePack;
import dev.latvian.kubejs.script.BindingsEvent;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.kubejs.util.ClassFilter;
import me.shedaniel.architectury.hooks.PackRepositoryHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.codec.language.bm.Languages;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PonderJS extends KubeJSPlugin {
    private static PonderJS INSTANCE;
    public PonderRegistryEventJS ponderEvent;

    static boolean posted = false;

    public PonderJS() {
        if(INSTANCE != null) return;
        INSTANCE = this;
        ponderEvent = new PonderRegistryEventJS();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ponderEvent::register);
        DeferredWorkQueue.runLater(() -> {
            if(!posted) {
                ponderEvent.post(ScriptType.STARTUP, "ponder.registry");
            }
            posted = true;
        });
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("PonderPalette", PonderPalette.class);
        event.addFunction("DancePose", ($) -> new ParrotElement.DancePose());
        super.addBindings(event);
    }

    @Override
    public void addClasses(ScriptType type, ClassFilter filter) {
        filter.allow(Entity.class);
        super.addClasses(type, filter);
    }

    public static PonderJS get() {
        return INSTANCE;
    }

    public static final HashMap<String, String> LANG = new HashMap<>();

    public static void addLang(String key, String value) {
        LANG.put(key, value);
    }

    public static JsonObject getKubeJSAssetLang(Gson g) {
        JsonObject assetLang = new JsonObject();
        try {
            Reader reader = new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("kubejs", "lang/en_us.json")).getInputStream(), StandardCharsets.UTF_8);
            assetLang = g.getAdapter(JsonObject.class).read(new JsonReader(reader));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assetLang;
    }

    public static void generatePonderLang() {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        Minecraft mc = Minecraft.getInstance();
        JsonObject json = new JsonObject();
        PonderLocalization.generateSceneLang();
        PonderLocalization.record("kubejs", json);

//        JsonObject assetLang = ((KubeJSClientResourcePack) mc.getResourcePackRepository().getPack("kubejs:resource_pack").open())
//                .getCachedResources().get(new ResourceLocation("kubejs", "lang/en_us")).getAsJsonObject();
        JsonObject assetLang = getKubeJSAssetLang(g);
        json.entrySet().forEach(e -> {
            // kubejs.ponder.ponder_builder.test.header
                if(!assetLang.has(e.getKey())) {
                    String key = e.getKey();
                    String val = e.getValue().getAsString();
                    addLang(key, val);
                }
        });
        if(LANG.size() > 0) {
            PonderMaker.LOGGER.warn("Found missing ponder lang, registering resource pack and reloading" +
                    "\nMissing: " + g.toJson(LANG) +
                    "\nKeys only: " + g.toJson(LANG.keySet()));
            ResourcePackList list = mc.getResourcePackRepository();
            PonderMakerResourcePack pack = new PonderMakerResourcePack();
            PackRepositoryHooks.addSource(list, pack);
            Minecraft.getInstance().reloadResourcePacks();
        } else {
            PonderMaker.LOGGER.info("No ponder lang missing, skipping resource pack registration");
        }

//        list.reload();
//        List<IResourcePack> list1 = list.openAllSelected();
//        CompletableFuture<Unit> completablePain = CompletableFuture.completedFuture(Unit.INSTANCE);
//        ((IReloadableResourceManager) mc.getResourceManager()).createFullReload(Util.backgroundExecutor(), mc,completablePain , list1).done().complete(Unit.INSTANCE);
//        completablePain.complete(Unit.INSTANCE);
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
