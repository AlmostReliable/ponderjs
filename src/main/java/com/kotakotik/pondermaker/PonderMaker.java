package com.kotakotik.pondermaker;


import com.kotakotik.pondermaker.config.ModConfigs;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Optional;

@Mod(BuildConfig.MODID)
public class PonderMaker {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(BuildConfig.MODID);
    public static IEventBus modEventBus;

    public PonderMaker() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(ModConfigs::onLoad);
        modEventBus.addListener(ModConfigs::onReload);
        ModConfigs.register();
//        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
//                () -> () -> {
//                    if(ModList.get().isLoaded("crafttweaker")) {
//                        com.kotakotik.pondermaker.crafttweaker.PonderTweaker.register();
//                    }
//                });
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

    public static final HashMap<String, AllIcons> cachedIcons = new HashMap<>();

    public static AllIcons getIconByName(String icon) {
        String str = icon.toUpperCase();
        if(!str.startsWith("I_")) {
            str = "I_" + str;
        }
        if(cachedIcons.containsKey(str)) {
            return cachedIcons.get(str);
        }
        Field f = ObfuscationReflectionHelper.findField(AllIcons.class, str);
        try {
            cachedIcons.put(str, (AllIcons) f.get(null));
            cachedIcons.get(str);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
