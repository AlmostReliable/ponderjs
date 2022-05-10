package com.kotakotik.ponderjs.mixin;

import com.kotakotik.ponderjs.api.BlockStateFunction;
import com.kotakotik.ponderjs.api.BlockStateSupplier;
import com.simibubi.create.foundation.ponder.*;
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
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

@SuppressWarnings("unused")
@Mixin(SceneBuilder.class)
public abstract class SceneBuilderMixin {

    @Shadow(remap = false)
    @Final
    public SceneBuilder.WorldInstructions world;

    @Shadow(remap = false)
    @Final
    public SceneBuilder.OverlayInstructions overlay;

    @Shadow(remap = false)
    @Final
    public SceneBuilder.DebugInstructions debug;

    @Shadow(remap = false)
    @Final
    public SceneBuilder.EffectInstructions effects;

    @Shadow(remap = false)
    @Final
    public SceneBuilder.SpecialInstructions special;

    @Shadow(remap = false)
    @Final
    private PonderScene scene;

    @Shadow(remap = false)
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

    @RemapForJS("showStructure")
    public void ponderjs$showStructure() {
        ponderjs$showStructure(scene.getBasePlateSize() * 2);
    }

    @RemapForJS("showStructure")
    public void ponderjs$showStructure(int height) {
        BlockPos start = new BlockPos(scene.getBasePlateOffsetX(), 0, scene.getBasePlateOffsetZ());
        Vec3i size = new Vec3i(scene.getBasePlateSize() - 1, height, scene.getBasePlateSize() - 1);
        Selection selection = scene.getSceneBuildingUtil().select.cuboid(start, size);
        world.showSection(selection, Direction.UP);
    }

    @RemapForJS("text")
    public TextWindowElement.Builder ponderjs$text(int duration, String text) {
        return overlay.showText(duration).text(text);
    }

    @RemapForJS("text")
    public TextWindowElement.Builder ponderjs$text(int duration, String text, Vec3 position) {
        return overlay.showText(duration).text(text).pointAt(position);
    }

    @RemapForJS("sharedText")
    public TextWindowElement.Builder ponderjs$sharedText(int duration, ResourceLocation key) {
        return overlay.showText(duration).sharedText(key);
    }

    @RemapForJS("sharedText")
    public TextWindowElement.Builder ponderjs$sharedText(int duration, ResourceLocation key, Vec3 position) {
        return overlay.showText(duration).sharedText(key).pointAt(position).colored(PonderPalette.BLUE);
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
        @Shadow(remap = false)
        public abstract ElementLink<EntityElement> createEntity(Function<Level, Entity> factory);

        @Shadow(remap = false)
        public abstract void modifyTileNBT(Selection selection, Class<? extends BlockEntity> teType, Consumer<CompoundTag> consumer, boolean reDrawBlocks);

        @Shadow(remap = false)
        public abstract void setBlocks(Selection selection, BlockState state, boolean spawnParticles);

        @Shadow(remap = false)
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
        @Shadow(remap = false)
        public abstract void movePointOfInterest(BlockPos location);
    }
}
