package com.kotakotik.pondermaker.crafttweaker.wrappers.interfaces;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.ZenWrapper;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.elements.InputWindowElement;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.openzen.zencode.java.ZenCodeType;

import java.lang.reflect.Field;
import java.util.HashMap;

@ZenRegister
@ZenCodeType.Name("pondermaker.IInputWindowElement")
@ZenWrapper(wrappedClass = "com.simibubi.create.foundation.ponder.elements.InputWindowElement", creationMethodFormat = "new InputWindowElementWrapper(%s)", implementingClass = "com.kotakotik.pondermaker.crafttweaker.wrappers.implementing.InputWindowElementWrapper")
public interface IInputWindowElement {
    InputWindowElement getInternal();

    @ZenCodeType.Method
    default InputWindowElement withItem(ItemStack stack) {
        return getInternal().withItem(stack);
    }

    @ZenCodeType.Method
    default InputWindowElement withWrench() {
        return getInternal().withWrench();
    }

    @ZenCodeType.Method
    default InputWindowElement scroll() {
        return getInternal().scroll();
    }

    @ZenCodeType.Method
    default InputWindowElement rightClick() {
        return getInternal().rightClick();
    }

    @ZenCodeType.Method
    default InputWindowElement showing(AllIcons icon) {
        return getInternal().showing(icon);
    }

    HashMap<String, AllIcons> cachedIcons = new HashMap<>();

    @ZenCodeType.Method
    default InputWindowElement showing(String icon) {
        String str = icon.toUpperCase();
        if(!str.startsWith("I_")) {
            str = "I_" + str;
        }
        if(cachedIcons.containsKey(str)) {
            AllIcons cached = cachedIcons.get(str);
            if(cached != null) {
                return showing(cached);
            }
            return getInternal();
        }
        Field f = ObfuscationReflectionHelper.findField(AllIcons.class, str);
        try {
            cachedIcons.put(str, (AllIcons) f.get(null));
            return showing(cachedIcons.get(str));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return getInternal();
    }

    @ZenCodeType.Method
    default InputWindowElement leftClick() {
        return getInternal().leftClick();
    }

    @ZenCodeType.Method
    default InputWindowElement whileSneaking() {
        return getInternal().whileSneaking();
    }

    @ZenCodeType.Method
    default InputWindowElement whileCTRL() {
        return getInternal().whileCTRL();
    }

    @ZenCodeType.Method
    default void setFade(float fade) {
        getInternal().setFade(fade);
    }

    @ZenCodeType.Method
    default void whileSkipping(PonderScene scene) {
        getInternal().whileSkipping(scene);
    }

    @ZenCodeType.Method
    default void reset(PonderScene scene) {
        getInternal().reset(scene);
    }

    @ZenCodeType.Method
    default boolean isVisible() {
        return getInternal().isVisible();
    }

    @ZenCodeType.Method
    default void setVisible(boolean visible) {
        getInternal().setVisible(visible);
    }
}
