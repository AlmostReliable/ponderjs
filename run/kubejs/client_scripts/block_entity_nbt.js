Ponder.registry((event) => {
    event
        .create("minecraft:paper")
        .scene("block_entity_nbt", "Set NBT for blocks", "ponderjs:block_entity_tutorial", (scene, util) => {
            scene.showStructure();
            scene.scaleSceneView(0.90);
            scene.setSceneOffsetY(-1)
            scene.idle(20);

            scene.world.modifyBlockEntityNBT([2, 3, 3], (nbt) => {
                nbt.Patterns = [
                    {
                        Color: 0,
                        Pattern: "pig"
                    }
                ]
            });

            scene.world.modifyBlockEntityNBT([3, 3, 2], (nbt) => {
                nbt.Patterns = [
                    {
                        Color: 0,
                        Pattern: "cre"
                    }
                ]
            });
        });
});
