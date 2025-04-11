package com.almostreliable.ponderjs.mixin;

import com.almostreliable.ponderjs.PonderJS;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.foundation.registration.PonderSceneRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(PonderSceneRegistry.class)
public class PonderSceneRegistryMixin {

    @ModifyVariable(method = "compileScene", at = @At("HEAD"), ordinal = 0, argsOnly = true, remap = false)
    private static @Nullable PonderLevel ponderjs$createLevelIfNotExist(@Nullable PonderLevel level) {
        if (!PonderJS.ON_RELOAD) {
            return level;
        }

        if (level != null) {
            return level;
        }

        try {
            //noinspection DataFlowIssue
            return new PonderLevel(BlockPos.ZERO, Minecraft.getInstance().level);
        } catch (Exception e) {
            PonderJS.LOGGER.error("Couldn't reload ponderjs", e);
            return level;
        }
    }
}
