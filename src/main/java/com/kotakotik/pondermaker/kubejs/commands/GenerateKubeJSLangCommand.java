package com.kotakotik.pondermaker.kubejs.commands;

import com.kotakotik.pondermaker.kubejs.PonderJS;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.simibubi.create.foundation.utility.Pair;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.ITextComponent;
import org.antlr.v4.runtime.misc.Triple;

public class GenerateKubeJSLangCommand implements Command<CommandSource> {
    @Override
    public int run(CommandContext<CommandSource> context) {
        Triple<Boolean, ITextComponent, Integer> result = PonderJS.generateJsonLang(PonderJS.LANG);
        if(result.a) {
            context.getSource().sendSuccess(result.b, false);
        } else {
            context.getSource().sendFailure(result.b);
            return 0;
        }
        return SINGLE_SUCCESS;
    }
}
