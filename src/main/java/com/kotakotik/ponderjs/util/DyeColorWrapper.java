package com.kotakotik.ponderjs.util;

import net.minecraft.world.item.DyeColor;

public class DyeColorWrapper {
    public final DyeColor mcColor;

    public DyeColorWrapper(DyeColor color) {
        this.mcColor = color;
    }

    public int getId() {
        return mcColor.getId();
    }

    public String getName() {
        return mcColor.getName();
    }

    public String getSerializedName() {
        return mcColor.getSerializedName();
    }

    public int getColorValue() {
        return mcColor.getTextColor();
    }

    public static DyeColorWrapper get(String name) {
        return new DyeColorWrapper(DyeColor.byName(name, null));
    }

    public static DyeColorWrapper byId(int id) {
        return new DyeColorWrapper(DyeColor.byId(id));
    }
}