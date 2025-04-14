package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.mixin.PonderIndexAccessor;
import com.almostreliable.ponderjs.mixin.PonderTagRegistryAccessor;
import com.almostreliable.ponderjs.particles.ParticleTransformation;
import com.almostreliable.ponderjs.util.BlockStateFunction;
import com.almostreliable.ponderjs.util.Util;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ParrotElement;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.PonderTag;
import net.createmod.ponder.foundation.element.InputWindowElement;
import net.createmod.ponder.foundation.registration.PonderTagRegistry;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PonderJS {
    public static final PonderJSPlugin PLUGIN = new PonderJSPlugin();
    public static final Logger LOGGER = LogManager.getLogger(BuildConfig.MOD_ID);
    @Nullable public static final String TAG_EVENT = "ponder.tag";
    public static final String REGISTRY_EVENT = "ponder.registry";
    public static final Set<String> NAMESPACES = new HashSet<>();
    //    public static final HashMap<String, AllIcons> CACHED_ICONS = new HashMap<>();
    public static final PonderStoriesManager STORIES_MANAGER = new PonderStoriesManager();
    @Nullable public static Language DEFAULT_LANGUAGE;
    public static boolean ON_RELOAD = false;


    static void addBindings(BindingsEvent event) {
        event.add("PonderPalette", PonderPalette.class);
        event.add("ParrotElement", ParrotElement.class);
        event.add("PonderInputWindowElement", InputWindowElement.class);
        event.add("PonderInput", InputWindowElement.class);
        event.add("PonderPointing", Pointing.class);
    }

    static void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(Selection.class, Util::selectionOf);
        typeWrappers.registerSimple(PonderTag.class, Util::ponderTagOf);
        typeWrappers.registerSimple(BlockState.class, Util::blockStateOf);
        typeWrappers.register(BlockStateFunction.class, BlockStateFunction::of);
        typeWrappers.registerSimple(ParticleTransformation.Data.class, ParticleTransformation.Data::of);
    }

    public static Optional<PonderTag> getTagByName(ResourceLocation res) {
        return getTagRegistryAccessor().getListedTags().stream().filter(tag -> tag.getId().equals(res)).findFirst();
    }

    public static PonderTagRegistryAccessor getTagRegistryAccessor() {
        return (PonderTagRegistryAccessor) (PonderIndexAccessor.getTags());
    }

    public static PonderTagRegistry getTagRegistry() {
        return PonderIndexAccessor.getTags();
    }

    public static ResourceLocation appendKubeToId(String id) {
        if (!id.contains(":")) id = KubeJS.MOD_ID + ":" + id;
        return new ResourceLocation(id);
    }

    public static Optional<PonderTag> getTagByName(String tag) {
        var rl = ResourceLocation.tryParse(tag);
        if (rl == null) {
            throw new IllegalArgumentException("Given tag is not a valid resource location: " + tag);
        }

        return getTagByName(rl);
    }
}
