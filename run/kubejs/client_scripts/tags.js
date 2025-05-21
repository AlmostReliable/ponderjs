Ponder.tags((event) => {
    event.createTag("ponderjs:custom_tag", "minecraft:nether_star", "Custom Tag Title", "Custom Tag Description", [
        "minecraft:blaze_powder",
        "minecraft:shears",
    ]);

    event.createTag("ponderjs:with_index_no_add", tag => {
        tag.icon("minecraft:diamond");
        tag.title("Add to index, but dont add icon");
        tag.description("Diamond Description");
        tag.noIndex();
    });

    event.createTag("ponderjs:with_index_with_add", tag => {
        tag.icon("minecraft:emerald");
        tag.title("Add to index, but dont add icon");
        tag.description("Emerald Description");
        // tag.noIndex();
        tag.addIconToItems();
    });

    if (Platform.isLoaded("create")) {
        event.add("create:kinetic_appliances", "minecraft:dirt");
        event.remove("create:kinetic_appliances", ["create:deployer", "create:encased_fan"]);
    }
});
