package com.kotakotik.ponderjs.mixin;

import com.simibubi.create.foundation.ponder.PonderLocalization;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PonderLocalization.class)
public interface PonderLocalizationAccessor {

    @Invoker("langKeyForTag")
    static String langKeyForTag(ResourceLocation k) {
        throw new UnsupportedOperationException();
    }

    @Invoker("langKeyForTagDescription")
    static String langKeyForTagDescription(ResourceLocation k) {
        throw new UnsupportedOperationException();
    }
}
