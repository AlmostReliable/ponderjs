package com.almostreliable.ponderjs.mixin;

import net.createmod.ponder.foundation.PonderIndex;
import net.createmod.ponder.foundation.registration.PonderTagRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.throwables.MixinException;

@Mixin(PonderIndex.class)
public interface PonderIndexAccessor {

    @Accessor(value = "TAGS", remap = false)
    static PonderTagRegistry getTags() {
        throw new MixinException("Cannot access PonderIndex.TAGS directly!");
    }
}
