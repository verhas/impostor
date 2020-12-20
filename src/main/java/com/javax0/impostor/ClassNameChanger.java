package com.javax0.impostor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

/**
 * Change the name of a class before loading it.
 * <p>
 * When we load an impostor class, it eventually has its name. The impostor class loader, however, wants to load it
 * instead of a different class. The impostor class must hide its identity and should lie about its real name. The byte
 * array of the class contains the name of the class. The method {@link #rename(String, String, byte[])} will rename the
 * class from the original name to the name given. The returned byte array will contain the same class but with the new
 * name.
 */
public class ClassNameChanger {
    private ClassNameChanger() {
    }

    /**
     * Convert the byte array so that the returned byte array will contains the new name.
     *
     * @param from      the old name of the class
     * @param to        the new name we want the class to have
     * @param classFile the original byte array containing the class
     * @return the modified class that is the same as the old one, but with the new name
     */
    public static byte[] rename(final String from, final String to, final byte[] classFile) {
        ClassReader cr = new ClassReader(classFile);
        ClassWriter cw = new ClassWriter(cr, 0);
        final String oldName = from.replace('.', '/');
        Remapper remapper = new Remapper() {
            @Override
            public String map(String internalName) {
                if (internalName.equals(oldName))
                    return to.replace('.', '/');
                else
                    return internalName;
            }
        };
        ClassVisitor cvR = new ClassRemapper(cw, remapper);
        cr.accept(cvR, 0);
        return cw.toByteArray();
    }

}
