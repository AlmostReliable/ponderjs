package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.util.PonderErrorHelper;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.PonderStoryBoard;
import net.createmod.ponder.foundation.PonderStoryBoardEntry;
import net.createmod.ponder.foundation.registration.PonderSceneRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class PonderBuilderJS {
    public static final ResourceLocation BASIC_STRUCTURE = new ResourceLocation("ponderjs:basic");
    private final Set<ResourceLocation> itemIds;
    private final PonderSceneRegistry sceneRegistry;

    public PonderBuilderJS(Set<ResourceLocation> itemIds, PonderSceneRegistry sceneRegistry) {
        this.itemIds = itemIds;
        this.sceneRegistry = sceneRegistry;
    }

    public PonderBuilderJS scene(String name, String title, PonderStoryBoard scene) {
        return scene(name, title, BASIC_STRUCTURE, scene);
    }

    public PonderBuilderJS scene(String name, String title, ResourceLocation structureName, PonderStoryBoard storyBoard, ResourceLocation... tags) {
        ResourceLocation id = PonderJS.appendKubeToId(name);
        PonderJS.NAMESPACES.add(id.getNamespace());

        PonderStoryBoard wrapper = (scene, util) -> {
            scene.title(id.getPath(), title);
            try {
                storyBoard.program(scene, util);
            } catch (Exception e) {
                PonderErrorHelper.yeet(e);
            }
        };

        for (var itemId : itemIds) {
            var storyBoardEntry = new PonderStoryBoardEntry(wrapper, id.getNamespace(), structureName, itemId);
            sceneRegistry.addStoryBoard(storyBoardEntry);
        }

        return this;
    }
}
