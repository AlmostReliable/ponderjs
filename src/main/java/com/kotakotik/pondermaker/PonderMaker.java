package com.kotakotik.pondermaker;


import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

@Mod(BuildConfig.MODID)
public class PonderMaker {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(BuildConfig.MODID);
    public static IEventBus modEventBus;

    public PonderMaker() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> {
                    if(ModList.get().isLoaded("kubejs")) {
                        new com.kotakotik.pondermaker.kubejs.PonderJS();
                    }
                    if(ModList.get().isLoaded("crafttweaker")) {
                        com.kotakotik.pondermaker.crafttweaker.PonderTweaker.register();
                    }
                });
    }

    public static Optional<PonderTag> getTagByName(ResourceLocation res) {
        return PonderRegistry.TAGS.getListedTags().stream().filter(tag -> tag.getId().equals(res)).findFirst();
    }

    public static ResourceLocation appendCreateToId(String tag) {
        if(!tag.contains(":")) tag = "create:" + tag;
        return new ResourceLocation(tag);
    }

    public static Optional<PonderTag> getTagByName(String tag) {
        return getTagByName(appendCreateToId(tag));
    }

    public static <T extends IForgeRegistryEntry<? super T> & IItemProvider> ItemProviderEntry<T> createItemProvider(RegistryObject<T> item) {
        return new ItemProviderEntry<>(Create.registrate(), item);
    }
}
