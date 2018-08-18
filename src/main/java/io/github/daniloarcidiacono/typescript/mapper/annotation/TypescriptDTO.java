package io.github.daniloarcidiacono.typescript.mapper.annotation;

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
     */
    String identifier() default "";

    /**
     * The relative path to write the class.
     */
    String path() default "";

    /**
     * Fields having at least one of the specified modifiers are ignored
     */
    int[] ignoreFieldModifiers() default { Modifier.STATIC };
}
