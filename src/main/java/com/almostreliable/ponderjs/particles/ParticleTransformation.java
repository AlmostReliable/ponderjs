package com.almostreliable.ponderjs.particles;

import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface ParticleTransformation {
    static ParticleTransformation onlyPosition(Simple transformation) {
        return (partialTick, position, motion) -> new Vec3[]{ transformation.apply(partialTick, position), motion };
    }

    static ParticleTransformation onlyMotion(Simple transformation) {
        return (partialTick, position, motion) -> new Vec3[]{ position, transformation.apply(partialTick, motion) };
    }

    Vec3[] apply(float partialTick, Vec3 position, Vec3 motion);

    @FunctionalInterface
    interface Simple {
        Vec3 apply(float partialTick, Vec3 vec);
    }
}
