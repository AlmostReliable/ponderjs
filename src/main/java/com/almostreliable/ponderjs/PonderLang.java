package com.almostreliable.ponderjs;

import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class PonderLang {
    public static boolean GENERATING_LANG = false;
    public static final Set<ResourceLocation> TAGS = new HashSet<>();
    public static final Set<ResourceLocation> SCENES = new HashSet<>();

    public static void reset() {
        TAGS.clear();
        SCENES.clear();
    }
}
