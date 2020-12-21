package com.javax0.impostor;

import java.util.AbstractMap;
import java.util.Map;

/**
 * Build a {@link Map.Entry Entry&lt;String,String&gt;} to be added to the impostor mapping.
 * <p>
 * The typical use of this builder is to {@code static import} the method {@link #impersonate(String) impersonate()} and
 * then write
 *
 * <pre>{@code
 * impersonate(Victim.class).using(Impostor.class)
 * }</pre>
 * <p>
 * which is one entry into the entry map you specify in the method {@link ImpostorClassLoader#map(Map.Entry[])
 * ImpostorClassLoader.map()}.
 * <p>
 * There are versions for both {@link #impersonate(String) impersonate()} and {@link #using(String) using()} methods
 * that accept {@code String} argument in case the classes cannot be used.
 */
public class ImpostorEntryBuilder {

    private final String fromClass;

    private ImpostorEntryBuilder(String fromClass) {
        this.fromClass = fromClass;
    }

    public static ImpostorEntryBuilder impersonate(final String fromClass) {
        return new ImpostorEntryBuilder(fromClass);
    }

    public static ImpostorEntryBuilder impersonate(final Class<?> fromClass) {
        return impersonate(fromClass.getName());
    }

    public Map.Entry<String, String> using(final String toClass) {
        return new AbstractMap.SimpleImmutableEntry<>(fromClass, toClass);
    }

    public Map.Entry<String, String> using(final Class<?> toClass) {
        return using(toClass.getName());
    }
}
