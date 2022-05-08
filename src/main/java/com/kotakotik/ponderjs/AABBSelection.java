package com.kotakotik.ponderjs;

import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.utility.outliner.Outline;
import com.simibubi.create.foundation.utility.outliner.Outliner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class AABBSelection extends Selection {
    private final AABB aabb;

    public AABBSelection(AABB aabb) {
        this.aabb = aabb;
    }
    
    @Override
    public Selection add(Selection other) {
        return null;
    }

    @Override
    public Selection substract(Selection other) {
        return null;
    }

    @Override
    public Selection copy() {
        return null;
    }

    @Override
    public Vec3 getCenter() {
        return null;
    }

    @Override
    public void forEach(Consumer<BlockPos> callback) {

    }

    @Override
    public Outline.OutlineParams makeOutline(Outliner outliner, Object slot) {
        return null;
    }

    @Override
    public boolean test(BlockPos blockPos) {
        return false;
    }
}
