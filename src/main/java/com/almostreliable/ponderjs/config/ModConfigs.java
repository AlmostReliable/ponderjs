package com.almostreliable.ponderjs.config;

import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModConfigs {
    public static ModClientConfigs CLIENT;
    static Map<AutoConfigBase, ModConfig.Type> configs = new HashMap<>();

    private static <T extends AutoConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        configs.put(config, side);
        return config;
    }

    public static void register() {
        CLIENT = register(ModClientConfigs::new, ModConfig.Type.CLIENT);

        for (Map.Entry<AutoConfigBase, ModConfig.Type> pair : configs.entrySet()) {
            ModLoadingContext.get()
                    .registerConfig(pair.getValue(), pair.getKey().specification);
        }
    }

    public static void onLoad(ModConfigEvent.Loading event) {
        for (Map.Entry<AutoConfigBase, ModConfig.Type> pair : configs.entrySet()) {
            if (pair.getKey().specification == event.getConfig()
                    .getSpec()) {
                pair.getKey()
                        .onLoad();
            }
        }
    }

    public static void onReload(ModConfigEvent.Reloading event) {
        for (Map.Entry<AutoConfigBase, ModConfig.Type> pair : configs.entrySet()) {
            if (pair.getKey().specification == event.getConfig()
                    .getSpec()) {
                pair.getKey()
                        .onReload();
            }
        }
    }

    public static class AutoConfigBase extends ConfigBase {
        @Override
        protected void registerAll(ForgeConfigSpec.Builder builder) {
            // prevent crashes with empty config
            if (children == null) {
                children = new ArrayList<>();
            }
            if (allValues == null) {
                allValues = new ArrayList<>();
            }
            super.registerAll(builder);
        }

        @Override
        public String getName() {
            return StringUtils.uncapitalize(getClass().getSimpleName());
        }
    }
}
