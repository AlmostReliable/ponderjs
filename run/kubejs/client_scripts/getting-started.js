Ponder.tags((event) => {
    /**
     * "kubejs:getting_started" -> The tag name
     * "minecraft:paper"        -> The icon
     * "Getting Started"        -> The title
     * "This is a description"  -> The description
     * [...items]               -> Default items
     */
    event.createTag("kubejs:getting_started", "minecraft:paper", "Getting started.", "We ponder now!", [
        // some default items!
        "minecraft:paper",
        "minecraft:apple",
        "minecraft:emerald_block",
    ]);
});

Ponder.registry((event) => {
    event.create("minecraft:paper").scene("our_first_scene", "First example scene", (scene, util) => {
        /**
         * Show the full strucutre.
         * Alternative you can just use `scene.showBasePlate()` to only show the base plate.
         * This is useful for animateing different parts from the structure.
         */
        scene.showStructure();

        /**
         * Encapsulate the structure bounds to given positions. This is useful if the custom structure has no proper bounds.
         * scene.showStructure() automatically encapsulates the bounds.
         */
        // scene.encapsulateBounds(blockPos)

        /**
         * Use idle(ticks) or idleSeconds(seconds) to wait for a certain amount of time.
         */
        scene.idle(10);

        /**
         * [x, y, z] is the position. You can use any kubejs way to represent a position.
         *
         * `.createEntity()` returns an entity link from Create,
         * which will be used in the future refer the entity.
         * Please dont modify the entity directly.
         */
        const creeperLink = scene.world.createEntity("creeper", [2.5, 1, 2.5]);

        /**
         * 50 -> The tick length of the instruction.
         * [x, y, z] -> The position where the text should point at.
         */
        scene
            .text(60, "Example text", [2.0, 2.5, 2.5])
            /**
             * Optional. Set the color of the text.
             * Possible values:
             *      - PonderPalette.WHITE, PonderPalette.BLACK
             *      - PonderPalette.RED, PonderPalette.GREEN, PonderPalette.BLUE
             *      - PonderPalette.SLOW, PonderPalette.MEDIUM, PonderPalette.FAST
             *      - PonderPalette.INPUT, PonderPalette.OUTPUT
             */
            .colored(PonderPalette.RED)
            /**
             * Optional. Will place the text closer to the target position.
             */
            .placeNearTarget()
            /**
             * Optional. Will add a keyframe to the scene.
             */
            .attachKeyFrame();

        /**
         * 120 -> The tick length of the instruction.
         * [x, y, z] -> The position where the controls should point at.
         * "down" -> The direction where the controls should point at.
         */
        scene
            .showControls(60, [2.5, 3, 2.5], "down")
            /**
             * Use mouse right click as icon. Alternative you can use `.leftClick()`,
             * or `.showing(icon)` with a custom icon.
             */
            .rightClick()
            /**
             * Which item should be shown together with the icon
             */
            .withItem("shears")
            /**
             * Optional. You cannot use `.whileSneaking()` and `withCTRL()` together.
             */
            .whileSneaking()
            /**
             * Optional
             */
            .whileCTRL();
    });
});

// Ponder.registry((event) => {
//     /**
//      * Additional structure
//      */
//     event
//         .create("minecraft:paper")
//         .scene(
//             "our_first_scene",
//             "Example scene for paper with structure",
//             "kubejs:your_structure_id",
//             (scene, util) => {
//                 // your scene code here
//             }
//         );
// });
