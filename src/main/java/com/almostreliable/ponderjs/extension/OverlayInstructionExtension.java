package com.almostreliable.ponderjs.extension;

import com.almostreliable.ponderjs.api.CustomPonderOverlayElement;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import net.createmod.ponder.foundation.instruction.TickingInstruction;

@RemapPrefixForJS("ponderjs$")
public interface OverlayInstructionExtension {

    @HideFromJS
    PonderSceneBuilder ponderjs$builder();

    default CustomPonderOverlayElement ponderjs$addElement(int ticks) {
        var element = new CustomPonderOverlayElement();
        ponderjs$builder().addInstruction(new TickingInstruction(false, ticks) {
            @Override
            protected void firstTick(PonderScene scene) {
                super.firstTick(scene);
                scene.addElement(element);
            }
        });

        return element;
    }

    default CustomPonderOverlayElement ponderjs$addElement() {
        var element = new CustomPonderOverlayElement();
        ponderjs$builder().addInstruction(ponderScene -> {
            ponderScene.addElement(element);
        });

        return element;
    }
}
