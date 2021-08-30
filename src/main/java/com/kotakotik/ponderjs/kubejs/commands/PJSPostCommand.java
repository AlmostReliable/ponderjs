package com.kotakotik.ponderjs.kubejs.commands;

import com.kotakotik.ponderjs.kubejs.PonderRegistryEventJS;
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
                    "Note: This reload is pretty janky with tags and it can't remove scenes so I recommend only using it to reload scenes";
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
