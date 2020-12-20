package com.javax0.impostor;

@Impersonate("com.javax0.impostor.MultiImpostor$SecondVictimStub -> com.javax0.impostor.SecondVictim")
@Impersonate("com.javax0.impostor.MultiImpostor$VictimStub -> com.javax0.impostor.Victim")
public class MultiImpostor {

    static class SecondVictimStub extends SecondVictim {
    }

    static class VictimStub extends Victim {
    }

    private final SecondVictimStub secondVictim = new SecondVictimStub();

    public String sound(String animal) {
        return new StringBuffer(secondVictim.sound(animal)).reverse().toString();
    }

    public void run() {
        VictimStub victim = new VictimStub();
        System.out.println("MultiImpostor run");
        victim.run();
        System.out.println("MultiImpostor ran");
    }
}
