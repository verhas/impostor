package com.javax0.impostor;

public class SecondOblivious {

    public void execute() {
        new Victim().run();
        final var sc = new SecondVictim();
        System.out.println(sc.sound("dog"));
        System.out.println(sc.sound("horse"));
        System.out.println(sc.sound("cat"));
        System.out.println(sc.sound("duck"));
    }
}
