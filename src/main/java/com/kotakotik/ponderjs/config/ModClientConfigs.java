package com.kotakotik.ponderjs.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfigs extends ModConfigs.AutoConfigBase {
    public ForgeConfigSpec.ConfigValue<String> langPath;

    public String getLangPath() {
        return langPath.get();
    }

    @Override
    protected void registerAll(ForgeConfigSpec.Builder builder) {
        langPath =
                builder.comment(" ", "The path to the lang file where lang entries are automatically generated" +
                                     " %lang% is replaced by the value of the lang argument in the generate command")
                        .define("langPath", "kubejs/assets/ponderjs_generated/lang/%lang%.json");

        super.registerAll(builder);
    }
}
