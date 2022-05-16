package com.almostreliable.ponderjs.mixin;

import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.ScriptableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// TODO Remove this when updating to rhino build > 157
@Mixin(NativeJavaObject.class)
public interface NativeJavaObjectInvoker {
    @Invoker(value = "createInterfaceAdapter", remap = false)
    public static Object ponderjs$createInterfaceAdapter(Class<?> type, ScriptableObject so) {
        throw new AssertionError();
    }
}
