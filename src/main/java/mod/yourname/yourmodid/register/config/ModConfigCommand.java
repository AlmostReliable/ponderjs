package mod.yourname.yourmodid.register.config;

import com.google.common.eventbus.Subscribe;
import com.simibubi.create.foundation.config.ui.BaseConfigScreen;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.TextStencilElement;
import com.simibubi.create.foundation.gui.widgets.BoxWidget;
import mod.yourname.yourmodid.BuildConfig;
import mod.yourname.yourmodid.CreateAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ModConfigCommand {
    public static class ModConfigScreen extends BaseConfigScreen {
        public ModConfigScreen(Screen parent, @Nonnull String modID) {
            super(parent, modID);
        }
    }

    public static ModConfigScreen createScreen(Screen parent) {
        return (ModConfigScreen) new ModConfigScreen(parent, BuildConfig.MODID)
                .withTitles("Client Settings", "World Generation Settings", "Gameplay Settings")
                .withSpecs(null, null, ModConfigs.SERVER.specification);
    }
    public static final String configCommandName = BuildConfig.MODID;

    @SubscribeEvent
    public static void commands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(configCommandName).then(Commands.literal("config").executes((ctx) -> {
            ScreenOpener.open(createScreen(null));
            return 1;
        })));
    }
}
