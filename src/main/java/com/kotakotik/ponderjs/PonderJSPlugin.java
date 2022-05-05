package com.kotakotik.ponderjs;

import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.PonderTag;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static net.minecraftforge.fml.DistExecutor.safeRunWhenOn;
import static net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn;

public class PonderJSPlugin extends KubeJSPlugin {

    @Override
    public void init() {
//        Create.registrate().addRegisterCallback()
    }

    @Override
    public void clientInit() {
        super.clientInit();
    }

    @Override
    public void afterInit() {
        super.afterInit();
    }

    @Override
    public void addBindings(BindingsEvent event) {
        unsafeRunWhenOn(Dist.CLIENT, () -> () -> PonderJS.addBindings(event));
    }

    @Override
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        if (type != ScriptType.CLIENT) return;
        unsafeRunWhenOn(Dist.CLIENT, () -> () -> PonderJS.addTypeWrappers(type, typeWrappers));
    }

    @Override
    public void generateLang(Map<String, String> lang) {
        super.generateLang(lang);
        Map<String, String> createdLang = PonderJS.createLang();
        lang.putAll(createdLang);
    }
}
