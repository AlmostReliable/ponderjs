package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.config.ModConfigs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class PonderLang {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * @param langName as String
     * @return true if a new lang file was created
     */
    public boolean generate(String langName) {
        File file = new File(ModConfigs.CLIENT.getLangPath().replace("%lang%", langName));

        JsonObject existingLang = read(file);
        JsonObject currentLang = createFromLocalization();

        if (currentLang.equals(existingLang)) {
            return false;
        }

        PonderJSMod.LOGGER.info(
                "PonderJS - New lang file differ from existing lang file, generating new lang file.\n Old Lang size: {} \n\n New lang size: {}",
                existingLang == null ? 0 : existingLang.size(),
                currentLang.size());

        return write(file, currentLang);
    }

    private boolean write(File file, JsonObject currentLang) {
        try {
            String output = GSON.toJson(currentLang);
            FileUtils.writeStringToFile(file, output, "UTF-8");
            return true;
        } catch (IOException e) {
            PonderJSMod.LOGGER.error(e);
        }

        return false;
    }

    @Nullable
    protected JsonObject read(File file) {
        if (file.exists()) {
            try {
                String s = FileUtils.readFileToString(file, "UTF-8");
                return GSON.fromJson(s, JsonObject.class);
            } catch (IOException e) {
                PonderJSMod.LOGGER.error(e);
            }
        }
        return null;
    }

    public JsonObject createFromLocalization() {
        PonderJS.STORIES_MANAGER.compileLang();
        JsonObject object = new JsonObject();
        PonderJS.NAMESPACES.forEach(namespace -> PonderLocalization.record(namespace, object));
        return object;
    }
}
