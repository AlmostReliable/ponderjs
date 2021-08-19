package com.kotakotik.pondermaker.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.ScriptLoadingOptions;
import com.kotakotik.pondermaker.BuildConfig;
import com.kotakotik.pondermaker.PonderMaker;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;

public class PonderTweaker {
    public static void register() {
        PonderMaker.modEventBus.addListener(PonderTweaker::clientSetup);
    }

    public static void clientSetup(FMLClientSetupEvent event) {
        final ScriptLoadingOptions scriptLoadingOptions = new ScriptLoadingOptions().setLoaderName(BuildConfig.MODID).execute();

        CraftTweakerAPI.loadScripts(scriptLoadingOptions);
        event.enqueueWork(() -> toReg.forEach(Runnable::run));
    }

    protected static List<Runnable> toReg = new ArrayList<>();

    public static void addFunctionToRegister(Runnable function) {
        toReg.add(function);
    }
}
