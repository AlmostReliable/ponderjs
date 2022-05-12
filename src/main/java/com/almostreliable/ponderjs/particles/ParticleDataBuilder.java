package com.almostreliable.ponderjs.particles;

import com.mojang.math.Vector3f;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.particle.RotationIndicatorParticleData;
import dev.latvian.mods.rhino.mod.util.color.Color;
import dev.latvian.mods.rhino.mod.wrapper.ColorWrapper;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
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
    @Nullable Color color = null;
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

    public O color(Color color) {
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
                Create.RANDOM.nextGaussian() * speed.x,
                Create.RANDOM.nextGaussian() * speed.y,
                Create.RANDOM.nextGaussian() * speed.z
        ));
    }

    public O area(Vec3 area) {
        return transformPosition((partialTicks, position) -> new Vec3(
                position.x + (Create.RANDOM.nextFloat() * (area.x - position.x)),
                position.y + (Create.RANDOM.nextFloat() * (area.y - position.y)),
                position.z + (Create.RANDOM.nextFloat() * (area.z - position.z))
        ));
    }

    public O delta(Vec3 delta) {
        return transformPosition((partialTicks, position) -> new Vec3(
                position.x + (Create.RANDOM.nextGaussian() * (delta.x)),
                position.y + (Create.RANDOM.nextGaussian() * (delta.y)),
                position.z + (Create.RANDOM.nextGaussian() * (delta.z))
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
            super();
            this.type = type;
        }

        @Override
        ParticleOptions createOptions() {
            return type;
        }
    }

    public static class DustParticleDataBuilder
            extends ParticleDataBuilder<DustParticleDataBuilder, DustParticleOptionsBase> {
        final Color fromColor;
        @Nullable final Color toColor;

        public DustParticleDataBuilder(Color fromColor, @Nullable Color toColor) {
            this.fromColor = fromColor;
            this.toColor = toColor;
        }

        @Override
        public DustParticleDataBuilder color(Color color) {
            // color is defined through constructor
            return this;
        }

        @Override
        DustParticleOptionsBase createOptions() {
            float s = scale == null ? 1.0f : scale;
            Vector3f fC = new com.simibubi.create.foundation.utility.Color(fromColor.getRgbKJS()).asVectorF();

            if (toColor == null) {
                return new DustParticleOptions(fC, s);
            }

            Vector3f toC = new com.simibubi.create.foundation.utility.Color(toColor.getRgbKJS()).asVectorF();
            return new DustColorTransitionOptions(fC, toC, s);
        }
    }

    public static class RotationIndicatorParticleDataBuilder
            extends ParticleDataBuilder<RotationIndicatorParticleDataBuilder, RotationIndicatorParticleData> {
        private final float radius1;
        private final float radius2;
        private final Direction.Axis axis;
        private float rotationSpeed;

        public RotationIndicatorParticleDataBuilder(float radius1, float radius2, Direction.Axis axis) {
            this.rotationSpeed = 1.0f;
            this.radius1 = radius1;
            this.radius2 = radius2;
            this.axis = axis;
        }

        public RotationIndicatorParticleDataBuilder rotationSpeed(float rotationSpeed) {
            this.rotationSpeed = rotationSpeed;
            return getSelf();
        }

        @Override
        RotationIndicatorParticleData createOptions() {
            Color c = color == null ? ColorWrapper.BLACK : color;
            char axisChar = axis.name().charAt(0);
            int lTime = lifetime == null ? 40 : lifetime;
            return new RotationIndicatorParticleData(c.getRgbKJS(), rotationSpeed, radius1, radius2, lTime, axisChar);
        }
    }
}
