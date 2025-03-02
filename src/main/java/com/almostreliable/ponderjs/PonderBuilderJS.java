package com.almostreliable.ponderjs;

import com.almostreliable.ponderjs.util.PonderErrorHelper;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.PonderStoryBoard;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class PonderBuilderJS {
    public static final ResourceLocation BASIC_STRUCTURE = new ResourceLocation("ponderjs:basic");
    private final Set<ResourceLocation> itemIds;
    private final PonderSceneRegistrationHelper<ResourceLocation> helper;

    public PonderBuilderJS(Set<ResourceLocation> itemIds, PonderSceneRegistrationHelper<ResourceLocation> helper) {
        this.itemIds = itemIds;
        this.helper = helper;
    }

    public PonderBuilderJS scene(String name, String title, PonderStoryBoard scene) {
        return scene(name, title, BASIC_STRUCTURE, scene);
    }

    public PonderBuilderJS scene(String name, String title, ResourceLocation structureName, PonderStoryBoard storyBoard, ResourceLocation... tags) {
        ResourceLocation id = PonderJS.appendKubeToId(name);

        PonderStoryBoard wrapper = (scene, util) -> {
            scene.title(id.getPath(), title);
            try {
                storyBoard.program(scene, util);
            } catch (Exception e) {
                PonderErrorHelper.yeet(e);
            }
        };

        for (var itemId : itemIds) {
            helper.addStoryBoard(itemId, PonderJS.appendKubeToId(structureName.toString()), wrapper, tags);
        }

        return this;
    }
}
