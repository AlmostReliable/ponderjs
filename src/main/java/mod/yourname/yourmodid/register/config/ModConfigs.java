package mod.yourname.yourmodid.register.config;

import com.simibubi.create.foundation.config.ConfigBase;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModConfigs {
    public static ModServerConfig SERVER;

    public static class Config extends ConfigBase {
        @Override
        protected void registerAll(ForgeConfigSpec.Builder builder) {
            // prevent crashes with empty config
            if(children == null) {
                children = new ArrayList<>();
            }
            if(allValues == null) {
                allValues = new ArrayList<>();
            }
            super.registerAll(builder);
        }

        @Override
        public String getName() {
            return StringUtils.uncapitalize(getClass().getSimpleName());
        }
    }

    static Map<Config, ModConfig.Type> configs = new HashMap<>();

    private static <T extends Config> T register(Supplier<T> factory, ModConfig.Type side) {
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
        SERVER = register(ModServerConfig::new, ModConfig.Type.SERVER);
        // server is here as an example, as its the most used config type in create, and adding more is pretty much the same

        for (Map.Entry<Config, ModConfig.Type> pair : configs.entrySet())
            ModLoadingContext.get()
                    .registerConfig(pair.getValue(), pair.getKey().specification);
    }

    public static void onLoad(net.minecraftforge.fml.config.ModConfig.Loading event) {
        for (Map.Entry<Config, ModConfig.Type> pair : configs.entrySet())
            if (pair.getKey().specification == event.getConfig()
                    .getSpec())
                pair.getKey()
                        .onLoad();
    }

    public static void onReload(net.minecraftforge.fml.config.ModConfig.Reloading event) {
        for (Map.Entry<Config, ModConfig.Type> pair : configs.entrySet())
            if (pair.getKey().specification == event.getConfig()
                    .getSpec())
                pair.getKey()
                        .onReload();
    }
}
