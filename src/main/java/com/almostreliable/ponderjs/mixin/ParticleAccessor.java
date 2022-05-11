package com.almostreliable.ponderjs.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {

    @Accessor("hasPhysics")
    void ponderjs$setHasPhysics(boolean hasPhysics);

    @Accessor("gravity")
    void ponderjs$setGravity(float gravity);

    @Accessor("stoppedByCollision")
    void ponderjs$setStoppedByCollision(boolean stoppedByCollision);

    @Accessor("roll")
    void ponderjs$setRoll(float roll);

    @Accessor("friction")
    void ponderjs$setFriction(float friction);

    @Accessor("alpha")
    void ponderjs$setAlpha(float alpha);

    @Accessor("lifetime")
    void ponderjs$setLifetime(int lifetime);
}
