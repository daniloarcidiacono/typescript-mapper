package io.github.daniloarcidiacono.typescript.mapper.identifier;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

/**
 * Id generator that reads the {@link TypescriptDTO#identifier()} annotation value.
 * @see TypescriptDTO
 * @author Danilo Arcidiacono
 */
public class AnnotationIdentifierGenerator implements IdentifierGenerator {
    public AnnotationIdentifierGenerator() {
    }

    @Override
    public void clear() {
    }

    @Override
    public String generate(final Object object) {
        final Class<?> clazz = (Class<?>)object;
        final TypescriptDTO annotation = clazz.getAnnotation(TypescriptDTO.class);
        return annotation.identifier();
    }

    @Override
    public boolean supports(final Object object) {
        return object instanceof Class<?> &&
               ((Class<?>)object).isAnnotationPresent(TypescriptDTO.class) &&
               !((Class<?>)object).getAnnotation(TypescriptDTO.class).identifier().isEmpty();
    }
}
