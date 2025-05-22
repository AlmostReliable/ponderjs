package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.PonderJS;
import com.almostreliable.ponderjs.PonderLang;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.foundation.PonderIndex;
import net.createmod.ponder.foundation.registration.PonderLocalization;
import net.createmod.ponder.foundation.registration.PonderSceneRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PonderLocalization.class)
public class PonderLocalizationMixin {

    @Inject(method = "generateSceneLang", at = @At("HEAD"), remap = false, cancellable = true)
    public void ponderjs$overrideGenerateSceneLang(CallbackInfo ci) {
        if (!PonderLang.GENERATING_LANG) {
            return;
        }

        var localization = (PonderLocalization) (Object) this;
        PonderIndex
                .getSceneAccess()
                .getRegisteredEntries()
                .stream()
                .filter(scene -> PonderJS.NAMESPACES.contains(scene.getKey().getNamespace()))
                .forEach(
                        entry -> {
                            assert Minecraft.getInstance().level != null;
                            PonderSceneRegistry.compileScene(localization,
                                    entry.getValue(),
                                    new PonderLevel(BlockPos.ZERO, Minecraft.getInstance().level));
                        }
                );

        ci.cancel();
    }
}
