package com.javax0.impostor;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class From {
    final Class<?> klass;
    public class FromOn {
        public On newInstance(Object... arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
            return On.instance(From.this.newInstance(arguments));
        }
    }
    final FromOn on = new FromOn();

    private From(Class<?> klass) {
        this.klass = klass;
    }

    public static From klass(Class<?> k) {
        return new From(k);
    }

    public Object newInstance(Object... arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        return klass.getConstructor(Arrays.stream(arguments).map(Object::getClass).toArray(Class[]::new)).newInstance(arguments);
    }
}
