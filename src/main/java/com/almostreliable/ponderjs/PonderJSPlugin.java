package com.almostreliable.ponderjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;

public class PonderJSPlugin extends KubeJSPlugin {

    @Override
    public void addBindings(BindingsEvent event) {
        if (event.type != ScriptType.CLIENT) return;
        PonderJS.addBindings(event);
    }

    @Override
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        if (type != ScriptType.CLIENT) return;
        PonderJS.addTypeWrappers(type, typeWrappers);
    }
}
