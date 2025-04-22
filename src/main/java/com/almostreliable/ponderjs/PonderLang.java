package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.mixin.PonderIndexAccessor;
import com.almostreliable.ponderjs.util.PonderErrorHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.almostreliable.ponderjs.PonderJS.PLUGIN;

public class PonderLang {
    public static final String PATH = "kubejs/assets/ponderjs_generated/lang/%lang%.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static boolean IGNORE_PONDER_REGISTERING = false;
    public static boolean IGNORE_SHARED_TEXT = false;

    public static void initLanguage(boolean ignorePonderReg, boolean ignoreSharedText) {
        if (!PonderIndexAccessor.getPlugins().contains(PLUGIN)) {
            return;
        }

        IGNORE_PONDER_REGISTERING = ignorePonderReg;
        IGNORE_SHARED_TEXT = ignoreSharedText;

        try {
            if (generate("en_us")) {
                try {
                    Minecraft.getInstance().reloadResourcePacks();
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Something went wrong while reloading resources after PonderJS init. You have to manually reload the resources for the changes to take effect.",
                            e);
                }
            }
        } catch (Exception e) {
            PonderErrorHelper.yeet(e);
        }

        IGNORE_PONDER_REGISTERING = false;
        IGNORE_SHARED_TEXT = false;
    }

    /**
     * @param langName as String
     * @return true if a new lang file was created
     */
    public static boolean generate(String langName) {
        File file = new File(PATH.replace("%lang%", langName));

        JsonObject existingLang = read(file);
        JsonObject currentLang = createFromLocalization();

        if (currentLang.equals(existingLang)) {
            return false;
        }

        PonderJS.LOGGER.info(
                "PonderJS - New lang file differ from existing lang file, generating new lang file.\n Old Lang size: {} \n\n New lang size: {}",
                existingLang == null ? 0 : existingLang.size(),
                currentLang.size());

        return write(file, currentLang);
    }

    private static boolean write(File file, JsonObject currentLang) {
        try {
            String output = GSON.toJson(currentLang);
            FileUtils.writeStringToFile(file, output, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            PonderJS.LOGGER.error(e);
        }

        return false;
    }

    @Nullable
    protected static JsonObject read(File file) {
        if (file.exists()) {
            try {
                String s = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                return GSON.fromJson(s, JsonObject.class);
            } catch (IOException e) {
                PonderJS.LOGGER.error(e);
            }
        }
        return null;
    }

    public static JsonObject createFromLocalization() {
        JsonObject object = new JsonObject();
        for (String namespace : PonderJS.NAMESPACES) {
            PonderIndex.getLangAccess().provideLang(namespace, object::addProperty);
        }

        return object;
    }
}
