package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.mixin.TextWindowElementAccessor;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.TextElementBuilder;
import net.createmod.ponder.foundation.PonderIndex;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.element.TextWindowElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class TextElementBuilderJS implements TextElementBuilder {

    private final TextWindowElement element;
    private final TextWindowElementAccessor accessor;
    private final PonderScene scene;

    public TextElementBuilderJS(TextWindowElement element, PonderScene scene) {
        this.element = element;
        this.accessor = (TextWindowElementAccessor) element;
        this.scene = scene;
    }

    @Override
    public TextElementBuilderJS colored(PonderPalette color) {
        accessor.setPalette(color);
        return this;
    }

    @Override
    public TextElementBuilderJS pointAt(Vec3 vec) {
        accessor.setVec(vec);
        return this;
    }

    @Override
    public TextElementBuilderJS independent(int y) {
        accessor.setY(y);
        return this;
    }

    public TextElementBuilderJS text(Component component) {
        accessor.setTextGetter(component::getString);
        return this;
    }

    public TextElementBuilderJS text(Component component, Object... params) {
        accessor.setTextGetter(component::getString);
        return this;
    }

    @Override
    @HideFromJS
    public TextElementBuilderJS text(String defaultText) {
        throw new IllegalStateException("Use text(Component) instead - should not be called from JS");
    }

    @Override
    @HideFromJS
    public TextElementBuilderJS text(String defaultText, Object... params) {
        throw new IllegalStateException("Use text(Component, ...params) instead - should not be called from JS");
    }

    @Override
    public TextElementBuilderJS sharedText(ResourceLocation key) {
        accessor.setTextGetter(() -> PonderIndex.getLangAccess().getShared(key));
        return this;
    }

    @Override
    public TextElementBuilderJS sharedText(ResourceLocation key, Object... params) {
        accessor.setTextGetter(() -> PonderIndex.getLangAccess().getShared(key, params));
        return this;
    }

    @Override
    @HideFromJS
    public TextElementBuilderJS sharedText(String key) {
        return sharedText(ResourceLocation.fromNamespaceAndPath(scene.getNamespace(), key));
    }

    @Override
    @HideFromJS
    public TextElementBuilderJS sharedText(String key, Object... params) {
        return sharedText(ResourceLocation.fromNamespaceAndPath(scene.getNamespace(), key), params);
    }

    @Override
    public TextElementBuilderJS placeNearTarget() {
        accessor.setNearScene(true);
        return this;
    }

    @Override
    public TextElementBuilderJS attachKeyFrame() {
        scene.builder().addLazyKeyframe();
        return this;
    }
}
