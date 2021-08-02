package com.kotakotik.pondermaker.kubejs.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.kotakotik.pondermaker.PonderMaker;
import com.kotakotik.pondermaker.kubejs.PonderJS;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.latvian.kubejs.command.KubeJSCommands;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class GenerateKubeJSLangCommand implements Command<CommandSource> {
    @Override
    public int run(CommandContext<CommandSource> context) {
        Logger log = PonderMaker.LOGGER;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File file = new File("kubejs/assets/kubejs/lang/en_us.json");
        JsonObject json = new JsonObject();
        if(file.exists()) {
            log.info("Found KubeJS en_us.json, reading!");
//            Files.writeString(file.toPath(), "", Charset.defaultCharset(), StandardOpenOption.WRITE);
//            file.
            try {
//                FileInputStream i = new FileInputStream(file.getAbsolutePath());
                json = gson.fromJson(FileUtils.readFileToString(file, "UTF-8"), JsonObject.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            json = gson.fromJson(Files.readString(file.getPath()));
        }
        JsonObject finalJson = json;
        List<String> wrote = new ArrayList<>();
        PonderJS.LANG.forEach((key, value) -> {
            log.info("Writing KubeJS lang key " + key);
            finalJson.addProperty(key, value);
            wrote.add(key);
        });
        String j = gson.toJson(wrote);
        try {
            FileUtils.writeStringToFile(file, gson.toJson(finalJson), "UTF-8");
            String c = "Wrote " + wrote.size() + " KubeJS lang keys";
            log.info(c);
            if(wrote.size() > 0) {
                log.info(j);
            }
            context.getSource().sendSuccess(new StringTextComponent(c), false);
        } catch (IOException e) {
            log.error("Couldn't write KubeJS lang");
            context.getSource().sendFailure(new StringTextComponent("Unable to write KubeJS lang: " + e.getClass().getSimpleName() + "\nMore info in logs"));
            e.printStackTrace();
            return 0;
        }
        return SINGLE_SUCCESS;
    }
}
