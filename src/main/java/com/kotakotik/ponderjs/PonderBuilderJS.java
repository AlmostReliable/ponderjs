package com.kotakotik.ponderjs;

import com.kotakotik.ponderjs.api.AbstractPonderBuilder;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import net.minecraft.world.item.Item;

import java.util.Set;

public class PonderBuilderJS extends AbstractPonderBuilder<PonderBuilderJS> {
    public static final String BASIC_STRUCTURE = "ponderjs:basic";

    public PonderBuilderJS(String name, Set<Item> items) {
        super(PonderJS.appendKubeToId(name), items);
        String namespace = this.name.getNamespace();
        PonderJS.namespaces.add(namespace);
    }

    public PonderBuilderJS scene(String name, String title, PonderStoryBoardEntry.PonderStoryBoard scene) {
        return scene(name, title, BASIC_STRUCTURE, scene);
    }

    public PonderBuilderJS scene(String name, String title, String structureName, PonderStoryBoardEntry.PonderStoryBoard scene) {
        String translationKey = createTitleTranslationKey(name);

        PonderStoryBoardWrapper wrapper = new PonderStoryBoardWrapper(scene);
        for (var item : items) {
            addNamedStoryBoard(translationKey, title, item, PonderJS.appendKubeToId(structureName), wrapper);
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
