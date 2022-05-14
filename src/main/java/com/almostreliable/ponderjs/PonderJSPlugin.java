package com.almostreliable.ponderjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.minecraftforge.api.distmarker.Dist;

import static net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn;

public class PonderJSPlugin extends KubeJSPlugin {

    @Override
    public void addBindings(BindingsEvent event) {
        unsafeRunWhenOn(Dist.CLIENT, () -> () -> PonderJS.addBindings(event));
    }

    @Override
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        if (type != ScriptType.CLIENT) return;
        unsafeRunWhenOn(Dist.CLIENT, () -> () -> PonderJS.addTypeWrappers(type, typeWrappers));
    }
}
