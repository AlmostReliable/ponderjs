package com.almostreliable.ponderjs;


import com.almostreliable.ponderjs.commands.GenerateKubeJSLangCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.almostreliable.ponderjs.PonderJS.PLUGIN;

@Mod(BuildConfig.MOD_ID)
public class PonderJSMod {

    public PonderJSMod() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> "ANY", (a, b) -> true));

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        modEventBus.addListener(this::onClient);
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
