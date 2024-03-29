package com.almostreliable.ponderjs.particles;

import com.almostreliable.ponderjs.mixin.ParticleAccessor;
import com.almostreliable.ponderjs.mixin.PonderWorldAccessor;
import com.almostreliable.ponderjs.util.PonderErrorHelper;
import com.almostreliable.ponderjs.util.PonderPlatform;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.fluids.particle.FluidParticleData;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.instruction.TickingInstruction;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.rhino.mod.util.color.Color;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ParticleInstructions {
    private final SceneBuilder scene;

    public ParticleInstructions(SceneBuilder scene) {
        this.scene = scene;
    }

    public ParticleDataBuilder<?, ?> simple(int ticks, ParticleType<?> type, Vec3 pos) {
        if (type instanceof SimpleParticleType simple) {
            return create(ticks, pos, new ParticleDataBuilder.Static(simple));
        }

        throw new IllegalArgumentException(
                "Particle type " + (type == null ? "INVALID" : PonderPlatform.getParticleTypeName(type)) +
                " is null or not simple.");
    }

    public ParticleDataBuilder.DustParticleDataBuilder dust(int ticks, Color color, Vec3 pos) {
        return create(ticks, pos, new ParticleDataBuilder.DustParticleDataBuilder(color, null).color(color));
    }

    public ParticleDataBuilder.DustParticleDataBuilder dust(int ticks, Color fromColor, Color toColor, Vec3 pos) {
        return create(ticks, pos, new ParticleDataBuilder.DustParticleDataBuilder(fromColor, toColor).color(fromColor));
    }

    public ParticleDataBuilder.Static item(int ticks, ItemStack item, Vec3 pos) {
        ItemParticleOption options = new ItemParticleOption(ParticleTypes.ITEM, item);
        return create(ticks, pos, new ParticleDataBuilder.Static(options));
    }

    public ParticleDataBuilder.Static block(int ticks, BlockState blockState, Vec3 pos) {
        BlockParticleOption options = new BlockParticleOption(ParticleTypes.BLOCK, blockState);
        return create(ticks, pos, new ParticleDataBuilder.Static(options));
    }

    public ParticleDataBuilder<?, ?> fluid(int ticks, FluidStackJS fluid, Vec3 pos) {
        FluidParticleData data = PonderPlatform.createFluidParticleData(fluid, AllParticleTypes.FLUID_PARTICLE.get());
        return create(ticks, pos, new ParticleDataBuilder.Static(data));
    }

    public ParticleDataBuilder<?, ?> drip(int ticks, FluidStackJS fluid, Vec3 pos) {
        FluidParticleData data = PonderPlatform.createFluidParticleData(fluid, AllParticleTypes.FLUID_DRIP.get());
        return create(ticks, pos, new ParticleDataBuilder.Static(data));
    }

    public ParticleDataBuilder<?, ?> basin(int ticks, FluidStackJS fluid, Vec3 pos) {
        FluidParticleData data = PonderPlatform.createFluidParticleData(fluid, AllParticleTypes.BASIN_FLUID.get());
        return create(ticks, pos, new ParticleDataBuilder.Static(data));
    }

    public ParticleDataBuilder<?, ?> rotationIndicator(int ticks, Vec3 pos, float radius1, float radius2, Direction.Axis axis) {
        return create(ticks, pos, new ParticleDataBuilder.RotationIndicatorParticleDataBuilder(radius1, radius2, axis));
    }

    private <O extends ParticleDataBuilder<O, ?>> O create(int ticks, Vec3 origin, O options) {
        scene.addInstruction(new ParticleInstruction(ticks, origin, options));
        return options;
    }

    public static class ParticleInstruction extends TickingInstruction {
        private final ParticleDataBuilder<?, ?> builder;
        private final Vec3 origin;
        private final List<ParticleTransformation> transformations = new ArrayList<>();
        private ParticleOptions cachedOptions;

        public ParticleInstruction(int ticks, Vec3 origin, ParticleDataBuilder<?, ?> builder) {
            super(false, ticks);
            this.origin = origin;
            this.builder = builder;
        }

        @Override
        protected void firstTick(PonderScene scene) {
            cachedOptions = builder.createOptions();
            transformations.clear();
            transformations.addAll(builder.transformations);
        }

        @Override
        public void tick(PonderScene scene) {
            try {
                super.tick(scene);
                doTick(scene);
            } catch (Exception e) {
                PonderErrorHelper.yeet(e);
                remainingTicks = 0;
            }
        }

        private void doTick(PonderScene scene) {
            int currentTick = totalTicks - remainingTicks;
            for (int i = 0; i < builder.density; i++) {
                var data = new ParticleTransformation.Data(origin, Vec3.ZERO);
                for (ParticleTransformation transformation : transformations) {
                    float partialTicks = currentTick + ((float) i / builder.density);
                    data = transformation.apply(partialTicks, data.position(), data.motion());
                }

                Vec3 pos = data.position();
                Vec3 motion = data.motion();
                Particle particle = ((PonderWorldAccessor) scene.getWorld()).ponderjs$makeParticle(cachedOptions,
                        pos.x,
                        pos.y,
                        pos.z,
                        motion.x,
                        motion.y,
                        motion.z);

                if (particle != null) {
                    applyParticleData(particle);
                    scene.getWorld().addParticle(particle);
                }
            }
        }

        private void applyParticleData(Particle particle) {
            if (particle instanceof ParticleAccessor accessor) {
                if (builder.color != null) {
                    long argb = builder.color.getArgbJS();
                    float a = (argb >> 24 & 255) / 255.0F;
                    float r = (argb >> 16 & 255) / 255.0F;
                    float g = (argb >> 8 & 255) / 255.0F;
                    float b = (argb & 255) / 255.0F;

                    particle.setColor(r, g, b);
                    accessor.ponderjs$setAlpha(a);
                }

                if (builder.scale != null) {
                    particle.scale(builder.scale);
                }

                if (builder.roll != null) {
                    accessor.ponderjs$setRoll(builder.roll);
                }

                if (builder.friction != null) {
                    accessor.ponderjs$setFriction(builder.friction);
                }

                if (builder.gravity != null) {
                    accessor.ponderjs$setGravity(builder.gravity);
                }

                if (builder.physics != null) {
                    accessor.ponderjs$setHasPhysics(builder.physics);
                }

                if (builder.collision != null) {
                    accessor.ponderjs$setStoppedByCollision(builder.collision);
                }

                if (builder.lifetime != null) {
                    accessor.ponderjs$setLifetime(builder.lifetime);
                }
            }
        }
    }
}
