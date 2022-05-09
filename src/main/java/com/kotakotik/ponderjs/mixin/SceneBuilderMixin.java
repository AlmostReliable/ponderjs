package com.kotakotik.ponderjs.mixin;

import com.kotakotik.ponderjs.api.BlockStateFunction;
import com.kotakotik.ponderjs.api.BlockStateSupplier;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.TextWindowElement;
import com.simibubi.create.foundation.ponder.instruction.PonderInstruction;
import com.simibubi.create.foundation.ponder.instruction.ShowInputInstruction;
import com.simibubi.create.foundation.utility.Pointing;
import dev.latvian.mods.kubejs.block.predicate.BlockIDPredicate;
import dev.latvian.mods.kubejs.entity.EntityJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@Mixin(SceneBuilder.class)
public abstract class SceneBuilderMixin {

    @Shadow
    @Final
    public SceneBuilder.WorldInstructions world;

    @Shadow
    @Final
    public SceneBuilder.OverlayInstructions overlay;

    @Shadow
    @Final
    public SceneBuilder.DebugInstructions debug;

    @Shadow
    @Final
    public SceneBuilder.EffectInstructions effects;

    @Shadow
    @Final
    public SceneBuilder.SpecialInstructions special;

    @Shadow
    public abstract void addInstruction(PonderInstruction instruction);

    @RemapForJS("getWorld")
    public SceneBuilder.WorldInstructions ponderjs$getWorld() {
        return world;
    }

    @RemapForJS("getLevel")
    public SceneBuilder.WorldInstructions ponderjs$getLevel() {
        return world;
    }

    @RemapForJS("getDebug")
    public SceneBuilder.DebugInstructions ponderjs$getDebug() {
        return debug;
    }

    @RemapForJS("getOverlay")
    public SceneBuilder.OverlayInstructions ponderjs$getOverlay() {
        return overlay;
    }

    @RemapForJS("getEffects")
    public SceneBuilder.EffectInstructions ponderjs$getEffects() {
        return effects;
    }

    @RemapForJS("getSpecial")
    public SceneBuilder.SpecialInstructions ponderjs$getSpecial() {
        return special;
    }

    @RemapForJS("showText")
    public TextWindowElement.Builder ponderjs$showText(int duration, String text) {
        return overlay.showText(duration).text(text);
    }

    @RemapForJS("showText")
    public TextWindowElement.Builder ponderjs$showText(int duration, String text, Vec3 position) {
        return overlay.showText(duration).text(text).pointAt(position);
    }

    @RemapForJS("showControls")
    public InputWindowElement ponderjs$showControls(int duration, Vec3 pos, Pointing pointing) {
        InputWindowElement element = new InputWindowElement(pos, pointing);
        // we use own instruction to avoid `element.clone()`
        addInstruction(new ShowInputInstruction(element, duration));
        return element;
    }

    @Mixin(SceneBuilder.WorldInstructions.class)
    private abstract static class WorldInstructionsMixin {
        @Shadow
        public abstract ElementLink<EntityElement> createEntity(Function<Level, Entity> factory);

        @Shadow
        public abstract void modifyTileNBT(Selection selection, Class<? extends BlockEntity> teType, Consumer<CompoundTag> consumer, boolean reDrawBlocks);

        @Shadow
        public abstract void setBlocks(Selection selection, BlockState state, boolean spawnParticles);

        @Shadow
        public abstract void modifyBlocks(Selection selection, UnaryOperator<BlockState> stateFunc, boolean spawnParticles);

        @RemapForJS("createEntity")
        public ElementLink<EntityElement> ponderjs$createEntity(EntityType<?> entityType, Vec3 position, Consumer<EntityJS> consumer) {
            return createEntity(level -> {
                Entity entity = entityType.create(level);
                Objects.requireNonNull(entity, "Could not create entity of type " + entityType.getRegistryName());
                entity.setPosRaw(position.x, position.y, position.z);
                entity.setOldPosAndRot();
                entity.lookAt(EntityAnchorArgument.Anchor.FEET, position.add(0, 0, -1));
                EntityJS entityJS = UtilsJS.getLevel(level).getEntity(entity);
                consumer.accept(entityJS);
                return entity;
            });
        }

        @RemapForJS("createEntity")
        public ElementLink<EntityElement> ponderjs$createEntity(EntityType<?> entityType, Vec3 position) {
            return ponderjs$createEntity(entityType, position, entity -> {
            });
        }

        @RemapForJS("modifyBlocks")
        public void ponderjs$modifyBlocks(Selection pos, BlockStateFunction function) {
            ponderjs$modifyBlocks(pos, true, function);
        }

        @RemapForJS("modifyBlocks")
        public void ponderjs$modifyBlocks(Selection selection, boolean spawnParticles, BlockStateFunction function) {
            modifyBlocks(selection, blockState -> {
                BlockIDPredicate predicate = new BlockIDPredicate(blockState.getBlock().getRegistryName());
                return function.apply(predicate);
            }, spawnParticles);
        }

        @RemapForJS("replaceBlocks")
        public void ponderjs$replaceBlocks(Selection selection, BlockStateFunction function) {
            ponderjs$replaceBlocks(selection, true, function);
        }

        @RemapForJS("replaceBlocks")
        public void ponderjs$replaceBlocks(Selection selection, boolean spawnParticles, BlockStateFunction function) {
            ponderjs$modifyBlocks(selection, spawnParticles, function);
        }

        @RemapForJS("setBlocks")
        public void ponderjs$setBlocks(Selection selection, BlockStateSupplier function) {
            ponderjs$setBlocks(selection, true, function);
        }

        @RemapForJS("setBlocks")
        public void ponderjs$setBlocks(Selection selection, boolean spawnParticles, BlockStateSupplier function) {
            setBlocks(selection, function.get(), spawnParticles);
        }

        @RemapForJS("modifyTileNBT")
        public void ponderjs$modifyTileNBT(Selection selection, Consumer<CompoundTag> consumer) {
            modifyTileNBT(selection, BlockEntity.class, consumer, false);
        }

        @RemapForJS("modifyTileNBT")
        public void ponderjs$modifyTileNBT(Selection selection, Consumer<CompoundTag> consumer, boolean reDrawBlocks) {
            modifyTileNBT(selection, BlockEntity.class, consumer, reDrawBlocks);
        }
    }

    @Mixin(SceneBuilder.SpecialInstructions.class)
    private static abstract class SpecialInstructionsMixin {
        @HideFromJS
        @Shadow
        public abstract void movePointOfInterest(BlockPos location);
    }
}
