package com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("pondermaker.IUnit")
@ZenWrapper(wrappedClass = "net.minecraft.util.Unit", creationMethodFormat = "new UnitWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.UnitWrapper")
public interface IUnit {
}
