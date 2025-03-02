package com.almostreliable.ponderjs.mixin;

import com.google.common.collect.Multimap;
import net.createmod.ponder.foundation.PonderTag;
import net.createmod.ponder.foundation.registration.PonderLocalization;
import net.createmod.ponder.foundation.registration.PonderTagRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(PonderTagRegistry.class)
public interface PonderTagRegistryAccessor {

    @Accessor(value = "MISSING", remap = false)
    PonderTag getMissing();

    @Accessor(value = "localization", remap = false)
    PonderLocalization getLocalization();

    @Accessor(value = "componentTagMap", remap = false)
    Multimap<ResourceLocation, ResourceLocation> getComponentTagMap();

    @Accessor(value = "registeredTags", remap = false)
    Map<ResourceLocation, PonderTag> getRegisteredTags();

    @Accessor(value = "listedTags", remap = false)
    List<PonderTag> getListedTags();
}
