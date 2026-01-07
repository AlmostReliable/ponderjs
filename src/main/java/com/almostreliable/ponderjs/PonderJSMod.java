package com.almostreliable.ponderjs;


import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import static com.almostreliable.ponderjs.PonderJS.PLUGIN;

@Mod(BuildConfig.MOD_ID)
public class PonderJSMod {

    public PonderJSMod(IEventBus bus) {
        // I have no clue for what this existed lol
//        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
//                () -> new IExtensionPoint.DisplayTest(() -> "ANY", (a, b) -> true));

        bus.addListener(this::onClient);
    }

    private void onClient(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(PLUGIN);
    }
}
