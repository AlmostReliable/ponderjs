package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.PonderJS;
import com.almostreliable.ponderjs.PonderLang;
import net.createmod.ponder.foundation.PonderIndex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PonderIndex.class)
public class PonderIndexMixin {

    @Inject(method = "reload", at = @At("RETURN"), remap = false)
    private static void ponderjs$injectLanguage(CallbackInfo ci) {
        PonderJS.ON_RELOAD = true;
        PonderLang.initLanguage(true, true);
        PonderJS.ON_RELOAD = false;
    }

    @Inject(method = "registerAll", at = @At("HEAD"), remap = false, cancellable = true)
    private static void ponderjs$blockRegistering(CallbackInfo ci) {
        if (PonderLang.IGNORE_PONDER_REGISTERING) ci.cancel();
    }

    @Inject(method = "gatherSharedText", at = @At("HEAD"), remap = false, cancellable = true)
    private static void ponderjs$blockSharedText(CallbackInfo ci) {
        if (PonderLang.IGNORE_SHARED_TEXT) ci.cancel();
    }
}
