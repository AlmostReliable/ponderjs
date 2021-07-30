package mod.yourname.yourmodid.register;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import mod.yourname.yourmodid.BuildConfig;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItems {
    public static ItemGroup itemGroup = new ItemGroup(BuildConfig.MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllItems.WRENCH.get());
        }
    };

    public static void register(CreateRegistrate registrate) {
        registrate.itemGroup(()->itemGroup, "Create Addon"); // TODO: change this
    }
}
