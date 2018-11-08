package io.github.daniloarcidiacono.typescript.mapper.annotation;

import java.lang.annotation.*;

/**
 * Annotation used to define the documentation of the generated typescript code.
 * Can be applied to a method, class, field or parameter.
 * @author Danilo Arcidiacono
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Documented
public @interface TypescriptComments {
    /**
     * Text used when rendering the target element in TypeScript.
     * (each array element is rendered as a comment line)
     *
     * @return the comments
     */
    String[] value() default {};
}
