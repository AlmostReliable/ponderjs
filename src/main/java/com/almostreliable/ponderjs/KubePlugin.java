package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.particles.ParticleTransformation;
import com.almostreliable.ponderjs.util.BlockStateFunction;
import com.almostreliable.ponderjs.util.Util;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.TypeWrapperRegistry;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ParrotElement;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.PonderTag;
import net.createmod.ponder.foundation.element.InputWindowElement;
import net.minecraft.world.level.block.state.BlockState;

public class KubePlugin implements KubeJSPlugin {

    @Override
    public void registerBindings(BindingRegistry bindings) {
        if (!bindings.type().isClient()) {
            return;
        }

        bindings.add("PonderPalette", PonderPalette.class);
        bindings.add("ParrotElement", ParrotElement.class);
        bindings.add("PonderInputWindowElement", InputWindowElement.class);
        bindings.add("PonderInput", InputWindowElement.class);
        bindings.add("PonderPointing", Pointing.class);
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        if (!registry.scriptType().isClient()) {
            return;
        }

        registry.register(Selection.class, Util::selectionOf);
        registry.register(PonderTag.class, Util::ponderTagOf);
        registry.register(BlockStateFunction.class, BlockStateFunction::of);
        registry.register(ParticleTransformation.Data.class, ParticleTransformation.Data::of);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(PonderEvents.GROUP);
    }
}
