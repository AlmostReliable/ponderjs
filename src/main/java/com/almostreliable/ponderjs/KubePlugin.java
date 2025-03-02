package com.almostreliable.ponderjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.createmod.ponder.foundation.PonderIndex;

public class KubePlugin extends KubeJSPlugin {

    private final Object lock = new Object();

    @Override
    public void registerBindings(BindingsEvent event) {
        if (event.getType().isClient()) {
            PonderJS.addBindings(event);
        }
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        if (type != ScriptType.CLIENT) return;
        PonderJS.addTypeWrappers(type, typeWrappers);
    }

    @Override
    public void registerEvents() {
        PonderEvents.GROUP.register();
    }

    @Override
    public void generateLang(LangEventJS event) {
        PonderIndex.getLangAccess().provideLang(BuildConfig.MOD_ID, event::add);
    }
}
