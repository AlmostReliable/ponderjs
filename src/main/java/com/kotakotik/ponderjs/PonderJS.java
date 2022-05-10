package com.kotakotik.ponderjs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kotakotik.ponderjs.config.ModConfigs;
import com.kotakotik.ponderjs.util.BlockStateFunction;
import com.kotakotik.ponderjs.util.BlockStateSupplier;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.ponder.*;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.ParrotElement;
import com.simibubi.create.foundation.utility.Pointing;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.antlr.v4.runtime.misc.Triple;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class PonderJS {
    @Nullable public static final String TAG_EVENT = "ponder.tag";
    public static final String REGISTRY_EVENT = "ponder.registry";
    public static final Set<String> NAMESPACES = new HashSet<>();
    public static final HashMap<String, AllIcons> CACHED_ICONS = new HashMap<>();
    public static final PonderStoriesManager STORIES_MANAGER = new PonderStoriesManager();
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
            if (o instanceof Selection s) return s;
            if (o instanceof BoundingBox box) {
                return Selection.of(box);
            }

            if (o instanceof BlockPos b) {
                return Selection.of(new BoundingBox(b));
            }

            if (o instanceof List<?> l) {
                Integer[] values = l.stream().map(entry -> UtilsJS.parseInt(entry, 0)).toArray(Integer[]::new);
                if (values.length == 6) {
                    return Selection.of(new BoundingBox(values[0],
                            values[1],
                            values[2],
                            values[3],
                            values[4],
                            values[5]));
                }
                if (values.length == 3) {
                    return Selection.of(new BoundingBox(values[0],
                            values[1],
                            values[2],
                            values[0],
                            values[1],
                            values[2]));
                }
                if (values.length == 2) {
                    // TODO add type wrappers for blockpos and vec3
                }
            }

            if (Context.jsToJava(o, Vec3.class) instanceof Vec3 v) {
                // TODO use type wrapper for ve3
                return Selection.of(new BoundingBox(new BlockPos(v.x, v.y, v.z)));
            }

            ConsoleJS.CLIENT.error("Invalid selection: " + o);
            return Selection.of(new BoundingBox(0, 0, 0, 0, 0, 0));
        });

        typeWrappers.register(AllIcons.class, o -> {
            if (o instanceof AllIcons) return (AllIcons) o;
            return getIconByName(o.toString());
        });

        typeWrappers.register(PonderTag.class, o -> {
            PonderTag ponderTag = PonderJS.getTagByName(o.toString()).orElse(null);
            if (ponderTag == null) {
                IllegalArgumentException e = new IllegalArgumentException("Invalid PonderTag: " + o);
                PonderErrorHelper.yeet(e);
                throw e;
            }
            return ponderTag;
        });
        typeWrappers.register(BlockStateFunction.class, BlockStateFunction::of);
        typeWrappers.register(BlockStateSupplier.class, BlockStateSupplier::of);
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
            return new Triple<>(false,
                    new TextComponent(
                            "Unable to write KubeJS lang: " + e.getClass().getSimpleName() + "\nMore info in logs"),
                    wrote.size());
        }
    }

    public static Map<String, String> createLang() {
        STORIES_MANAGER.compileLang();
        Map<String, String> lang = new HashMap<>();
        JsonObject object = new JsonObject();
        PonderJS.NAMESPACES.forEach(namespace -> PonderLocalization.record(namespace, object));
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
        if (CACHED_ICONS.containsKey(str)) {
            return CACHED_ICONS.get(str);
        }
        Field f = ObfuscationReflectionHelper.findField(AllIcons.class, str);
        try {
            CACHED_ICONS.put(str, (AllIcons) f.get(null));
            CACHED_ICONS.get(str);
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
        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
