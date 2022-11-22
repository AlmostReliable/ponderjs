package com.almostreliable.ponderjs;


import com.almostreliable.ponderjs.commands.GenerateKubeJSLangCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

//@Mod(BuildConfig.MOD_ID)
public class PonderJSMod implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        PonderJS.init();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (!environment.includeIntegrated) return;
            registerCommands(dispatcher);
        });
    }

    public PonderJSMod() {
//        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
//                () -> new IExtensionPoint.DisplayTest(() -> "ANY", (a, b) -> true));
//
//        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
//        modEventBus.addListener(this::ponderClientInit);
        // I need to refactor this
//        modEventBus.addListener(ModConfigs::onLoad);
//        modEventBus.addListener(ModConfigs::onReload);
//        ModConfigs.register();
    }

    //    private void ponderClientInit(FMLClientSetupEvent event) {
//        event.enqueueWork(PonderJS::init);
//    }
//
    private void registerCommands(CommandDispatcher<CommandSourceStack> dis) {
//        CommandDispatcher<CommandSourceStack> dis = event.getDispatcher();
        LiteralArgumentBuilder<CommandSourceStack> b = Commands.literal(BuildConfig.MOD_ID);
        b.then(Commands.literal("generate_lang_template")
                .then(Commands.argument("lang", StringArgumentType.word())
                        .requires((source) -> source.getServer().isSingleplayer())
                        .executes(new GenerateKubeJSLangCommand())));
        dis.register(b);
    }

}
