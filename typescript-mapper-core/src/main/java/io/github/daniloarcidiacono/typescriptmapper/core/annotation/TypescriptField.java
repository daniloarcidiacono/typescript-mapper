package io.github.daniloarcidiacono.typescriptmapper.core.annotation;

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
     * @return the type
     */
    Class<?> type() default void.class;

    /**
     * Whether the field is required or not.
     * @return field required
     */
    boolean required() default true;

    /**
     * Whether to serialize the field or not.
     * @return field ignore
     */
    boolean ignore() default false;
}
