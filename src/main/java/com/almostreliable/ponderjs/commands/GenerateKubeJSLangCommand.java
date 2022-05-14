package com.almostreliable.ponderjs.commands;

import com.almostreliable.ponderjs.PonderLang;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;

public class GenerateKubeJSLangCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        String lang = context.getArgument("lang", String.class);
        PonderLang ponderLang = new PonderLang();

        CommandSourceStack source = context.getSource();
        if (ponderLang.generate(lang)) {
            source.sendSuccess(new TextComponent("Changes detected - New lang file created."), false);
        } else {
            source.sendSuccess(new TextComponent("Lang file the same. Nothing created."), false);
        }

        return SINGLE_SUCCESS;
    }
}
