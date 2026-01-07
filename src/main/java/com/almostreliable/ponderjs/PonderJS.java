package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.mixin.PonderIndexAccessor;
import com.almostreliable.ponderjs.mixin.PonderTagRegistryAccessor;
import dev.latvian.mods.kubejs.KubeJS;
import net.createmod.ponder.foundation.PonderTag;
import net.createmod.ponder.foundation.registration.PonderTagRegistry;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PonderJS {
    public static final PonderJSPlugin PLUGIN = new PonderJSPlugin();
    public static final Logger LOGGER = LogManager.getLogger(BuildConfig.MOD_ID);
    @Nullable public static final String TAG_EVENT = "ponder.tag";
    public static final String REGISTRY_EVENT = "ponder.registry";
    //    public static final HashMap<String, AllIcons> CACHED_ICONS = new HashMap<>();
    public static boolean ON_RELOAD = false;

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
        return ResourceLocation.parse(id);
    }

    public static Optional<PonderTag> getTagByName(String tag) {
        var rl = ResourceLocation.tryParse(tag);
        if (rl == null) {
            throw new IllegalArgumentException("Given tag is not a valid resource location: " + tag);
        }

        return getTagByName(rl);
    }
}
