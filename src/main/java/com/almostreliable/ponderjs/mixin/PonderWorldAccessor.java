package com.almostreliable.ponderjs.mixin;

import com.simibubi.create.foundation.ponder.PonderWorld;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PonderWorld.class)
public interface PonderWorldAccessor {

    @Invoker(value = "makeParticle", remap = false)
    <T extends ParticleOptions>Particle ponderjs$makeParticle(T data, double x, double y, double z, double mx, double my, double mz);
}
