package com.almostreliable.ponderjs;


import com.almostreliable.ponderjs.commands.GenerateKubeJSLangCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static com.almostreliable.ponderjs.PonderJS.PLUGIN;

@Mod(BuildConfig.MOD_ID)
public class PonderJSMod {

    public PonderJSMod(IEventBus bus) {
        // I have no clue for what this existed lol
//        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
//                () -> new IExtensionPoint.DisplayTest(() -> "ANY", (a, b) -> true));

        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        bus.addListener(this::onClient);
    }

    private void onClient(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(PLUGIN);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dis = event.getDispatcher();
        LiteralArgumentBuilder<CommandSourceStack> b = Commands.literal(BuildConfig.MOD_ID);
        b.then(Commands.literal("generate_lang_template")
                .then(Commands.argument("lang", StringArgumentType.word())
                        .requires((source) -> source.getServer().isSingleplayer())
                        .executes(new GenerateKubeJSLangCommand())));
        dis.register(b);
    }
}
