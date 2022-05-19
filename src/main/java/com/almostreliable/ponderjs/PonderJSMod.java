//package com.almostreliable.ponderjs;
//
//
//import com.almostreliable.ponderjs.commands.GenerateKubeJSLangCommand;
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.arguments.StringArgumentType;
//import com.mojang.brigadier.builder.LiteralArgumentBuilder;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.event.RegisterCommandsEvent;
//import net.minecraftforge.eventbus.api.IEventBus;
//import net.minecraftforge.fml.IExtensionPoint;
//import net.minecraftforge.fml.ModLoadingContext;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//
//@Mod(BuildConfig.MOD_ID)
//public class PonderJSMod {
//
//    public PonderJSMod() {
//        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
//                () -> new IExtensionPoint.DisplayTest(() -> "ANY", (a, b) -> true));
//
//        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
//        modEventBus.addListener(this::ponderClientInit);
//        // I need to refactor this
////        modEventBus.addListener(ModConfigs::onLoad);
////        modEventBus.addListener(ModConfigs::onReload);
////        ModConfigs.register();
//    }
//
//    private void ponderClientInit(FMLClientSetupEvent event) {
//        event.enqueueWork(PonderJS::init);
//    }
//
//    private void registerCommands(RegisterCommandsEvent event) {
//        CommandDispatcher<CommandSourceStack> dis = event.getDispatcher();
//        LiteralArgumentBuilder<CommandSourceStack> b = Commands.literal(BuildConfig.MOD_ID);
//        b.then(Commands.literal("generate_lang_template")
//                .then(Commands.argument("lang", StringArgumentType.word())
//                        .requires((source) -> source.getServer().isSingleplayer())
//                        .executes(new GenerateKubeJSLangCommand())));
//        dis.register(b);
//    }
//}
