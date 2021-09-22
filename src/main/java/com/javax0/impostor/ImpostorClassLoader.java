package com.javax0.impostor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A class loader that can be configured to load impostor classes.
 */
public class ImpostorClassLoader extends ClassLoader {
    private final Map<String, Class<?>> loaded = new HashMap<>();
    private final Map<String, String> impostorMap = new HashMap<>();
    private Function<String, Optional<String>> impostorMapper = s -> Optional.empty();

    private synchronized void add(final String from, final String to) {
        impostorMap.put(from, to);
    }

    @SafeVarargs
    public final ImpostorClassLoader map(final Map.Entry<String, String>... entires) {
        final var impostorMap = new HashMap<String,String>();
        for (final var entry : entires) {
            if (impostorMap.put(entry.getKey(), entry.getValue()) != null) {
                throw new IllegalArgumentException("The victim class '" +
                    entry.getKey() +
                    "' is impersonated by more than one impostors.");
            }
        }
        impostorMap.forEach(this::add);
        return this;
    }

    private static ClassDumper dumper = null;

    public ImpostorClassLoader dumpDirectory(final String dumpDir) {
        this.dumper = new ClassDumper(dumpDir);
        return this;
    }

    public synchronized ImpostorClassLoader mapper(final Function<String, Optional<String>> impostorMapper) {
        this.impostorMapper = impostorMapper;
        return this;
    }

    /**
     * Create a new ImpostorClassLoader instance specifying the parent class loader. The typical use is usually
     *
     * <pre>{@code
     * ImpostorClassLoader loader = new ImpostorClassLoader(this.getClass().getClassLoader());
     * }</pre>
     *
     * @param parent passed to the super constructor. For more information see {@link ClassLoader#ClassLoader(ClassLoader)}.
     */
    public ImpostorClassLoader(ClassLoader parent) {
        super(ImpostorClassLoader.class.getName(), parent);
    }

    public ImpostorClassLoader() {
        super(ImpostorClassLoader.class.getName(), ImpostorClassLoader.class.getClassLoader());
    }

    public Class<?> load(final Class<?> klass) throws ClassNotFoundException {
        return loadClass(klass.getName());
    }

    @Override
    public synchronized Class<?> loadClass(final String name) throws ClassNotFoundException {
        Class<?> klass;
        if ((klass = loaded.get(name)) != null) {
            return klass;
        }
        if (name.startsWith("java.") || name.equals(Impersonate.class.getName()) || name.equals(Impersonates.class.getName())) {
            klass = super.loadClass(name);
        } else {
            String loadName = impostorMapper.apply(name).orElse(impostorMap.computeIfAbsent(name, s -> s));
            final byte[] impostorBytes = getClassByteContent(loadName);
            final byte[] classFile;
            if (Objects.equals(name, loadName)) {
                classFile = impostorBytes;
            } else {
                classFile = ClassNameChanger.rename(loadName, name, impostorBytes);
                dumper.dump(loadName + "_" + name.substring(name.lastIndexOf(".")+1), classFile);
                dumper.dump(loadName, impostorBytes);
            }
            klass = defineClass(name, classFile, 0, classFile.length);
            loaded.put(name, klass);
            fetchMappings(name, klass);
        }

        return klass;
    }



    private void fetchMappings(String name, Class<?> klass) {
        final var replaces = getImpersonates(klass);
        for (final var replace : replaces) {
            String[] fromTo = replace.split("\\s*->\\s*");
            if (fromTo.length < 2) {
                add(replace, name);
            } else {
                for (int i = 1; i < fromTo.length; i++) {
                    add(fromTo[i - 1], fromTo[i]);
                }
            }
        }
    }

    private List<String> getImpersonates(Class<?> klass) {
        List<String> replaces = new ArrayList<>();
        Impersonate[] annotations = klass.getAnnotationsByType(Impersonate.class);
        for (final var annotation : annotations) {
            replaces.add(annotation.value());
        }
        return replaces;
    }


    /**
     * Load the actual bytes from the {@code .class} file using the name of the class.
     *
     * @param className of the class. Note that this is not the canonical name. In other words, when this is an inner
     *                  class then it should contain {@code $} and not a {@code .} after the name of the outer
     *                  class(es).
     * @return the byte array containing the modified class.
     */
    private byte[] getClassByteContent(String className) throws ClassNotFoundException {
        final var resourceName = className.replace('.', '/') + ".class";
        try (final var stream = new ByteArrayOutputStream()) {
            final var is = super.getResourceAsStream(resourceName);
            if (is == null) {
                throw new ClassNotFoundException(className);
            }
            is.transferTo(stream);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new ClassNotFoundException(className);
        }
    }

    public class VictimHolder {
        final String victim;

        private VictimHolder(String victim) {
            this.victim = victim;
        }

        public ImpostorClassLoader using(String impostor) {
            add(victim, impostor);
            return ImpostorClassLoader.this;
        }

        public ImpostorClassLoader using(Class<?> impostor) {
            return using(impostor.getName());
        }
    }

    public VictimHolder impersonate(final String impostor) {
        return new VictimHolder(impostor);
    }

    public VictimHolder impersonate(final Class<?> impostor) {
        return impersonate(impostor.getName());
    }


}
