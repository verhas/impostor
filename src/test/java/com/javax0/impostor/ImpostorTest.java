package com.javax0.impostor;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static com.javax0.impostor.ImpostorEntryBuilder.impersonate;

public class ImpostorTest {

    @Test
    void demoTest() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final var oblivious = new ImpostorClassLoader()
            .map(
                impersonate(Victim.class).using(Impostor.class)
            ).load(Oblivious.class).getConstructor().newInstance();
        oblivious.getClass().getDeclaredMethod("execute").invoke(oblivious);
    }

    @Test
    void demoTest1() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final var oblivious = new ImpostorClassLoader()
            .impersonate(Victim.class).using(Impostor.class)
            .load(Oblivious.class).getConstructor().newInstance();
        oblivious.getClass().getDeclaredMethod("execute").invoke(oblivious);
    }
}
