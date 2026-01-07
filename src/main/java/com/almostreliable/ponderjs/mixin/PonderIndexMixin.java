package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.*;
import net.createmod.ponder.foundation.PonderIndex;
import net.createmod.ponder.foundation.registration.DefaultPonderTagRegistrationHelper;
import net.createmod.ponder.foundation.registration.PonderLocalization;
import net.createmod.ponder.foundation.registration.PonderSceneRegistry;
import net.createmod.ponder.foundation.registration.PonderTagRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.almostreliable.ponderjs.PonderJS.PLUGIN;

@Mixin(PonderIndex.class)
public class PonderIndexMixin {

    @Shadow(remap = false) @Final private static PonderSceneRegistry SCENES;

    @Shadow(remap = false) @Final private static PonderTagRegistry TAGS;

    @Shadow(remap = false) @Final private static PonderLocalization LOCALIZATION;

    @Inject(method = "registerAll", at = @At("HEAD"), remap = false, cancellable = true)
    private static void ponderjs$blockRegistering(CallbackInfo ci) {
        if (PonderLang.GENERATING_LANG) ci.cancel();
    }

    @Inject(method = "gatherSharedText", at = @At("HEAD"), remap = false, cancellable = true)
    private static void ponderjs$blockSharedText(CallbackInfo ci) {
        if (PonderJS.ON_RELOAD) ci.cancel();
    }

    @Inject(method = "registerAll", at = @At("RETURN"), remap = false)
    private static void ponderjs$invokeEvents(CallbackInfo ci) {
        PonderLang.reset();
        PonderEvents.REGISTRY.post(new PonderRegistryEventJS(SCENES));
        var tagRegHelper = new DefaultPonderTagRegistrationHelper(PLUGIN.getModId(), TAGS, LOCALIZATION);
        PonderEvents.TAGS.post(new PonderItemTagEventJS(tagRegHelper));
    }
}
