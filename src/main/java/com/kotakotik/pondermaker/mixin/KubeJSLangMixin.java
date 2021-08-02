package com.kotakotik.pondermaker.mixin;

import com.google.gson.JsonElement;
import com.kotakotik.pondermaker.kubejs.PonderRegistryEventJS;
import dev.latvian.kubejs.client.KubeJSClientResourcePack;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.kubejs.script.data.KubeJSResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(KubeJSClientResourcePack.class)
public abstract class KubeJSLangMixin extends KubeJSResourcePack {
    public KubeJSLangMixin(ResourcePackType t) {
        super(t);
    }

    @Inject(method = "generateJsonFiles", at = @At("HEAD"), remap = false)
    public void mixin(Map<ResourceLocation, JsonElement> map, CallbackInfo ci) {
        new PonderRegistryEventJS().post(ScriptType.CLIENT, "ponder.registry");
    }
}
