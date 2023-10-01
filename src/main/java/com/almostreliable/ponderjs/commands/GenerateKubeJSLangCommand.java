package com.almostreliable.ponderjs.commands;

import com.almostreliable.ponderjs.PonderLang;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class GenerateKubeJSLangCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        String lang = context.getArgument("lang", String.class);
        PonderLang ponderLang = new PonderLang();

        CommandSourceStack source = context.getSource();
        if (ponderLang.generate(lang)) {
            source.sendSuccess(() -> Component.literal("Changes detected - New lang file created."), false);
        } else {
            source.sendSuccess(() -> Component.literal("Lang file the same. Nothing created."), false);
        }

        return SINGLE_SUCCESS;
    }
}
