package com.javax0.impostor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Output implements AutoCloseable {

    private final PrintStream old;
    private final PrintStream out;
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public Output() {
        this.old = System.out;
        String s = "";
        out = new PrintStream(baos);
        System.setOut(out);
    }

    @Override
    public String toString() {
        return baos.toString(StandardCharsets.UTF_8);
    }

    @Override
    public void close() {
        out.close();
        System.setOut(old);
    }
}