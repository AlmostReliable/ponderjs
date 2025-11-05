package com.almostreliable.ponderjs.mixin;

import com.google.common.collect.Multimap;
import net.createmod.ponder.api.registration.StoryBoardEntry;
import net.createmod.ponder.foundation.registration.PonderSceneRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PonderSceneRegistry.class)
public interface PonderSceneRegistryAccessor {

    @Accessor("scenes")
    Multimap<ResourceLocation, StoryBoardEntry> ponderjs$scenes();
}
