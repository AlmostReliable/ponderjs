package com.kotakotik.ponderjs;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

public class PonderErrorHelper {
    public static void yeet(Exception e) {
        ConsoleJS.CLIENT.error(e);
        e.printStackTrace();

        Player clientPlayer = KubeJS.PROXY.getClientPlayer();
        if(clientPlayer != null) {
            MutableComponent first = new TextComponent("[PonderJS ERROR] ").withStyle(ChatFormatting.DARK_RED);
            MutableComponent second = new TextComponent(e.getMessage()).withStyle(ChatFormatting.RED);
            clientPlayer.sendMessage(first.append(second), clientPlayer.getUUID());
        }
    }
}
