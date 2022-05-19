package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.api.AbstractPonderBuilder;
import com.almostreliable.ponderjs.api.ExtendedPonderStoryBoard;
import com.almostreliable.ponderjs.api.ExtendedSceneBuilder;
import com.almostreliable.ponderjs.api.SceneBuildingUtilDelegate;
import com.almostreliable.ponderjs.mixin.SceneBuilderAccessor;
import com.almostreliable.ponderjs.util.PonderErrorHelper;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Set;

public class PonderBuilderJS extends AbstractPonderBuilder<PonderBuilderJS> {
    public static final String BASIC_STRUCTURE = "ponderjs:basic";

    public PonderBuilderJS(Set<Item> items) {
        super(items);
    }

    public PonderBuilderJS scene(String name, String title, ExtendedPonderStoryBoard scene) {
        return scene(name, title, BASIC_STRUCTURE, scene);
    }

    public PonderBuilderJS scene(String name, String title, String structureName, ExtendedPonderStoryBoard scene) {
        ResourceLocation id = createTitleTranslationKey(name);

        PonderStoryBoardWrapper wrapper = new PonderStoryBoardWrapper(scene);
        for (var item : items) {
            addNamedStoryBoard(id, title, item, PonderJS.appendKubeToId(structureName), wrapper);
        }

        return this;
    }

    @Override
    public PonderBuilderJS getSelf() {
        return this;
    }

    public static class PonderStoryBoardWrapper implements PonderStoryBoardEntry.PonderStoryBoard {
        private final ExtendedPonderStoryBoard storyBoard;

        protected PonderStoryBoardWrapper(ExtendedPonderStoryBoard storyBoard) {
            this.storyBoard = storyBoard;
        }

        @Override
        public void program(SceneBuilder builder, SceneBuildingUtil util) {
            try {
                PonderScene scene = ((SceneBuilderAccessor) builder).ponderjs$getPonderScene();
                ExtendedSceneBuilder extended = new ExtendedSceneBuilder(scene);
                storyBoard.program(extended, new SceneBuildingUtilDelegate(util));
            } catch (Exception e) {
                PonderErrorHelper.yeet(e);
            }
        }
    }
}
