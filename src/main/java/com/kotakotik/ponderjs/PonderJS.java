package com.kotakotik.ponderjs;


import com.kotakotik.ponderjs.config.ModConfigs;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import com.simibubi.create.repack.registrate.util.entry.ItemProviderEntry;
import dev.latvian.kubejs.KubeJS;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

@Mod(BuildConfig.MODID)
public class PonderJS {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger(BuildConfig.MODID);
    public static IEventBus modEventBus;

    public PonderJS() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

            modEventBus.addListener(ModConfigs::onLoad);
            modEventBus.addListener(ModConfigs::onReload);
            ModConfigs.register();
        });
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

    protected static ResourceLocation appendNamespaceToId(String namespace, String id) {
        if(!id.contains(":")) id = namespace + ":" + id;
        return new ResourceLocation(id);
    }

    public static ResourceLocation appendCreateToId(String tag) {
        return appendNamespaceToId(Create.ID, tag);
    }

    public static ResourceLocation appendKubeToId(String id) {
        return appendNamespaceToId(KubeJS.MOD_ID, id);
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

    static LazyValue<?> staticFinalFieldVal(Class<?> clazz, String field) {
        return new LazyValue<>(() -> {
            Field f;
            try {
                f = clazz.getDeclaredField(field);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                return null;
            }
            f.setAccessible(true);
            try {
                return f.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    static LazyValue<Method> staticMethodVal(Class<?> clazz, String method, Class<?>... classes) {
        return new LazyValue<>(() -> {
            Method f;
            try {
                f = clazz.getDeclaredMethod(method, classes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
            f.setAccessible(true);
            return f;
        });
    }

    static <R, T> Function<T, R> staticOneArgMethod(Class<?> clazz, String method, Class<T> arg) {
        LazyValue<Method> m = staticMethodVal(clazz, method, arg);
        return (t) -> {
            try {
                return (R) m.get().invoke(null, t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        };
    }
}
