package com.almostreliable.ponderjs.extension;

import com.almostreliable.ponderjs.TextElementBuilderJS;
import com.almostreliable.ponderjs.particles.ParticleInstructions;
import com.google.common.base.Preconditions;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.InputElementBuilder;
import net.createmod.ponder.api.element.TextElementBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.foundation.PonderScene;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

@RemapPrefixForJS("ponderjs$")
public interface SceneBuilderExtension {

    @HideFromJS
    SceneBuilder ponderjs$self();

    @HideFromJS
    PonderScene ponderjs$getScene();

    ParticleInstructions ponderjs$getParticles();

    default void ponderjs$showStructure() {
        ponderjs$showStructure(ponderjs$getScene().getBasePlateSize() * 2);
    }

    default void ponderjs$showStructure(int height) {
        var start = new BlockPos(ponderjs$getScene().getBasePlateOffsetX(),
                0,
                ponderjs$getScene().getBasePlateOffsetZ());
        var size = start.offset(ponderjs$getScene().getBasePlateSize() - 1,
                height,
                ponderjs$getScene().getBasePlateSize() - 1);
        var selection = ponderjs$getScene().getSceneBuildingUtil().select().cuboid(start, size);
        ponderjs$self().world().showSection(selection, Direction.UP);
    }

    default void ponderjs$encapsulateBounds(BlockPos size) {
        ponderjs$self().addInstruction(ps -> {
            ps.getWorld().getBounds().encapsulate(size);
        });
    }

    @SuppressWarnings("ConstantValue")
    default void ponderjs$playSound(SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {
        Preconditions.checkArgument(soundEvent != null, "Given sound does not exist");

        ponderjs$self().addInstruction(ps -> {
            if (Minecraft.getInstance().player == null) {
                return;
            }

            var sound = new SimpleSoundInstance(soundEvent,
                    soundSource,
                    volume,
                    pitch,
                    SoundInstance.createUnseededRandom(),
                    Minecraft.getInstance().player.blockPosition());
            Minecraft.getInstance().getSoundManager().play(sound);
        });
    }

    default void ponderjs$playSound(SoundEvent soundEvent, float volume) {
        ponderjs$playSound(soundEvent, SoundSource.MASTER, volume, 1);
    }

    default void ponderjs$playSound(SoundEvent soundEvent) {
        ponderjs$playSound(soundEvent, SoundSource.MASTER, 1, 1);
    }

    default TextElementBuilderJS ponderjs$text(int duration, Component component) {
        var overlay = (OverlayInstructionExtension) ponderjs$self().overlay();
        return overlay.ponderjs$showText(duration).text(component);
    }

    default TextElementBuilderJS ponderjs$text(int duration, Component component, Vec3 position) {
        var overlay = (OverlayInstructionExtension) ponderjs$self().overlay();
        return overlay.ponderjs$showText(duration).text(component).pointAt(position);
    }

    default TextElementBuilderJS ponderjs$sharedText(int duration, ResourceLocation key) {
        var overlay = (OverlayInstructionExtension) ponderjs$self().overlay();
        return overlay.ponderjs$showText(duration).sharedText(key);
    }

    default TextElementBuilder ponderjs$sharedText(int duration, ResourceLocation key, Vec3 position) {
        return ponderjs$self()
                .overlay()
                .showText(duration)
                .sharedText(key)
                .pointAt(position)
                .colored(PonderPalette.BLUE);
    }

    default InputElementBuilder ponderjs$showControls(int duration, Vec3 pos, Pointing pointing) {
        return ponderjs$self().overlay().showControls(pos, pointing, duration);
    }
}
