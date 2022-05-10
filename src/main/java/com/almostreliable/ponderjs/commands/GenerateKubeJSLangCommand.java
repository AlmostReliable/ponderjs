package com.almostreliable.ponderjs.commands;

import com.almostreliable.ponderjs.PonderJS;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.antlr.v4.runtime.misc.Triple;

public class GenerateKubeJSLangCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        String lang = context.getArgument("lang", String.class);
        Triple<Boolean, Component, Integer> result = PonderJS.generateJsonLang(lang);
        CommandSourceStack source = context.getSource();
        if (result.a) {
            source.sendSuccess(result.b, false);
        } else {
            source.sendFailure(result.b);
            return 0;
        }
        return SINGLE_SUCCESS;
    }
}
