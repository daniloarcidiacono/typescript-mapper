package io.github.daniloarcidiacono.typescript.mapper.registry;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

/**
 * Identifier generator for classes annotated with {@link TypescriptDTO#identifier()}.
 *
 * @see TypescriptDTO
 * @author Danilo Arcidiacono
 */
public class AnnotationIdentifierGenerator implements IdentifierGenerator {
    public AnnotationIdentifierGenerator() {
    }

    @Override
    public void reset() {
    }

    @Override
    public String generateIdentifier(final Object object) {
        if (!supports(object)) {
            return null;
        }

        final Class<?> clazz = (Class<?>)object;
        final TypescriptDTO annotation = clazz.getAnnotation(TypescriptDTO.class);
        return annotation.identifier();
    }

    private boolean supports(final Object object) {
        return object instanceof Class<?> &&
               ((Class<?>)object).isAnnotationPresent(TypescriptDTO.class) &&
               !((Class<?>)object).getAnnotation(TypescriptDTO.class).identifier().isEmpty();
    }
}
