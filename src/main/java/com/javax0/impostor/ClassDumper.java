package com.javax0.impostor;

import org.objectweb.asm.util.Textifier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

class ClassDumper {
    private final String dumpDir;

    ClassDumper(String dumpDir) {
        this.dumpDir = dumpDir;
    }

    void dump(String name, byte[] classFile) {
        try {
            var dumpFile = new File(dumpDir + name.replace(".", "/") + ".class");
            dumpFile.getParentFile().mkdirs();
            new ByteArrayInputStream(classFile).transferTo(new FileOutputStream(dumpFile));

            final var baos = new ByteArrayOutputStream();
            var old = System.out;
            String s = "";
            System.setOut(new PrintStream(baos));
            Textifier.main(new String[]{dumpFile.getAbsolutePath()});
            System.setOut(old);
            var textifile = new File(dumpDir + name.replace(".", "/") + ".asm");
            textifile.getParentFile().mkdirs();
            new ByteArrayInputStream(baos.toByteArray()).transferTo(new FileOutputStream(textifile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
