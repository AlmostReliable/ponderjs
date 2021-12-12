package com.kotakotik.ponderjs.commands;

import com.kotakotik.ponderjs.PonderRegistryEventJS;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.latvian.mods.kubejs.KubeJS;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;

public class PJSReloadCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        // HEY! HEY! IF YOU ARE READING THIS AND YOU ARENT KOTA, GO AWAY BECAUSE THIS CODE IS HORRIBLE AND WILL BURN YOUR EYES!!!
        KubeJS.PROXY.reloadClientInternal();
        String out = PJSPostCommand.r(false);
        PonderRegistryEventJS.runAllRegistration();
        Minecraft.getInstance().reloadResourcePacks();

//        out.append("RELOAD ASSETS: Asset reload ").append(noOut).append("\n");
        context.getSource().sendSuccess(new TextComponent(out), false);
        return SINGLE_SUCCESS;
    }
}
