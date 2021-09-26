package com.kotakotik.ponderjs.commands;

import com.kotakotik.ponderjs.PonderRegistryEventJS;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

public class PJSPostCommand implements Command<CommandSource> {
    public static String r(boolean requireReload) {
        try {
            PonderRegistryEventJS.runAllRegistration();
            return "Reran all ponder registration successfully" + (requireReload ? ", press F3+T to reload scenes" : "") + " \n" +
                    "Note: This reload only reloads scenes and lang, not tags";
        } catch (Exception e) {
            e.printStackTrace();
            return "err";
        }
    }

    @Override
    public int run(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        String r = r(true);
        if(r.equals("err")) return 0;
        ctx.getSource().sendSuccess(new StringTextComponent(r), false);
        return 1;
    }
}
