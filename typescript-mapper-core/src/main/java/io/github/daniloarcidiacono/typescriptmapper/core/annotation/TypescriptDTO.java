package io.github.daniloarcidiacono.typescriptmapper.core.annotation;

import java.lang.annotation.*;
import java.lang.reflect.Modifier;

/**
 * Marker interface that can be used for searching classes that need to be converted in TypeScript code.
 * @author Danilo Arcidiacono
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface TypescriptDTO {
    /**
     * Identifier to use for the class
     * @return the identifier
     */
    String identifier() default "";

    /**
     * The relative path to write the class.
     * @return the path
     */
    String path() default "";

    /**
     * Fields having at least one of the specified modifiers are ignored
     * @return a bitmask composed of {@link Modifier} values
     */
    int[] ignoreFieldModifiers() default { Modifier.STATIC };
}
