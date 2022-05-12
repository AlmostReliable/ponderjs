package com.almostreliable.ponderjs.util;

import com.almostreliable.ponderjs.PonderJS;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.ponder.Selection;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Context;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class Util {
    public static Selection selectionOf(@Nullable Object o) {
        if (o instanceof Selection s) return s;
        if (o instanceof BoundingBox box) {
            return Selection.of(box);
        }

        if (o instanceof BlockPos b) {
            return Selection.of(new BoundingBox(b));
        }

        if (o instanceof List<?> l) {
            if (l.size() == 2) {
                // TODO Change to direct typewrapper if kube adds them
                Vec3 from = (Vec3) Context.jsToJava(l.get(0), Vec3.class);
                Vec3 to = (Vec3) Context.jsToJava(l.get(1), Vec3.class);
                return Selection.of(new BoundingBox((int) from.x,
                        (int) from.y,
                        (int) from.z,
                        (int) to.x,
                        (int) to.y,
                        (int) to.z));
            }

            Integer[] values = l.stream().map(entry -> UtilsJS.parseInt(entry, 0)).toArray(Integer[]::new);
            if (values.length == 6) {
                return Selection.of(new BoundingBox(values[0], values[1], values[2], values[3], values[4], values[5]));
            }
            if (values.length == 3) {
                return Selection.of(new BoundingBox(values[0], values[1], values[2], values[0], values[1], values[2]));
            }
        }

        if (Context.jsToJava(o, Vec3.class) instanceof Vec3 v) {
            // TODO use type wrapper for ve3
            return Selection.of(new BoundingBox(new BlockPos(v.x, v.y, v.z)));
        }

        ConsoleJS.CLIENT.error("Invalid selection: " + o);
        return Selection.of(new BoundingBox(0, 0, 0, 0, 0, 0));
    }

    public static AllIcons allIconsOf(@Nullable Object o) {
        if (o instanceof AllIcons) return (AllIcons) o;
        if (o == null) {
            return AllIcons.I_ACTIVE;
        }
        return PonderJS.getIconByName(o.toString());
    }

    public static PonderTag ponderTagOf(@Nullable Object o) {
        Objects.requireNonNull(o);
        PonderTag ponderTag = PonderJS.getTagByName(o.toString()).orElse(null);
        if (ponderTag == null) {
            IllegalArgumentException e = new IllegalArgumentException("Invalid PonderTag: " + o);
            PonderErrorHelper.yeet(e);
            throw e;
        }
        return ponderTag;
    }
}