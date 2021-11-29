package com.kotakotik.ponderjs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.kotakotik.ponderjs.config.ModConfigs;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import com.simibubi.create.foundation.ponder.elements.InputWindowElement;
import com.simibubi.create.foundation.ponder.elements.ParrotElement;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Pointing;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import dev.latvian.kubejs.KubeJS;
import dev.latvian.kubejs.script.BindingsEvent;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import me.shedaniel.architectury.hooks.PackRepositoryHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.antlr.v4.runtime.misc.Triple;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PonderJS {
    public static final HashMap<String, String> LANG = new HashMap<>();
    public static final List<String> namespaces = new ArrayList<>();
    public static final List<ResourceLocation> tags = new ArrayList<>();
    public static final List<Couple<String>> scenes = new ArrayList<>();
    public static final HashMap<String, AllIcons> cachedIcons = new HashMap<>();
    public static PonderRegistryEventJS ponderEvent;
    public static PonderTagRegistryEventJS tagRegistryEvent;
    public static PonderItemTagEventJS tagItemEvent;

    static void clientPluginInit() {
        ponderEvent = new PonderRegistryEventJS();
        tagRegistryEvent = new PonderTagRegistryEventJS();
        tagItemEvent = new PonderItemTagEventJS();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ponderEvent::register);
    }

    public static IEventBus modEventBus;

    static void clientModInit() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(com.kotakotik.ponderjs.config.ModConfigs::onLoad);
        modEventBus.addListener(com.kotakotik.ponderjs.config.ModConfigs::onReload);
        com.kotakotik.ponderjs.config.ModConfigs.register();
    }

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
            if (o instanceof MutableBoundingBox) {
                return Selection.of((MutableBoundingBox) o);
            }
            if (o instanceof BlockPos) {
                return Selection.of(new MutableBoundingBox((BlockPos) o, BlockPos.ZERO));
            }
            return Selection.of(new MutableBoundingBox(0, 0, 0, 0, 0, 0));
        });
        typeWrappers.register(AllIcons.class, o -> {
            if (o instanceof AllIcons) return (AllIcons) o;
            return getIconByName(o.toString());
        });
    }

    public static Triple<Boolean, ITextComponent, Integer> generateJsonLang(HashMap<String, String> from) {
        Logger log = PonderJSMod.LOGGER;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File(ModConfigs.CLIENT.getLangPath());
        JsonObject json = new JsonObject();
        if (file.exists()) {
            log.info("Found KubeJS lang, reading!");
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
            return new Triple<>(true, new StringTextComponent(c), wrote.size());
        } catch (IOException e) {
            log.error("Couldn't write KubeJS lang");
            e.printStackTrace();
            return new Triple<>(false, new StringTextComponent("Unable to write KubeJS lang: " + e.getClass().getSimpleName() + "\nMore info in logs"), wrote.size());
        }
    }

    public static void addLang(String key, String value) {
        LANG.put(key, value);
    }

    public static JsonObject getKubeJSAssetLang(Gson g) {
        JsonObject assetLang = new JsonObject();
        try {
            Reader reader = new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(
                    new ResourceLocation("kubejs", "lang/" + ModConfigs.CLIENT.lang.get() + ".json")).getInputStream(), StandardCharsets.UTF_8);
            assetLang = g.getAdapter(JsonObject.class).read(new JsonReader(reader));
        } catch(FileNotFoundException ignored) {

        } catch(IOException e) {
            e.printStackTrace();
        }
        return assetLang;
    }

    public static void fillPonderLang(Gson gson) {
        LANG.clear();
        JsonObject json = new JsonObject();
        PonderLocalization.generateSceneLang();
        PJSLocalization.record(namespaces, tags, scenes, json);

//        JsonObject assetLang = ((KubeJSClientResourcePack) mc.getResourcePackRepository().getPack("kubejs:resource_pack").open())
//                .getCachedResources().get(new ResourceLocation("kubejs", "lang/en_us")).getAsJsonObject();
        JsonObject assetLang = getKubeJSAssetLang(gson);
        json.entrySet().forEach(e -> {
            // kubejs.ponder.ponder_builder.test.header
            if (!assetLang.has(e.getKey()) || !assetLang.get(e.getKey()).equals(e.getValue())) {
                String key = e.getKey();
                String val = e.getValue().getAsString();
                addLang(key, val);
            }
        });
    }

    public static void fillPonderLang() {
        fillPonderLang(new GsonBuilder().setPrettyPrinting().create());
    }

    public static void generatePonderLang() {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        Minecraft mc = Minecraft.getInstance();
        fillPonderLang(g);
        if (LANG.size() > 0) {
            PonderJSMod.LOGGER.warn("Found missing ponder lang, registering resource pack and reloading" +
                    "\nMissing: " + g.toJson(LANG) +
                    "\nKeys only: " + g.toJson(LANG.keySet()));
            ResourcePackList list = mc.getResourcePackRepository();
            PonderJSResourcePack pack = new PonderJSResourcePack();
            PackRepositoryHooks.addSource(list, pack);
            Minecraft.getInstance().reloadResourcePacks();
        } else {
            PonderJSMod.LOGGER.info("No ponder lang missing, skipping resource pack registration");
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

    public static <T extends IForgeRegistryEntry<? super T> & IItemProvider> ItemProviderEntry<T> createItemProvider(RegistryObject<T> item) {
        return new ItemProviderEntry<>(Create.registrate(), item);
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
}
