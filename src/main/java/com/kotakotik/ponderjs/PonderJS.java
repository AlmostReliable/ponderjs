package com.kotakotik.ponderjs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kotakotik.ponderjs.config.ModConfigs;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.ParrotElement;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Pointing;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.antlr.v4.runtime.misc.Triple;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class PonderJS {
    public static final String TAG_EVENT = "ponder.tag";
    public static final String REGISTRY_EVENT = "ponder.registry";
    public static final List<String> namespaces = new ArrayList<>();
    public static final List<Couple<String>> scenes = new ArrayList<>();
    public static final HashMap<String, AllIcons> cachedIcons = new HashMap<>();
    private static boolean initialized;

    static void addBindings(BindingsEvent event) {
        event.add("PonderPalette", PonderPalette.class);
        event.add("ParrotElement", ParrotElement.class);
        event.add("PonderInputWindowElement", InputWindowElement.class);
        event.add("PonderInput", InputWindowElement.class);
        event.add("PonderIcons", AllIcons.class);
        event.add("PonderPointing", Pointing.class);
    }

    static void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(Selection.class, o -> {
            if (o instanceof Selection) return (Selection) o;
            if (o instanceof AABB) {
                return Selection.of((BoundingBox) o);
            }
            if (o instanceof BlockPos) {
                return Selection.of(new BoundingBox((BlockPos) o));
            }
            return Selection.of(new BoundingBox(0, 0, 0, 0, 0, 0));
        });

        typeWrappers.register(AllIcons.class, o -> {
            if (o instanceof AllIcons) return (AllIcons) o;
            return getIconByName(o.toString());
        });

        typeWrappers.register(PonderTag.class, o -> PonderJS.getTagByName(o.toString()).orElseThrow(() -> new NoSuchElementException("No tags found matching " + o)));
    }

    public static Triple<Boolean, Component, Integer> generateJsonLang(String langName) {
        Map<String, String> langMap = createLang();
        Logger log = PonderJSMod.LOGGER;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(ModConfigs.CLIENT.getLangPath().replace("%lang%", langName));
        JsonObject json = new JsonObject();
        if (file.exists()) {
            log.info("Found KubeJS lang, reading!");
            try {
                json = gson.fromJson(FileUtils.readFileToString(file, "UTF-8"), JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JsonObject finalJson = json;
        List<String> wrote = new ArrayList<>();
        langMap.forEach((key, value) -> {
            if (!(finalJson.has(key) && finalJson.get(key).getAsString().equals(value))) {
                log.info("Writing KubeJS lang key " + key);
                finalJson.addProperty(key, value);
                wrote.add(key);
            }
        });
        String j = gson.toJson(wrote);
        try {
            FileUtils.writeStringToFile(file, gson.toJson(finalJson), "UTF-8");
            String c = "Wrote " + wrote.size() + " KubeJS lang keys";
            log.info(c);
            if (wrote.size() > 0) {
                log.info(j);
            }
            return new Triple<>(true, new TextComponent(c), wrote.size());
        } catch (IOException e) {
            log.error("Couldn't write KubeJS langMap");
            e.printStackTrace();
            return new Triple<>(false, new TextComponent("Unable to write KubeJS lang: " + e.getClass().getSimpleName() + "\nMore info in logs"), wrote.size());
        }
    }

    public static Map<String, String> createLang() {
        PonderRegistry.ALL.forEach((resourceLocation, stories) -> {
            for (int i = 0; i < stories.size(); i++) {
                var story = stories.get(i);
                if(namespaces.contains(story.getNamespace())) {
                    try {
                        PonderRegistry.compileScene(i, story,null);
                    } catch (Exception e) {
                        ConsoleJS.CLIENT.error(e);
                        e.printStackTrace();
                    }
                }
            }
        });
        Map<String, String> lang = new HashMap<>();
        JsonObject object = new JsonObject();
        PonderJS.namespaces.forEach(namespace -> PonderLocalization.record(namespace, object));
        object.entrySet().forEach(entry -> lang.put(entry.getKey(), entry.getValue().getAsString()));
        return lang;
    }

    public static Optional<PonderTag> getTagByName(ResourceLocation res) {
        return PonderRegistry.TAGS.getListedTags().stream().filter(tag -> tag.getId().equals(res)).findFirst();
    }

    protected static ResourceLocation appendNamespaceToId(String namespace, String id) {
        if (!id.contains(":")) id = namespace + ":" + id;
        return new ResourceLocation(id);
    }

    public static ResourceLocation appendCreateToId(String tag) {
        return appendNamespaceToId(Create.ID, tag);
    }

    public static ResourceLocation appendKubeToId(String id) {
        return appendNamespaceToId(KubeJS.MOD_ID, id);
    }

    public static Optional<PonderTag> getTagByName(String tag) {
        return getTagByName(appendCreateToId(tag));
    }

    public static AllIcons getIconByName(String icon) {
        String str = icon.toUpperCase();
        if (!str.startsWith("I_")) {
            str = "I_" + str;
        }
        if (cachedIcons.containsKey(str)) {
            return cachedIcons.get(str);
        }
        Field f = ObfuscationReflectionHelper.findField(AllIcons.class, str);
        try {
            cachedIcons.put(str, (AllIcons) f.get(null));
            cachedIcons.get(str);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void init() {
        if (initialized) {
            throw new IllegalStateException("Ponder has already been initialized!");
        }
        new PonderItemTagEventJS().post(ScriptType.CLIENT, TAG_EVENT);
        new PonderRegistryEventJS().post(ScriptType.CLIENT, REGISTRY_EVENT);
        generateJsonLang("en_us");
        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
