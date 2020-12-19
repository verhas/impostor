package com.javax0.impostor;

@Impersonate("com.javax0.impostor.Impostor$Stub")
public class Impostor {

    private static class Stub {
        private Stub() {
            System.out.println("Stub constructor, should never run");
        }

        public void run() {
            System.out.println("Stub never RUN this");
        }
    }

    public void run() {
        System.out.println("Impostor start");
        Stub victim = new Stub();
        victim.run();
        System.out.println("Impostor end");
    }
}
