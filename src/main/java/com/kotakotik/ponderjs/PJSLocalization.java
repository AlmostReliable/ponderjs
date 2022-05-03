package com.kotakotik.ponderjs;

import com.google.gson.JsonObject;
import com.kotakotik.ponderjs.mixin.PonderLocalizationAccessor;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PJSLocalization {
    public static void record(List<String> namespaces, List<ResourceLocation> tags, List<Couple<String>> scenes, JsonObject json) {
        JsonObject tempJson = new JsonObject();
        for (String namespace : namespaces) {
            PonderLocalization.record(namespace, tempJson);
        }
        List<String> tagsAsIds = tags.stream().map(PonderLocalizationAccessor::langKeyForTag).toList();
        List<String> tagsAsDescriptionIds = tags.stream().map(PonderLocalizationAccessor::langKeyForTagDescription).toList();
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

    public static String getSceneLangId(Couple<String> scene) {
        return scene.getFirst() + ".ponder." + scene.getSecond();
    }

    public static boolean sceneLangMatches(Couple<String> scene, String textKey) {
        String l = getSceneLangId(scene);
        return textKey.startsWith(l + ".text_") ||
                textKey.equals(l + ".header");
    }
}
