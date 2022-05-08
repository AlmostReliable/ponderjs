package com.kotakotik.ponderjs;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Function;
import dev.latvian.mods.rhino.RhinoException;

public class FunctionJS<R> {

    private final Function function;
    private final Class<R> resultClass;
    private final R defaultValue;
    private final Context context;

    public FunctionJS(Function function, Class<R> resultClass, R defaultValue, Context context) {
        this.function = function;
        this.resultClass = resultClass;
        this.defaultValue = defaultValue;
        this.context = context;
    }

    public R call(Object... args) {
        try {
            Object result = function.call(Context.getCurrentContext(), function.getParentScope(), function, args);
            //noinspection unchecked
            return (R) Context.jsToJava(result, resultClass);
        } catch (RhinoException | ClassCastException e) {
            PonderErrorHelper.yeet(e);
        }
        return defaultValue;
    }
}
