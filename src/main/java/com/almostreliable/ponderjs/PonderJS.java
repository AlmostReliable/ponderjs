package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.particles.ParticleTransformation;
import com.almostreliable.ponderjs.util.BlockStateFunction;
import com.almostreliable.ponderjs.util.Util;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.ParrotElement;
import com.simibubi.create.foundation.utility.Pointing;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PonderJS {
    public static final Logger LOGGER = LogManager.getLogger(BuildConfig.MOD_ID);
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
        typeWrappers.registerSimple(Selection.class, Util::selectionOf);
        typeWrappers.registerSimple(AllIcons.class, Util::allIconsOf);
        typeWrappers.registerSimple(PonderTag.class, Util::ponderTagOf);
        typeWrappers.registerSimple(BlockState.class, Util::blockStateOf);
        typeWrappers.register(BlockStateFunction.class, BlockStateFunction::of);
        typeWrappers.registerSimple(ParticleTransformation.Data.class, ParticleTransformation.Data::of);
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
        try {
            Field f = AllIcons.class.getDeclaredField(str);
            CACHED_ICONS.put(str, (AllIcons) f.get(null));
            CACHED_ICONS.get(str);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void init() {
        if (initialized) {
            throw new IllegalStateException("Ponder has already been initialized!");
        }
        LOGGER.info("Initializing PonderJS - Run events.");
        PonderEvents.TAGS.post(new PonderItemTagEventJS());
        PonderEvents.REGISTRY.post(new PonderRegistryEventJS());
        PonderLang lang = new PonderLang();
        if (lang.generate("en_us")) {
            try {
                Minecraft.getInstance().reloadResourcePacks();
            } catch (Exception e) {
                LOGGER.error("Something went wrong while reloading resources after PonderJS init. You have to manually reload the resources for the changes to take effect.");
            }
        }

        initialized = true;
    }

    public static void reload() {
        // TODO try making PonderItemTag event reloadable
        PonderEvents.REGISTRY.post(new PonderRegistryEventJS());
        PonderLang lang = new PonderLang();
        lang.generate("en_us");
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
