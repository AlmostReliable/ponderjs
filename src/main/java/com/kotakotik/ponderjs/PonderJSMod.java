package com.kotakotik.ponderjs;


import com.jozufozu.flywheel.util.Lazy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

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

    static Lazy<?> staticFinalFieldVal(Class<?> clazz, String field) {
        return new Lazy<>(() -> {
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

    static Lazy<Method> staticMethodVal(Class<?> clazz, String method, Class<?>... classes) {
        return new Lazy<>(() -> {
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
        Lazy<Method> m = staticMethodVal(clazz, method, arg);
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
