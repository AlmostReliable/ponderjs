package com.kotakotik.ponderjs;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BuildConfig.MODID)
public class PonderJSMod {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(BuildConfig.MODID);

    public PonderJSMod() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> "ANY", (a, b) -> true));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> PonderJS::clientModInit);
//        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
//                () -> () -> {
//                    if(ModList.get().isLoaded("crafttweaker")) {
//                        com.kotakotik.pondermaker.crafttweaker.PonderTweaker.register();
//                    }
//                });
    }
}
