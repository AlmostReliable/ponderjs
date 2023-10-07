package com.almostreliable.ponderjs.util;

import com.simibubi.create.content.fluids.particle.FluidParticleData;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

/**
 * Helper class to hopefully make it easier merging new stuff between fabric and forge
 */
public class PonderPlatform {
    public static FluidParticleData createFluidParticleData(FluidStackJS fluid, ParticleType<?> type) {
//        var archFluidStack = fluid.getFluidStack();
//        FluidStack fs = new FluidStack(archFluidStack.getFluid(),
//                (int) archFluidStack.getAmount(),
//                archFluidStack.getTag());
//        return new FluidParticleData(type, fs);
        throw new UnsupportedOperationException("PonderJS fluid particles are currently not supported for fabric");
    }

    public static Stream<ParticleType<?>> getParticleTypes() {
        return Registry.PARTICLE_TYPE.stream();
    }

    public static ResourceLocation getParticleTypeName(ParticleType<?> particleType) {
        return Registry.PARTICLE_TYPE.getKey(particleType);
    }

    public static ResourceLocation getBlockName(Block block) {
        return Registry.BLOCK.getKey(block);
    }

    public static ResourceLocation getEntityTypeName(EntityType<?> entityType) {
        return Registry.ENTITY_TYPE.getKey(entityType);
    }

    public static ResourceLocation getItemName(Item item) {
        return Registry.ITEM.getKey(item);
    }
}
