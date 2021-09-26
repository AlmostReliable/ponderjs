package com.kotakotik.ponderjs.commands;

import com.kotakotik.ponderjs.PonderJS;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import org.antlr.v4.runtime.misc.Triple;

public class GenerateKubeJSLangCommand implements Command<CommandSource> {
    @Override
    public int run(CommandContext<CommandSource> context) {
        PonderJS.fillPonderLang();
        Triple<Boolean, ITextComponent, Integer> result = PonderJS.generateJsonLang(PonderJS.LANG);
        if (result.a) {
            context.getSource().sendSuccess(result.b, false);
        } else {
            context.getSource().sendFailure(result.b);
            return 0;
        }
        return SINGLE_SUCCESS;
    }
}
