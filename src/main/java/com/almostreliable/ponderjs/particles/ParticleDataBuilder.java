package com.almostreliable.ponderjs.particles;

import dev.latvian.mods.kubejs.color.KubeColor;
import net.createmod.ponder.Ponder;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class ParticleDataBuilder<O extends ParticleDataBuilder<O, PO>, PO extends ParticleOptions> {
    final List<ParticleTransformation> transformations = new ArrayList<>();
    int density = 1;
    @Nullable Float gravity = null;
    @Nullable Boolean physics = null;
    @Nullable Boolean collision = null;
    @Nullable KubeColor color = null;
    @Nullable Float roll = null;
    @Nullable Float friction = null;
    @Nullable Float scale = null;
    @Nullable Integer lifetime = null;

    public O density(int density) {
        this.density = density;
        return getSelf();
    }

    public O gravity(float gravity) {
        this.gravity = gravity;
        return getSelf();
    }

    public O physics(boolean physics) {
        this.physics = physics;
        return getSelf();
    }

    public O collision(boolean collision) {
        this.collision = collision;
        return getSelf();
    }

    public O color(KubeColor color) {
        this.color = color;
        return getSelf();
    }

    public O roll(float roll) {
        this.roll = roll;
        return getSelf();
    }

    public O friction(float friction) {
        this.friction = friction;
        return getSelf();
    }

    public O scale(float scale) {
        this.scale = scale;
        return getSelf();
    }

    public O lifetime(int lifetime) {
        this.lifetime = lifetime;
        return getSelf();
    }

    public O motion(Vec3 motion) {
        return transformMotion((partialTicks, m) -> motion);
    }

    public O speed(Vec3 speed) {
        return transformMotion((partialTick, motion) -> new Vec3(
                Ponder.RANDOM.nextGaussian() * speed.x,
                Ponder.RANDOM.nextGaussian() * speed.y,
                Ponder.RANDOM.nextGaussian() * speed.z
        ));
    }

    public O withinBlockSpace() {
        return transformPosition((partialTicks, position) -> new Vec3(
                Math.floor(position.x) + Ponder.RANDOM.nextFloat(),
                Math.floor(position.y) + Ponder.RANDOM.nextFloat(),
                Math.floor(position.z) + Ponder.RANDOM.nextFloat()
        ));
    }

    public O area(Vec3 area) {
        return transformPosition((partialTicks, position) -> new Vec3(
                position.x + (Ponder.RANDOM.nextFloat() * (area.x - position.x)),
                position.y + (Ponder.RANDOM.nextFloat() * (area.y - position.y)),
                position.z + (Ponder.RANDOM.nextFloat() * (area.z - position.z))
        ));
    }

    public O delta(Vec3 delta) {
        return transformPosition((partialTicks, position) -> new Vec3(
                position.x + (Ponder.RANDOM.nextGaussian() * (delta.x)),
                position.y + (Ponder.RANDOM.nextGaussian() * (delta.y)),
                position.z + (Ponder.RANDOM.nextGaussian() * (delta.z))
        ));
    }

    public O transform(ParticleTransformation transformer) {
        transformations.add(transformer);
        return getSelf();
    }

    public O transformPosition(ParticleTransformation.Simple transformer) {
        return transform(ParticleTransformation.onlyPosition(transformer));
    }

    public O transformMotion(ParticleTransformation.Simple transformer) {
        return transform(ParticleTransformation.onlyMotion(transformer));
    }

    abstract PO createOptions();

    @SuppressWarnings("unchecked")
    protected O getSelf() {
        return (O) this;
    }

    public static class Static extends ParticleDataBuilder<Static, ParticleOptions> {
        private final ParticleOptions type;

        public Static(ParticleOptions type) {
            this.type = type;
        }

        @Override
        ParticleOptions createOptions() {
            return type;
        }
    }

    public static class DustParticleDataBuilder
            extends ParticleDataBuilder<DustParticleDataBuilder, ScalableParticleOptionsBase> {
        final KubeColor fromColor;
        @Nullable final KubeColor toColor;

        public DustParticleDataBuilder(KubeColor fromColor, @Nullable KubeColor toColor) {
            this.fromColor = fromColor;
            this.toColor = toColor;
        }

        @Override
        public DustParticleDataBuilder color(KubeColor color) {
            // color is defined through constructor
            return this;
        }

        @Override
        ScalableParticleOptionsBase createOptions() {
            float s = scale == null ? 1.0f : scale;
            var fC = new net.createmod.catnip.theme.Color(fromColor.kjs$getRGB()).asVectorF();

            if (toColor == null) {
                return new DustParticleOptions(fC, s);
            }

            var toC = new net.createmod.catnip.theme.Color(toColor.kjs$getRGB()).asVectorF();
            return new DustColorTransitionOptions(fC, toC, s);
        }
    }
}
