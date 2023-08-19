package com.almostreliable.ponderjs.util;

import com.simibubi.create.content.fluids.particle.FluidParticleData;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Stream;

/**
 * Helper class to hopefully make it easier merging new stuff between fabric and forge
 */
public class PonderPlatform {
    public static FluidParticleData createFluidParticleData(FluidStackJS fluid, ParticleType<?> type) {
        var archFluidStack = fluid.getFluidStack();
        FluidStack fs = new FluidStack(archFluidStack.getFluid(),
                (int) archFluidStack.getAmount(),
                archFluidStack.getTag());
        return new FluidParticleData(type, fs);
    }

    public static Stream<ParticleType<?>> getParticleTypes() {
        return ForgeRegistries.PARTICLE_TYPES.getValues().stream();
    }

    public static ResourceLocation getParticleTypeName(ParticleType<?> particleType) {
        return ForgeRegistries.PARTICLE_TYPES.getKey(particleType);
    }

    public static ResourceLocation getBlockName(Block block) {
        return block.getRegistryName();
    }

    public static ResourceLocation getEntityTypeName(EntityType<?> entityType) {
        return entityType.getRegistryName();
    }

    public static ResourceLocation getItemName(Item item) {
        return item.getRegistryName();
    }
}
