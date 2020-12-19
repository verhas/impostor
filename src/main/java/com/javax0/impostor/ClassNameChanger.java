package com.javax0.impostor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * Change the name of a class before loading it.
 * <p>
 * When we load an impostor class, it eventually has its name. The impostor class loader, however, wants to load it
 * instead of a different class. The impostor class must hide its identity and should lie about its real name. The byte
 * array of the class contains the name of the class. The method {@link #rename(String, byte[])} will rename the class
 * from the original name to the name given. The returned byte array will contain the same class but with the new name.
 */
public class ClassNameChanger {
    private ClassNameChanger() {
    }

    private static class ClassNameChangerAdapter extends ClassVisitor {
        final String name;

        private ClassNameChangerAdapter(ClassVisitor classVisitor, final String name) {
            super(Opcodes.ASM9, classVisitor);
            this.name = name.replace('.', '/');
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            cv.visit(version, access, this.name, signature, superName, interfaces);
        }
    }

    /**
     * Convert the byte array so that the returned byte array will contains the new name.
     *
     * @param name      the new name we want the class to have
     * @param classFile the original byte array containing the class
     * @return the modified class that is the same as the old one, but with the new name
     */
    public static byte[] rename(final String name, final byte[] classFile) {
        ClassReader cr = new ClassReader(classFile);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = new ClassNameChangerAdapter(cw, name);
        cr.accept(cv, 0);
        return cw.toByteArray();
    }

}
