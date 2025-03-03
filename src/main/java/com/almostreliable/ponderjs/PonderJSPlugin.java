package com.almostreliable.ponderjs;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class PonderJSPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return BuildConfig.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderEvents.REGISTRY.post(new PonderRegistryEventJS(helper));
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderEvents.REGISTRY.post(new PonderItemTagEventJS(helper));
    }
}
