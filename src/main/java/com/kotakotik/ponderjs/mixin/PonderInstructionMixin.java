package com.kotakotik.ponderjs.mixin;

import com.kotakotik.ponderjs.PonderErrorHelper;
import com.simibubi.create.foundation.ponder.PonderScene;
import dev.latvian.mods.rhino.RhinoException;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/**
 * Mixin into the simple PonderInstruction to catch Rhino Exceptions, so we can delegate them to the user.
 */
@Mixin(targets = "com.simibubi.create.foundation.ponder.instruction.PonderInstruction$Simple")
public class PonderInstructionMixin {

    @Shadow
    private Consumer<PonderScene> callback;

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void init(Consumer<PonderScene> argCallback, CallbackInfo ci) {
        callback = ponderScene -> {
            try {
                argCallback.accept(ponderScene);
            } catch (RhinoException e) {
                PonderErrorHelper.yeet(e);
                if (Minecraft.getInstance() != null) {
                    Minecraft.getInstance().setScreen(null);
                }
            }
        };
    }
}
