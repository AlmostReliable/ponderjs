package com.almostreliable.ponderjs;

import net.fabricmc.api.ClientModInitializer;

public class PonderJSFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PonderJS.init();
    }
}
