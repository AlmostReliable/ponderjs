package com.kotakotik.ponderjs.mixin;

import com.google.common.collect.Multimap;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.PonderTagRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PonderTagRegistry.class)
public interface PonderTagRegistryAccessor {

    @Accessor(value = "tags", remap = false)
    Multimap<ResourceLocation, PonderTag> getTags();
}
