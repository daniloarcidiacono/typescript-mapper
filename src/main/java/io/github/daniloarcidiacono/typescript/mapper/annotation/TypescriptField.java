package io.github.daniloarcidiacono.typescript.mapper.annotation;

import java.lang.annotation.*;

/**
 * Marker interface that can be used for customizing field options.
 * @author Danilo Arcidiacono
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface TypescriptField {
    /**
     * The type of the field.
     */
    Class<?> type() default void.class;

    /**
     * Whether the field is required or not.
     */
    boolean required() default true;

    /**
     * Whether to serialize the field or not.
     */
    boolean ignore() default false;
}
