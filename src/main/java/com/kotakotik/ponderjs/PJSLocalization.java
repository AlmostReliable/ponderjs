package com.kotakotik.ponderjs;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PJSLocalization {
    public static void record(List<String> namespaces, List<ResourceLocation> tags, List<Couple<String>> scenes, JsonObject json) {
        JsonObject tempJson = new JsonObject();
        for (String namespace : namespaces) {
            PonderLocalization.record(namespace, tempJson);
        }
        List<String> tagsAsIds = tags.stream().map(langKeyForTag).collect(Collectors.toList());
        List<String> tagsAsDescriptionIds = tags.stream().map(langKeyForTagDescription).collect(Collectors.toList());
        tempJson.entrySet().forEach((e) -> {
            String key = e.getKey();
            String value = e.getValue().getAsString();
            if (tagsAsIds.contains(key) || tagsAsDescriptionIds.contains(key) ||
                    scenes.stream().anyMatch(s -> sceneLangMatches(s, key))) {
                json.addProperty(key, value);
            }
        });
    }

    public static JsonObject record(List<String> namespaces, List<ResourceLocation> tags, List<Couple<String>> scenes) {
        JsonObject json = new JsonObject();
        record(namespaces, tags, scenes, json);
        return json;
    }

    // hell
//    protected static LazyValue<Map<ResourceLocation, String>> sharedField = (LazyValue<Map<ResourceLocation,java.lang.String>>)
//            PonderJS.staticFinalFieldVal(PonderLocalization.class, "SHARED");
//    protected static LazyValue<Map<ResourceLocation, Couple<String>>> tagField = (LazyValue<Map<ResourceLocation, Couple<String>>>)
//            PonderJS.staticFinalFieldVal(PonderLocalization.class, "TAG");
//    protected static LazyValue<Map<ResourceLocation, String>> chapterField = (LazyValue<Map<ResourceLocation, String>>)
//            PonderJS.staticFinalFieldVal(PonderLocalization.class, "CHAPTER");
//    protected static LazyValue<Map<ResourceLocation, Map<String, String>>> specificField = (LazyValue<Map<ResourceLocation, Map<String, String>>>)
//            PonderJS.staticFinalFieldVal(PonderLocalization.class, "SPECIFIC");
//
//    protected static Function<ResourceLocation, String> langKeyForShared =
//            PonderJS.staticOneArgMethod(PonderLocalization.class, "langKeyForShared", ResourceLocation.class);
    protected static Function<ResourceLocation, String> langKeyForTag =
            PonderJS.staticOneArgMethod(PonderLocalization.class, "langKeyForTag", ResourceLocation.class);
    protected static Function<ResourceLocation, String> langKeyForTagDescription =
            PonderJS.staticOneArgMethod(PonderLocalization.class, "langKeyForTagDescription", ResourceLocation.class);
//    protected static Function<ResourceLocation, String> langKeyForChapter =
//            PonderJS.staticOneArgMethod(PonderLocalization.class, "langKeyForChapter", ResourceLocation.class);
//    protected static LazyValue<Method> langKeyForSpecificVal = PonderJS.staticMethodVal(PonderLocalization.class, "langKeyForSpecific", ResourceLocation.class, String.class);
//    protected static BiFunction<ResourceLocation, String, String> langKeyForSpecific = (r, s) -> {
//        try {
//            return (String) langKeyForSpecificVal.get().invoke(null, r, s);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return null;
//    };

    public static String getSceneLangId(Couple<String> scene) {
        return scene.getFirst() + ".ponder." + scene.getSecond();
    }

    public static boolean sceneLangMatches(Couple<String> scene, String textKey) {
        String l = getSceneLangId(scene);
        return textKey.startsWith(l + ".text_") ||
                textKey.equals(l + ".header");
    }

//
//    public static void record(JsonObject object, List<String> sharedKeys, List<ResourceLocation> tags, List<ResourceLocation> chapters) {
//        PonderLangEventJS lang = new PonderLangEventJS();
//        lang.post(ScriptType.CLIENT, "ponder.lang");
//        sharedField.get().forEach((k, v) -> {
//            String key = langKeyForShared.apply(k);
//            if(lang.matches(key) || sharedKeys.contains(key)) {
//                object.addProperty(key, v);
//            }
//        });
//        tagField.get().forEach((k, v) -> {
//            if(tags.contains(k)) {
//                String key = langKeyForTag.apply(k);
//                if(lang.matches(key)) {
//                    object.addProperty(key, v.getFirst());
//                }
//                String key2 = langKeyForTagDescription.apply(k);
//                if(lang.matches(key2)) {
//                    object.addProperty(langKeyForTagDescription.apply(k), v.getSecond());
//                }
//            }
//        });
//        chapterField.get().forEach((k, v) -> {
//            String key = langKeyForChapter.apply(k);
//            if (lang.matches(key) || chapters.contains(k)) {
//                object.addProperty(key, v);
//            }
//
//        });
//        specificField.get().entrySet().stream().map((k) -> Pair.of(k.getKey(), langKeyForSpecific.apply(k.getVa))).filter((entry) -> {
//            return chapters.contains(entry.getKey()) || lang.matches(entry.getKe);
//        }).sorted(Map.Entry.comparingByKey()).forEach((entry) -> {
//            ((Map)entry.getValue()).entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach((subEntry) -> {
//                object.addProperty(langKeyForSpecific((ResourceLocation)entry.getKey(), (String)subEntry.getKey()), (String)subEntry.getValue());
//            });
//        });
//    }
}
