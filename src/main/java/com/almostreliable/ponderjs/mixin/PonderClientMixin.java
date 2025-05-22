package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.PonderLang;
import net.createmod.ponder.PonderClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PonderClient.class)
public class PonderClientMixin {

    @Inject(method = "modLoadCompleted", at = @At("RETURN"), remap = false)
    private static void ponderjs$injectLanguage(CallbackInfo ci) {
        PonderLang.initLanguage(false);
    }
}
