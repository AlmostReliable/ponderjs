package com.almostreliable.ponderjs.mixin;

import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.foundation.element.TextWindowElement;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(TextWindowElement.class)
public interface TextWindowElementAccessor {

    @Accessor
    void setY(int y);

    @Accessor
    void setTextGetter(Supplier<String> text);

    @Accessor
    void setVec(Vec3 vec);

    @Accessor
    void setPalette(PonderPalette palette);

    @Accessor
    void setNearScene(boolean nearScene);
}
