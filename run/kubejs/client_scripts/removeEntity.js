Ponder.registry((event) => {
    event
        .create("minecraft:dirt")
        .scene("removing_an_entity", "Yeet", (scene, util) => {
            scene.showStructure();
            scene.idle(10);

            const centerBlockPos = util.grid.at(2, 0, 2);
            const centerTop = util.vector.topOf(centerBlockPos);

            const entity = scene.world.createEntity("sheep", centerTop);
            scene.idle(30);
            scene.world.removeEntity(entity);
            scene.idle(60);
        });
});
