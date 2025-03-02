package com.almostreliable.ponderjs.mixin;

import dev.latvian.mods.kubejs.client.KubeJSClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KubeJSClient.class)
public class KubeJSClientMixin {

    @Inject(method = "reloadClientScripts", at = @At("RETURN"), remap = false)
    private static void reloadClientScripts(CallbackInfo ci) {
//        if (PonderJS.isInitialized()) {
//            PonderJS.reload();
//            String msg = "Ponder tags event is currently not reloadable. Only scenes were reloaded.";
//            if (KubeJS.PROXY.getClientPlayer() != null) {
//                KubeJS.PROXY.getClientPlayer().sendSystemMessage(Component.literal(msg));
//            }
//            ConsoleJS.CLIENT.info(msg);
//        }
    }

}
