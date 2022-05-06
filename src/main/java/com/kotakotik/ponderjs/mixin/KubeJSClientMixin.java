package com.kotakotik.ponderjs.mixin;

import com.kotakotik.ponderjs.PonderJS;
import com.kotakotik.ponderjs.PonderRegistryEventJS;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.client.KubeJSClient;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KubeJSClient.class)
public class KubeJSClientMixin {

    @Inject(method = "reloadClientScripts", at = @At("RETURN"), remap = false)
    private static void reloadClientScripts(CallbackInfo ci) {
        if (PonderJS.isInitialized()) {
            // TODO try making PonderItemTag event reloadable
            new PonderRegistryEventJS().post(ScriptType.CLIENT, PonderJS.REGISTRY_EVENT);
            PonderJS.generateJsonLang("en_us");

            String msg = "Ponder tags event currently is not reloadable. Only scenes are reloaded.";
            if(KubeJS.PROXY.getClientPlayer() != null) {
                KubeJS.PROXY.getClientPlayer().sendMessage(new TextComponent(msg), KubeJS.PROXY.getClientPlayer().getUUID());
            }
            ConsoleJS.CLIENT.info(msg);
        }
    }

}
