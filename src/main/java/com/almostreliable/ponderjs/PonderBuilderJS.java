package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.api.AbstractPonderBuilder;
import com.almostreliable.ponderjs.util.PonderErrorHelper;
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

    public PonderBuilderJS scene(String name, String title, PonderStoryBoardEntry.PonderStoryBoard scene) {
        return scene(name, title, BASIC_STRUCTURE, scene);
    }

    public PonderBuilderJS scene(String name, String title, String structureName, PonderStoryBoardEntry.PonderStoryBoard scene) {
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
        private final PonderStoryBoardEntry.PonderStoryBoard storyBoard;

        protected PonderStoryBoardWrapper(PonderStoryBoardEntry.PonderStoryBoard storyBoard) {
            this.storyBoard = storyBoard;
        }

        @Override
        public void program(SceneBuilder scene, SceneBuildingUtil util) {
            try {
                storyBoard.program(scene, util);
            } catch (Exception e) {
                PonderErrorHelper.yeet(e);
            }
        }
    }
}
