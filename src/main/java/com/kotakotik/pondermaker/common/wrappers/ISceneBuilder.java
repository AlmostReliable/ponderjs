package com.kotakotik.pondermaker.common.wrappers;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("pondermaker.ISceneBuilder")
@ZenWrapper(wrappedClass = "com.simibubi.create.foundation.ponder.SceneBuilder", creationMethodFormat = "new SceneBuilderWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.SceneBuilderWrapper")
public interface ISceneBuilder {
    SceneBuilder getInternal();

    @ZenCodeType.Method
    default void title(String sceneId, String title) {
        getInternal().title(sceneId, title);
    }

    @ZenCodeType.Method
    default void configureBasePlate(int xOffset, int zOffset, int basePlateSize) {
        getInternal().configureBasePlate(xOffset, zOffset, basePlateSize);
    }

    @ZenCodeType.Method
    default void scaleSceneView(float factor) {
        getInternal().scaleSceneView(factor);
    }

    @ZenCodeType.Method
    default void setSceneOffsetY(float yOffset) {
        getInternal().setSceneOffsetY(yOffset);
    }

    @ZenCodeType.Method
    default void showBasePlate() {
        getInternal().showBasePlate();
    }

    @ZenCodeType.Method
    default void idle(int ticks) {
        getInternal().idle(ticks);
    }

    @ZenCodeType.Method
    default void idleSeconds(int seconds) {
        getInternal().idleSeconds(seconds);
    }

    @ZenCodeType.Method
    default void markAsFinished() {
        getInternal().markAsFinished();
    }

    @ZenCodeType.Method
    default void rotateCameraY(float degrees) {
        getInternal().rotateCameraY(degrees);
    }

    @ZenCodeType.Method
    default void addKeyframe() {
        getInternal().addKeyframe();
    }

    @ZenCodeType.Method
    default void addLazyKeyframe() {
        getInternal().addLazyKeyframe();
    }
}
