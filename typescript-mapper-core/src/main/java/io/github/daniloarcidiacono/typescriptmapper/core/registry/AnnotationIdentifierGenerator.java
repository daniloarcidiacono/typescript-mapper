package io.github.daniloarcidiacono.typescriptmapper.core.registry;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

/**
 * Identifier generator for classes annotated with {@link TypescriptDTO#identifier()}.
 *
 * @see TypescriptDTO
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
