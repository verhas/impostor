package com.javax0.impostor;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class On {

    private final Object object;

    private On(Object object) {
        this.object = object;
    }

    public static On instance(Object object) {
        return new On(object);
    }

    public Object call(String method, Object... arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return object.getClass().getMethod(method, Arrays.stream(arguments).map(Object::getClass).toArray(Class[]::new)).invoke(object,arguments);
    }

}
