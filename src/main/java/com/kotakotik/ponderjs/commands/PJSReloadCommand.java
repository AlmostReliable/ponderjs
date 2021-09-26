package com.kotakotik.ponderjs.commands;

import com.kotakotik.ponderjs.PonderRegistryEventJS;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.latvian.kubejs.KubeJS;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;

public class PJSReloadCommand implements Command<CommandSource> {
    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        // HEY! HEY! IF YOU ARE READING THIS AND YOU ARENT KOTA, GO AWAY BECAUSE THIS CODE IS HORRIBLE AND WILL BURN YOUR EYES!!!
        KubeJS.PROXY.reloadClientInternal();
        String out = PJSPostCommand.r(false);
        PonderRegistryEventJS.runAllRegistration();
        Minecraft.getInstance().reloadResourcePacks();

//        out.append("RELOAD ASSETS: Asset reload ").append(noOut).append("\n");
        context.getSource().sendSuccess(new StringTextComponent(out), false);
        return SINGLE_SUCCESS;
    }
}
