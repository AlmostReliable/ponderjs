Ponder.tags((event) => {
    event.createTag("ponderjs:custom_tag", "minecraft:nether_star", "Custom Tag Title", "Custom Tag Description", [
        "minecraft:blaze_powder",
        "minecraft:shears",
    ]);

    try {
        event.add("create:kinetic_appliances", "minecraft:dirt");
        event.remove("create:kinetic_appliances", ["create:deployer", "create:encased_fan"]);
    } catch (e) {
        console.log("Create not loaded, so we just skip")
    }
});
