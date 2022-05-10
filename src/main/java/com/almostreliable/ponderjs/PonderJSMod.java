package com.almostreliable.ponderjs;


import com.almostreliable.ponderjs.config.ModConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BuildConfig.MOD_ID)
public class PonderJSMod {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(BuildConfig.MOD_ID);

    public PonderJSMod() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> "ANY", (a, b) -> true));

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(EventPriority.LOWEST, this::onCommonSetup);
        modEventBus.addListener(ModConfigs::onLoad);
        modEventBus.addListener(ModConfigs::onReload);
        ModConfigs.register();
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> PonderJS::init);
    }
}
