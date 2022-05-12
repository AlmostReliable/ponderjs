package com.almostreliable.ponderjs.particles;

import com.almostreliable.ponderjs.mixin.ParticleAccessor;
import com.almostreliable.ponderjs.mixin.PonderWorldAccessor;
import com.almostreliable.ponderjs.util.BlockStateSupplier;
import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.contraptions.fluids.particle.FluidParticleData;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.instruction.TickingInstruction;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.rhino.mod.util.color.Color;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

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
                "Particle type " + (type == null ? "INVALID" : Registry.PARTICLE_TYPE.getKey(type)) +
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

    public ParticleDataBuilder.Static block(int ticks, BlockStateSupplier blockState, Vec3 pos) {
        BlockParticleOption options = new BlockParticleOption(ParticleTypes.BLOCK, blockState.get());
        return create(ticks, pos, new ParticleDataBuilder.Static(options));
    }

    public ParticleDataBuilder<?, ?> fluid(int ticks, FluidStackJS fluid, Vec3 pos) {
        var archFluidStack = fluid.getFluidStack();
        FluidStack fs = new FluidStack(archFluidStack.getFluid(),
                (int) archFluidStack.getAmount(),
                archFluidStack.getTag());
        FluidParticleData data = new FluidParticleData(AllParticleTypes.FLUID_PARTICLE.get(), fs);
        return create(ticks, pos, new ParticleDataBuilder.Static(data));
    }

    public ParticleDataBuilder<?, ?> drip(int ticks, FluidStackJS fluid, Vec3 pos) {
        var archFluidStack = fluid.getFluidStack();
        FluidStack fs = new FluidStack(archFluidStack.getFluid(),
                (int) archFluidStack.getAmount(),
                archFluidStack.getTag());
        FluidParticleData data = new FluidParticleData(AllParticleTypes.FLUID_DRIP.get(), fs);
        return create(ticks, pos, new ParticleDataBuilder.Static(data));
    }

    public ParticleDataBuilder<?, ?> basin(int ticks, FluidStackJS fluid, Vec3 pos) {
        var archFluidStack = fluid.getFluidStack();
        FluidStack fs = new FluidStack(archFluidStack.getFluid(),
                (int) archFluidStack.getAmount(),
                archFluidStack.getTag());
        FluidParticleData data = new FluidParticleData(AllParticleTypes.BASIN_FLUID.get(), fs);
        return create(ticks, pos, new ParticleDataBuilder.Static(data));
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
            super.tick(scene);
            int currentTick = totalTicks - remainingTicks;
            for (int i = 0; i < builder.density; i++) {
                Vec3[] data = new Vec3[]{ origin, Vec3.ZERO };
                for (ParticleTransformation transformation : transformations) {
                    float partialTicks = currentTick + ((float) i / builder.density);
                    data = transformation.apply(partialTicks, data[0], data[1]);
                }

                Vec3 pos = data[0];
                Vec3 motion = data[1];
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
                    long argb = builder.color.getArgbKJS();
                    float a = (float) (argb >> 24 & 255) / 255.0F;
                    float r = (float) (argb >> 16 & 255) / 255.0F;
                    float g = (float) (argb >> 8 & 255) / 255.0F;
                    float b = (float) (argb & 255) / 255.0F;

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
