package com.kotakotik.ponderjs;

import com.kotakotik.ponderjs.config.ModConfigs;
import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.event.EventJS;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.antlr.v4.runtime.misc.Triple;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PonderRegistryEventJS extends EventJS {
    public PonderBuilderJS create(String name, Object items) {
        return new PonderBuilderJS(name, ListJS.orSelf(items));
    }

    public static void rerunScripts(ScriptType scriptType, String tagRegistry, String tagItem, String ponder) {
        if (tagRegistry != null) {
            PonderJS.tagRegistryEvent.post(scriptType, tagRegistry);
        }
        if (tagItem != null) {
            PonderJS.tagItemEvent.post(scriptType, tagItem);
        }
        if (ponder != null) {
            PonderJS.ponderEvent.post(scriptType, ponder);
        }
    }

    public static void rerunScripts() {
        rerunScripts(ScriptType.CLIENT, "ponder.tag.registry", "ponder.tag", "ponder.registry");
    }

    public static void regenerateLangIntoFile() {
//        JsonObject json = new JsonObject();
//        PonderLocalization.generateSceneLang();
        PonderJS.fillPonderLang();
//        PJSLocalization.record(PonderJSPlugin.namespaces,);
        Triple<Boolean, ITextComponent, Integer> result = PonderJS.generateJsonLang(PonderJS.LANG);
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
            PonderJS.generatePonderLang();
        }
    }

    public static void regenerateLang() {
            if(ModConfigs.CLIENT.autoGenerateLang.get()) {
                regenerateLangIntoFile();
            } else {
                PonderJS.generatePonderLang();
            }
    }

    public static boolean rerun = false;

    public static void runAllRegistration() {
        if (rerun) {
            rerunScripts(ScriptType.CLIENT, null, null, "ponder.registry");
        } else {
            rerunScripts();
        }
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
                ScriptType.CLIENT.console.error("Error occurred while running ponder events", e);
            }
        });
    }

}
