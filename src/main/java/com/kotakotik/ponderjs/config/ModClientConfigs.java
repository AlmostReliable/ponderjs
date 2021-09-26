package com.kotakotik.ponderjs.config;

import com.simibubi.create.foundation.config.ui.ConfigAnnotations;
import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfigs extends ModConfigs.AutoConfigBase {
    public ForgeConfigSpec.ConfigValue<String> langPath;
    public ForgeConfigSpec.ConfigValue<String> lang;
    public ConfigBool autoGenerateLang = b(true, "autoGenerateLang",
            "Whether or not to automatically generate lang " +
                    "into the path in the langPath config value, if disabled, a resource pack is generated at runtime for this" +
                    ", however this should really be true", ConfigAnnotations.RequiresRestart.CLIENT.asComment());

    public String getLangPath() {
        return langPath.get().replace("%lang%", lang.get());
    }

    @Override
    protected void registerAll(ForgeConfigSpec.Builder builder) {
        langPath =
                builder.comment(" ", "The path to the lang file where lang entries are automatically generated" +
                        " %lang% is replaced by the value of the lang config value")
                        .define("langPath", "kubejs/assets/ponderjs_generated/lang/%lang%.json");

        lang =
                builder.comment(" ", "Used for the langPath config value, and if autoGenerateLang is false this is where it will generate lang in the generated pack")
                .define("lang", "en_us");

        super.registerAll(builder);
    }
}
