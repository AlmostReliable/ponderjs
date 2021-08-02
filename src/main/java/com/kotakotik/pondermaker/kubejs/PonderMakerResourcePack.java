package com.kotakotik.pondermaker.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.KubeJSObjects;
import dev.latvian.kubejs.client.KubeJSClientResourcePack;
import dev.latvian.kubejs.script.data.KubeJSResourcePack;
import dev.latvian.kubejs.util.BuilderBase;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.*;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Consumer;

public class PonderMakerResourcePack implements IPackFinder {
    public String id = "pondermaker:resource_pack";

    @Override
    public void loadPacks(Consumer<ResourcePackInfo> nameToPackMap, ResourcePackInfo.IFactory packInfoFactory) {
        PonderMakerClientPack pack = new PonderMakerClientPack();
        PackMetadataSection metadataSection = new PackMetadataSection(new StringTextComponent("./pondermaker/assets/"), 6);
        nameToPackMap.accept(new ResourcePackInfo(id, true, () -> pack, pack, metadataSection, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILT_IN));
    }

    public static class PonderMakerClientPack extends KubeJSResourcePack {
        public PonderMakerClientPack() {
            super(ResourcePackType.CLIENT_RESOURCES);
        }

        boolean r = false;

        @Override
        public void generateJsonFiles(Map<ResourceLocation, JsonElement> map) {
            // kubejs.ponder.ponder_builder.test.header
            JsonObject lang = new JsonObject();

            for(String key : PonderJS.LANG.keySet()) {
                lang.addProperty(key, PonderJS.LANG.get(key));
            }
//            for (BuilderBase builder : KubeJSObjects.ALL) {
//                if (builder instanceof PonderJS.LangBuilderJS && !builder.displayName.isEmpty()) {
//                    lang.addProperty(builder.translationKey, builder.displayName);
//                }
//            }
            map.put(new ResourceLocation(KubeJS.MOD_ID, "lang/en_us"), lang);
            super.generateJsonFiles(map);
        }

        @Override
        public Map<ResourceLocation, JsonElement> getCachedResources() {
            return super.getCachedResources();
        }
    }
}
