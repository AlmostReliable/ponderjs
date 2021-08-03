package com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import com.simibubi.create.foundation.ponder.content.PonderPalette;
import com.simibubi.create.foundation.ponder.elements.TextWindowElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("pondermaker.ISceneBuilder")
@ZenWrapper(wrappedClass = "com.simibubi.create.foundation.ponder.elements.TextWindowElement.Builder", creationMethodFormat = "new TextWindowElementBuilderWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.TextWindowElementBuilderWrapper")
public interface ITextWindowElementBuilder {
    TextWindowElement.Builder getInternal();

    @ZenCodeType.Method
    default TextWindowElement.Builder colored(PonderPalette color) {
        return getInternal().colored(color);
    }

    @ZenCodeType.Method
    default TextWindowElement.Builder pointAt(Vector3d vec) {
        return getInternal().pointAt(vec);
    }

    @ZenCodeType.Method
    default TextWindowElement.Builder independent(int y) {
        return getInternal().independent(y);
    }

    @ZenCodeType.Method
    default TextWindowElement.Builder independent() {
        return getInternal().independent();
    }

    @ZenCodeType.Method
    default TextWindowElement.Builder text(String defaultText) {
        return getInternal().text(defaultText);
    }

    @ZenCodeType.Method
    default TextWindowElement.Builder sharedText(ResourceLocation key) {
        return getInternal().sharedText(key);
    }

    @ZenCodeType.Method
    default TextWindowElement.Builder sharedText(String key) {
        return getInternal().sharedText(key);
    }

    @ZenCodeType.Method
    default TextWindowElement.Builder placeNearTarget() {
        return getInternal().placeNearTarget();
    }

    @ZenCodeType.Method
    default TextWindowElement.Builder attachKeyFrame() {
        return getInternal().attachKeyFrame();
    }

}
