package mod.yourname.yourmodid.register;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import mod.yourname.yourmodid.BuildConfig;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class ModPonder {
    public static void register() {
        // Make sure to include your modid in your ponder id! Otherwise it won't generate the lang!
    }

    public static void generateLang(CreateRegistrate registrate, GatherDataEvent event) {
        register();
        PonderRegistry.provideLangEntries().getAsJsonObject().entrySet().forEach(e -> {
            String k = e.getKey();
            String v = e.getValue().getAsString();
            if (k.contains(BuildConfig.MODID + ".")) {
                registrate.addRawLang(k, v);
            }
        });
    }
}
