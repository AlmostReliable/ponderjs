onEvent("ponder.tag", (event) => {
    event.createTag("particle_test", "minecraft:blaze_powder", "All the particles", "Use PonderJS with particles!", [
        "minecraft:blaze_powder",
    ]);
});

const TICK_LENGTH = 20;
const IDLE_TICK_LENGTH = TICK_LENGTH * 3;

onEvent("ponder.registry", (event) => {
    /**
     * Prints all particle names in the client.txt which can be used for `scene.particles.simple()`.
     */
    // event.printParticleNames();

    event
        .create("minecraft:blaze_powder")
        .tag("kubejs:particle_test")
        .scene("particles", "How to particle", (scene, util) => {
            scene.showStructure();
            scene.idle(10);

            const pos = [4, 1.5, 4];
            const start = [0, 1, 0];
            const end = [2, 2, 3];

            /**
             * Using `scene.particles.` returns an object to set additional options
             *
             * .density(number) -> Sets the density of the particles.
             * .gravity(number) -> Sets the gravity of the particles.
             * .physics(true or false) -> Sets if the particles have physics.
             * .collision(true or false) -> Sets if the particles stop by collision.
             * .roll(number) -> Do a barrell roll!
             * .scale(number) -> Scale the particles between 0.01 and 4.
             * .lifetime(number) -> Set the lifetime of the particles.
             *
             * .motion(vec3) -> Sets the motion of the particles.
             * .speed(vec3) -> Sets the speed of the particles. Affects the motion if given.
             * .area(vec3) -> Sets the area of the particles.
             * .delta(vec3) -> Sets the delta of the particles. Same as the particle command.
             * .withinBlockSpace() -> Sets the particles to be within the block space.
             *
             * .transform((tick, position, motion) => { ... })  -> return [newPosition, newVector]
             * .transformPosition((tick, position) => { ... })  -> return newPosition
             * .transformMotion((tick, motion) => { ... })      -> return newMotion
             */
            scene.addKeyframe();
            scene.particles.simple(TICK_LENGTH, "glow", pos);
            scene.particles.simple(TICK_LENGTH, "glow", start).density(10).area(end);
            scene.idle(IDLE_TICK_LENGTH);

            scene.addKeyframe();
            scene.particles.simple(TICK_LENGTH, "small_flame", pos);
            scene.particles.simple(TICK_LENGTH, "small_flame", start).density(10).motion([0, 0, -0.1]).area(end);
            scene.idle(IDLE_TICK_LENGTH);

            scene.addKeyframe();
            scene.particles.item(TICK_LENGTH, "minecraft:diamond_block", pos).motion([-0.09, 0.3, 0]).density(8);
            scene.particles.item(TICK_LENGTH, "minecraft:diamond_block", start).area(end);
            scene.idle(IDLE_TICK_LENGTH);

            scene.addKeyframe();
            scene.particles.block(TICK_LENGTH, "minecraft:diamond_block", pos);
            scene.particles.block(TICK_LENGTH, "minecraft:diamond_block", start).density(4).area(end);
            scene.idle(IDLE_TICK_LENGTH);

            scene.addKeyframe();
            scene.particles.dust(TICK_LENGTH, "#00FFF0", start).density(5).motion([0, 0, -0.1]).area(end).roll(10);
            scene.idle(IDLE_TICK_LENGTH);

            scene.addKeyframe();
            scene.particles
                .dust(TICK_LENGTH, "#FF0000", "#0000FF", start)
                .density(2)
                .scale(2.1)
                .motion([0, 0, -0.1])
                .area(end)
                .roll(3);
            scene.idle(IDLE_TICK_LENGTH);

            scene.addKeyframe();
            scene.particles.simple(TICK_LENGTH, "portal", start).density(6).withinBlockSpace();
            scene.idle(IDLE_TICK_LENGTH);
        })
        .scene("custom_transform", "Use some custom transformations", (scene, util) => {
            scene.showStructure();
            scene.idle(10);

            scene.particles.simple(TICK_LENGTH * 3, "glow", [1, 1.5, 0]).transformPosition((tick, p) => {
                return [p.x(), p.y(), p.z() + (tick / TICK_LENGTH) * 1.6];
            });

            scene.particles.simple(TICK_LENGTH * 3, "sneeze", [2.5, 1.5, 0]).transformMotion((tick, m) => {
                return [0, 0, (tick / TICK_LENGTH) * 0.2];
            });

            /**
             * You also can directly transform all in once.
             * Must return [ position_vector, motion_vector ] or [ [x, y, z], [mx, my, mz] ]
             */
            scene.particles.simple(TICK_LENGTH * 3, "small_flame", [4, 1.5, 0]).transform((tick, p, m) => {
                return [
                    [p.x(), p.y(), Math.random() * 5],
                    [(tick / TICK_LENGTH) * 0.2, 0, (tick / TICK_LENGTH) * 0.2],
                ];
            });
        })
        .scene("create_particles", "Create's own particles", (scene, util) => {
            scene.showStructure();
            scene.idle(10);

            scene.addKeyframe();
            const delta = [0.3, 0.3, 0.3];
            const fluidPos = new Vec3(0.5, 1.5, 0.5);
            scene.particles
                .fluid(TICK_LENGTH, "lava", fluidPos.add([0, 0, 0]))
                .delta(delta)
                .density(5);

            scene.particles
                .drip(TICK_LENGTH, "lava", fluidPos.add([2, 0, 0]))
                .delta(delta)
                .density(5);

            scene.particles
                .basin(TICK_LENGTH, "lava", fluidPos.add([4, 0, 0]))
                .delta(delta)
                .density(5);

            scene.particles
                .fluid(TICK_LENGTH, "create:honey", fluidPos.add([0, 0, 2]))
                .delta(delta)
                .density(5);

            scene.particles
                .drip(TICK_LENGTH, "create:honey", fluidPos.add([2, 0, 2]))
                .delta(delta)
                .density(5);

            scene.particles
                .basin(TICK_LENGTH, "create:honey", fluidPos.add([4, 0, 2]))
                .delta(delta)
                .density(5);

            const potionFluid = Fluid.of("create:potion", { Potion: "minecraft:blindness" });
            scene.particles
                .fluid(TICK_LENGTH, potionFluid, fluidPos.add([0, 0, 4]))
                .delta(delta)
                .collision(true)
                .density(5);

            scene.particles
                .drip(TICK_LENGTH, potionFluid, fluidPos.add([2, 0, 4]))
                .delta(delta)
                .collision(true)
                .density(5);

            scene.particles
                .basin(TICK_LENGTH, potionFluid, fluidPos.add([4, 0, 4]))
                .delta(delta)
                .collision(true)
                .density(5);
            scene.idle(IDLE_TICK_LENGTH);

            scene.addKeyframe();
            /**
             * The 1 is the first radius, the 0.5 the second radius.
             * The last argument is the axis "X", "Y" or "Z".
             */
            scene.particles
                .rotationIndicator(TICK_LENGTH, [2.5, 1.5, 2.5], 1, 0.5, "Z")
                .rotationSpeed(4)
                .color("#FF0000");
            scene.idle(IDLE_TICK_LENGTH * 1.5);
        });
});
