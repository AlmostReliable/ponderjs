onEvent("ponder.tag", (event) => {
    event.createTag("lytho:ponder_test", "minecraft:shears", "Some testing", "Some test description!", [
        "minecraft:blaze_powder",
        "minecraft:shears",
    ]);

    event.add("kinetic_appliances", "minecraft:dirt");
    event.remove("kinetic_appliances", ["create:deployer", "create:encased_fan"]);
});

onEvent("ponder.registry", (event) => {
    event
        .create("minecraft:shears")
        .tag("lytho:ponder_test")
        .scene("shear_sheep", "How to shear a sheep", (scene, util) => {
            scene.showStructure();
            scene.idle(10);

            const centerBlockPos = util.grid.at(2, 0, 2);
            const centerTop = util.vector.topOf(centerBlockPos);

            const entity = scene.world.createEntity("sheep", centerTop);

            scene.idle(10);
            /**
             * down, up, left, right to set where it should point at
             */
            scene.showControls(120, centerBlockPos.above(2), "down").rightClick().withItem("shears");

            scene.addKeyframe();
            /**
             *  [2.5, 2.5, 2.5] -> position at [x, y, z]
             */
            scene.text(50, "Right-click to shear the sheep", [2.5, 2.5, 2.5]).placeNearTarget();
            scene.idle(60);

            scene.addKeyframe();
            scene.idle(10);
            scene
                .text(40, "Shearing will drop 1 - 3 wool of the corresponding color", centerBlockPos.above(2))
                .placeNearTarget();

            scene.world.modifyEntity(entity, (e) => {
                e.setSheared(true);
            });
            scene.playSound("entity.sheep.shear", 1);

            /**
             * The first argument is the position, the second one the motion vector. I tried to simulate "shearing" :D
             */
            scene.world.createItemEntity(centerTop.add(0, 0.5, 0), util.vector.of(-0.07, 0.4, 0), "white_wool");
            scene.world.createItemEntity(centerTop.add(0, 0.5, 0), util.vector.of(-0.07, 0.4, -0.07), "white_wool");
            scene.world.createItemEntity(centerTop.add(0, 0.5, 0), util.vector.of(0, 0.4, -0.07), "white_wool");

            scene.idle(60);
        });
});
