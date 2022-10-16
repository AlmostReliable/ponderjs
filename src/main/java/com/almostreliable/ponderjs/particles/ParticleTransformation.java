package com.almostreliable.ponderjs.particles;

import com.almostreliable.ponderjs.util.PonderErrorHelper;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Context;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

@FunctionalInterface
public interface ParticleTransformation {
    static ParticleTransformation onlyPosition(Simple transformation) {
        return (partialTick, position, motion) -> new Data(transformation.apply(partialTick, position), motion);
    }

    static ParticleTransformation onlyMotion(Simple transformation) {
        return (partialTick, position, motion) -> new Data(position, transformation.apply(partialTick, motion));
    }

    Data apply(float partialTick, Vec3 position, Vec3 motion);

    @FunctionalInterface
    interface Simple {
        Vec3 apply(float partialTick, Vec3 vec);
    }

    record Data(Vec3 position, Vec3 motion) {
        public static Data of(@Nullable Object o) {
            if (o instanceof List<?> list && list.size() >= 2) {
                Vec3 pos = UtilsJS.vec3Of(list.get(0));
                Vec3 motion = UtilsJS.vec3Of(list.get(1));
                return new Data(pos, motion);
            }

            IllegalArgumentException e = new IllegalArgumentException(
                    "Invalid format for particle transformation data. Please use [position, motion] or [[x, y, z], [mx, my, mz]]");
            PonderErrorHelper.yeet(e);
            return new Data(Vec3.ZERO, Vec3.ZERO);
        }
    }
}
