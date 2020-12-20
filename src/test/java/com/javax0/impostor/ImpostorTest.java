package com.javax0.impostor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static com.javax0.impostor.ImpostorEntryBuilder.impersonate;

public class ImpostorTest {

    @Test
    @DisplayName("Impostor impersonates victim in front of oblivious, configured calling .map()")
    void demoTest0() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        try (final var output = new Output()) {
            final var oblivious = new ImpostorClassLoader()
                .map(
                    impersonate(Victim.class).using(Impostor.class)
                )
                .dumpDirectory("./dump/")
                .load(Oblivious.class).getConstructor().newInstance();
            oblivious.getClass().getDeclaredMethod("execute").invoke(oblivious);
            Assertions.assertEquals("Impostor start\n" +
                "Victim run\n" +
                "Impostor end\n", output.toString());
        }
    }

    @Test
    @DisplayName("Impostor impersonates victim in front of oblivious, configured calling .impersonate().using() directly")
    void demoTest1() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        try (final var output = new Output()) {
            final var oblivious = new ImpostorClassLoader()
                .impersonate(Victim.class).using(Impostor.class)
                .load(Oblivious.class).getConstructor().newInstance();
            oblivious.getClass().getDeclaredMethod("execute").invoke(oblivious);
            Assertions.assertEquals("Impostor start\n" +
                "Victim run\n" +
                "Impostor end\n", output.toString());
        }
    }

    @Test
    @DisplayName("One impostor can impersonate more than one victims")
    void demoTest2() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        try (final var output = new Output()) {
            final var oblivious = new ImpostorClassLoader()
                .impersonate(Victim.class).using(MultiImpostor.class)
                .impersonate(SecondVictim.class).using(MultiImpostor.class)
                .load(SecondOblivious.class).getConstructor().newInstance();
            oblivious.getClass().getDeclaredMethod("execute").invoke(oblivious);
            Assertions.assertEquals("MultiImpostor run\n" +
                "Victim run\n" +
                "MultiImpostor ran\n" +
                "ffuw\n" +
                "thgien\n" +
                "faorb\n" +
                "kcauq\n", output.toString());
        }
    }
}
