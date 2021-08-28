package com.kotakotik.ponderjs.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.script.data.KubeJSResourcePack;
import net.minecraft.resources.IPackFinder;
import net.minecraft.resources.IPackNameDecorator;
import net.minecraft.resources.ResourcePackInfo;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.Map;
import java.util.function.Consumer;

public class PonderJSResourcePack implements IPackFinder {
    public String id = "ponderjs:resource_pack";

    @Override
    public void loadPacks(Consumer<ResourcePackInfo> nameToPackMap, ResourcePackInfo.IFactory packInfoFactory) {
        PonderJSClientPack pack = new PonderJSClientPack();
        PackMetadataSection metadataSection = new PackMetadataSection(new StringTextComponent("./ponderjs/assets/"), 6);
        nameToPackMap.accept(new ResourcePackInfo(id, true, () -> pack, pack, metadataSection, ResourcePackInfo.Priority.TOP, IPackNameDecorator.BUILT_IN));
    }

    public static class PonderJSClientPack extends KubeJSResourcePack {
        public PonderJSClientPack() {
            super(ResourcePackType.CLIENT_RESOURCES);
        }

        boolean r = false;

        @Override
        public void generateJsonFiles(Map<ResourceLocation, JsonElement> map) {
            // kubejs.ponder.ponder_builder.test.header
            JsonObject lang = new JsonObject();

            for(String key : PonderJSPlugin.LANG.keySet()) {
                lang.addProperty(key, PonderJSPlugin.LANG.get(key));
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
