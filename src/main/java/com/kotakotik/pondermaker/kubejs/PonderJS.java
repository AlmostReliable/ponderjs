package com.kotakotik.pondermaker.kubejs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.kotakotik.pondermaker.PonderMaker;
import com.kotakotik.pondermaker.kubejs.util.DyeColorWrapper;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.simibubi.create.foundation.ponder.elements.ParrotElement;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.script.BindingsEvent;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.kubejs.util.ClassFilter;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import me.shedaniel.architectury.hooks.PackRepositoryHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.antlr.v4.runtime.misc.Triple;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PonderJS extends KubeJSPlugin {
    private static PonderJS INSTANCE;
    public PonderRegistryEventJS ponderEvent;
    public PonderTagRegistryEventJS tagRegistryEvent;
    public PonderItemTagEventJS tagItemEvent;

    public PonderJS() {
        INSTANCE = this;
        ponderEvent = new PonderRegistryEventJS();
        tagRegistryEvent = new PonderTagRegistryEventJS();
        tagItemEvent = new PonderItemTagEventJS();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ponderEvent::register);
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.addClass("PonderPalette", PonderPalette.class);
        event.addFunction("DancePose", ($) -> new ParrotElement.DancePose());
        if(event.type == ScriptType.STARTUP) {
            event.add("pondersettings", Settings.instance);
        }
        event.addClass("DyeColor", DyeColorWrapper.class);
        event.addClass("ParrotElement", ParrotElement.class);
        event.addClass("Direction", Direction.class); // ik about the facing wrapper, i just prefer to call it direction
//        event.addClass("ParrotElement.FacePointOfInterestPose", ParrotElement.FacePointOfInterestPose.class);
        super.addBindings(event);
    }

    public static Triple<Boolean, ITextComponent, Integer> generateJsonLang(HashMap<String, String> from) {
        Logger log = PonderMaker.LOGGER;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File("kubejs/assets/kubejs/lang/en_us.json");
        JsonObject json = new JsonObject();
        if(file.exists()) {
            log.info("Found KubeJS en_us.json, reading!");
//            Files.writeString(file.toPath(), "", Charset.defaultCharset(), StandardOpenOption.WRITE);
//            file.
            try {
//                FileInputStream i = new FileInputStream(file.getAbsolutePath());
                json = gson.fromJson(FileUtils.readFileToString(file, "UTF-8"), JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            json = gson.fromJson(Files.readString(file.getPath()));
        }
        JsonObject finalJson = json;
        List<String> wrote = new ArrayList<>();
        from.forEach((key, value) -> {
            if(!(finalJson.has(key) && finalJson.get(key).getAsString().equals(value))) {
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
            if(wrote.size() > 0) {
                log.info(j);
            }
            return new Triple<>(true, new StringTextComponent(c), wrote.size());
        } catch (IOException e) {
            log.error("Couldn't write KubeJS lang");
            e.printStackTrace();
            return new Triple<>(false, new StringTextComponent("Unable to write KubeJS lang: " + e.getClass().getSimpleName() + "\nMore info in logs"), wrote.size());
        }
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


    @Override
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(Vector3d.class, o -> {
            if (o instanceof Vector3d) {
                return (Vector3d) o;
            } else if (o instanceof EntityJS) {
                return ((EntityJS) o).minecraftEntity.position();
            } else if (o instanceof List && ((List<?>) o).size() >= 3) {
                return new Vector3d(((Number) ((List<?>) o).get(0)).doubleValue(), ((Number) ((List<?>) o).get(1)).doubleValue(), ((Number) ((List<?>) o).get(2)).doubleValue());
            } else if (o instanceof BlockPos) {
                BlockPos bp = (BlockPos) o;
                return new Vector3d(bp.getX() + .5, bp.getY() + .5, bp.getZ() + .5);
            }

            return Vector3d.ZERO;
        });
        typeWrappers.register(Selection.class, o -> {
            if(o instanceof Selection) return (Selection) o;
            if(o instanceof MutableBoundingBox) {
                return Selection.of((MutableBoundingBox) o);
            }
            if(o instanceof BlockPos) {
                return Selection.of(new MutableBoundingBox((BlockPos) o, BlockPos.ZERO));
            }
            return Selection.of(new MutableBoundingBox(0, 0, 0, 0,0, 0));
        });
    }

    public static class Settings {
        public static Settings instance = new Settings();

        public boolean autoGenerateLang = true;
    }
}
