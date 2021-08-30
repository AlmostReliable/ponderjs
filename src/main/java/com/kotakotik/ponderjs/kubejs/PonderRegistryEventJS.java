package com.kotakotik.ponderjs.kubejs;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kotakotik.ponderjs.config.ModConfigs;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.event.EventJS;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.antlr.v4.runtime.misc.Triple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PonderRegistryEventJS extends EventJS {
    public static final List<String> namespaces = new ArrayList<>();

    static {
        namespaces.add(KubeJS.MOD_ID);
    }

    public PonderBuilderJS create(String name, Object items) {
        return new PonderBuilderJS(name, ListJS.orSelf(items));
    }

    public void addNamespace(Object namespace) {
        namespaces.addAll(ListJS.orSelf(namespace).stream().map(Objects::toString).filter((s) -> !namespaces.contains(s)).collect(Collectors.toList()));
    }

    public void addNamespaces(Object namespace) {
        addNamespace(namespace);
    }

    public static void rerunScripts(ScriptType scriptType, String tagRegistry, String tagItem, String ponder, PonderJSPlugin mainJS) {
        mainJS.tagRegistryEvent.post(scriptType, tagRegistry);
        mainJS.tagItemEvent.post(scriptType, tagItem);
        mainJS.ponderEvent.post(scriptType, ponder);
    }

    public static void rerunScripts() {
        rerunScripts(ScriptType.CLIENT, "ponder.tag.registry", "ponder.tag", "ponder.registry", PonderJSPlugin.get());
    }

    public static void regenerateLangIntoFile() {
        JsonObject json = new JsonObject();
        PonderLocalization.generateSceneLang();
        for (String namespace : namespaces) {
            PonderLocalization.record(namespace, json);
        }
        Triple<Boolean, ITextComponent, Integer> result = PonderJSPlugin.generateJsonLang(new Gson().fromJson(json, HashMap.class));
        boolean success = result.a;
        int count = result.c;
        if (success) {
            if (count > 0) {
                KubeJS.PROXY.reloadLang();
                if (!rerun) {
                    Minecraft.getInstance().reloadResourcePacks();
                }
            }
        } else {
            PonderJSPlugin.generatePonderLang();
        }
    }

    public static void regenerateLang() {
            if(ModConfigs.CLIENT.autoGenerateLang.get()) {
                regenerateLangIntoFile();
            } else {
                PonderJSPlugin.generatePonderLang();
            }
    }

    protected static boolean rerun = false;

    public static void runAllRegistration() {
        PonderTagRegistryEventJS.rerun = rerun;
        PonderItemTagEventJS.rerun = rerun;
        rerunScripts();
        regenerateLang();
        rerun = true;
    }

    public void register(FMLClientSetupEvent event) {
//                PonderRegistry.forComponents(itemProvider)
//                        .addStoryBoard("test", b.function::accept);
//                PonderRegistry.TAGS.forTag(PonderTag.KINETIC_RELAYS)
//                        .add(itemProvider);
        event.enqueueWork(() -> {
            try {
                runAllRegistration();
            } catch (Exception e) { // i think theres a way to do this with the completable future but this is easier
                e.printStackTrace();
            }
        });
    }

}
