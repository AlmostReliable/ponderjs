package com.kotakotik.ponderjs.commands;

import com.almostreliable.ponderjs.BuildConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PJSCommands {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dis = event.getDispatcher();
        LiteralArgumentBuilder<CommandSourceStack> b = Commands.literal(BuildConfig.MODID);
        b.then(Commands.literal("generate_lang_template")
                .then(Commands.argument("lang", StringArgumentType.word())
                        .requires((source) -> source.getServer().isSingleplayer())
                        .executes(new GenerateKubeJSLangCommand())));
        dis.register(b);
    }
}
