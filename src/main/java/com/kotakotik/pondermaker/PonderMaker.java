package com.kotakotik.pondermaker;


import com.simibubi.create.foundation.ponder.content.PonderTag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Mod(BuildConfig.MODID)
public class PonderMaker {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger(BuildConfig.MODID);
    public static IEventBus modEventBus;

    public PonderMaker() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> {
                    if(ModList.get().isLoaded("kubejs")) {
                        new com.kotakotik.pondermaker.kubejs.PonderJS();
                    }
                });
    }

    public static Optional<PonderTag> getTagByName(ResourceLocation res) {
        return PonderTag.LISTED_TAGS.stream().filter(tag -> tag.getId().equals(res)).findFirst();
    }
}
