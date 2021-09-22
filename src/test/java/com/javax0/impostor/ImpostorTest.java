package com.javax0.impostor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static com.javax0.impostor.ImpostorEntryBuilder.impersonate;

public class ImpostorTest {

    @Test
    @DisplayName("Impostor impersonates victim in front of oblivious, configured calling .map()")
    void demoTest0() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        try (final var output = new Output()) {
            final var oblivious =
                new ImpostorClassLoader()
                    .map(
                        impersonate(Victim.class).using(Impostor.class)
                    )
                    .dumpDirectory("./dump/")
                    .load(Oblivious.class);
            From.klass(oblivious).on.newInstance().call("execute");
            Assertions.assertEquals("Impostor start\n" +
                "Victim run\n" +
                "Impostor end\n", output.toString());
        }
    }

    @Test
    @DisplayName("Impostor impersonates victim in front of oblivious, configured calling .mapper()")
    void demoTest0a() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Assumptions.assumeTrue("com.javax0.impostor.Victim".equals(Victim.class.getName()));
        Assumptions.assumeTrue("com.javax0.impostor.Impostor".equals(Impostor.class.getName()));
        try (final var output = new Output()) {
            final var oblivious =
                new ImpostorClassLoader()
                    .mapper(className -> switch (className) {
                            case "com.javax0.impostor.Victim" -> Optional.of("com.javax0.impostor.Impostor");
                            default -> Optional.empty();
                        }
                    )
                    .dumpDirectory("./dump/")
                    .load(Oblivious.class);
            From.klass(oblivious).on.newInstance().call("execute");
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
                .load(Oblivious.class);
            From.klass(oblivious).on.newInstance().call("execute");
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
                .load(SecondOblivious.class);
            From.klass(oblivious).on.newInstance().call("execute");
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
