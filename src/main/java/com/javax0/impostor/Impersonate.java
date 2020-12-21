package com.javax0.impostor;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * When a class loaded by the {@link ImpostorClassLoader} has one or more {@code @Impersonate} annotations they can define
 * more impostor classes.
 * <p>
 * <br> Single class specified <br>-----------------------
 * <p>
 * When {@code @Impersonate("...")} specifies a single class name, then it tells the class loader to load the original class
 * that the actual class is impersonating instead of the named class.
 * <p>
 * For example {@code Impostor} impersonates {@code Victim} and the class {@code Impostor} is annotated as
 *
 * <pre>{@code
 * {@literal @}Impersonate("com.javax0.impostor.Impostor$Stub")
 * public class Impostor {
 * ...
 * }
 * }</pre>
 * <p>
 * In this case the class loader will load the class {@code Victim} to impersonate {@code Impostor$Stub}. This way the
 * class {@code Impostor} can reference the class it impersonates. If it was simply using the class {@code Victim} it
 * would get a reference to itself.
 * <p>
 * <br> Impostor chain <br>---------------
 * <p>
 * When {@code @Impersonate("...")} specifies several classes in the format:
 *
 * <pre>{@code
 * A -> B -> C -> D -> ... -> X -> Y
 * }</pre>
 * <p>
 * then class {@code A} will be impersonated by class {@code B}, class {@code B} will be impersonated by class {@code
 * C}, class {@code C} will be impersonated by class {@code D}, and so on.
 * <p>
 * <br> NOTE: about names <br>--------------------
 * <p>
 * The names are the names of the class. Not simple names and not canonical names. The name should contain the full
 * package the class is in. In case of an inner class the character {@code $} has to be used to separate the class names
 * after the outer most top level class down to the specified inner class. As an example, see the above
 *
 * <pre>
 * {@literal @}Impersonate("com.javax0.impostor.Impostor$Stub")
 * </pre>
 * <p>
 * example.
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Impersonates.class)
public @interface Impersonate {
    String value();
}
