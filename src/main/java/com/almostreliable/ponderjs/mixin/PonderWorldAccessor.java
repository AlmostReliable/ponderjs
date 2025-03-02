package com.almostreliable.ponderjs.mixin;

import net.createmod.ponder.api.level.PonderLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PonderLevel.class)
public interface PonderWorldAccessor {

    @Invoker(value = "makeParticle", remap = false)
    <T extends ParticleOptions> Particle ponderjs$makeParticle(T data, double x, double y, double z, double mx, double my, double mz);
}
