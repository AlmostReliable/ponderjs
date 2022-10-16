/**
 * Helper function for fading in a section.
 *
 * scene => the scene to fade in the section in.
 * section => the section to fade in.
 * movingOffset => the offset to move the section by. (Not a position!)
 * direction => fade direction.
 * idleTicks => number of ticks to idle.
 */
function fadeInSection(scene, selection, movingOffset, direction, idleTicks) {
    let link = scene.world.showIndependentSection(selection, direction);
    scene.world.moveSection(link, movingOffset, 0); // 0 to make moving instant
    scene.idle(idleTicks);
    scene.world.hideIndependentSection(link, direction);
    scene.idle(idleTicks);
}

Ponder.registry((event) => {
    event.create("minecraft:hopper").scene("section_fading", "Let's fade", (scene, util) => {

        /**
         * We will use these blocks for fading. So we don't show them directly.
         * 
         * If you are using a custom structure, you can directly add the blocks to your structure file.
         */
        scene.world.setBlocks([4, 1, 2], "minecraft:dispenser");
        scene.world.setBlocks([3, 1, 2], "minecraft:chest");
        scene.world.setBlocks([2, 1, 2], "minecraft:dropper");

        scene.world.setBlocks([2, 2, 2], "minecraft:hopper");

        /**
         * We only want to show the base plate and the hopper we manually placed right now!
         */
        scene.showBasePlate();
        scene.world.showSection([2, 2, 2], Facing.DOWN);
        scene.idle(20);

        fadeInSection(scene, [4, 1, 2], [-2, 0, 0], Direction.EAST, 15);
        fadeInSection(scene, [3, 1, 2], [-1, 0, 0], Direction.EAST, 15);
        fadeInSection(scene, [2, 1, 2], [0, 0, 0], Direction.EAST, 15);
    });
});

Ponder.registry((event) => {
    event
        .create("minecraft:cake")
        .scene("animate_section", "The cake is a lie.", "ponderjs:the_cake_is_a_lie", (scene, util) => {
            /**
             * Layer 0
             */
            for (let x = 0; x < 5; x++) {
                for (let z = 0; z < 5; z++) {
                    scene.world.showSection([x, 0, z], Facing.DOWN);
                }
                /**
                 * With idle we can can create a cool animation.
                 */
                scene.idle(3);
            }

            /**
             * Layer 1
             */
            for (let z = 0; z < 5; z++) {
                for (let x = 0; x < 5; x++) {
                    scene.world.showSection([x, 1, z], Facing.DOWN);
                }
                scene.idle(3);
            }

            /**
             * Layer 2
             */
            for (let x = 0; x < 5; x++) {
                for (let z = 0; z < 5; z++) {
                    scene.world.showSection([x, 2, z], Facing.DOWN);
                    scene.idle(2);
                }
            }

            /**
             * Top layer
             */
            for (let x = 0; x < 5; x++) {
                for (let z = 0; z < 5; z++) {
                    scene.world.showSection([x, 3, z], Facing.DOWN);
                    scene.idle(1);
                }
            }

            scene.text(30, "What a great cake!", [2.5, 3.5, 2.5]);
            scene.idle(40);

            scene.world.hideSection(
                [
                    [0, 0, 0],
                    [1, 4, 1],
                ],
                Facing.NORTH
            );
            scene.text(30, "Yummy!", [1, 1.5, 2.5]).colored(PonderPalette.MEDIUM);
            scene.idle(40);
        });
});
