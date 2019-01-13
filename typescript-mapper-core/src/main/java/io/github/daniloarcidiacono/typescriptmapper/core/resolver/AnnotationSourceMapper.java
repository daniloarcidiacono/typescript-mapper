package io.github.daniloarcidiacono.typescriptmapper.core.resolver;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Maps uri by checking {@link TypescriptDTO} annotation.
 * @see TypescriptDTO
 */
public class AnnotationSourceMapper implements SourceMapper {
    public AnnotationSourceMapper() {
    }

    @Override
    public URI map(final Class<?> javaClass, final TypescriptDeclaration declaration) {
        if (!canMap(javaClass, declaration)) {
            return null;
        }

        final TypescriptDTO annotation = javaClass.getAnnotation(TypescriptDTO.class);
        try {
            return new URI(annotation.path());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Could not create uri from " + annotation.path() + " (check your finder code)");
        }
    }

    private boolean canMap(final Class<?> javaClass, final TypescriptDeclaration declaration) {
        return javaClass.isAnnotationPresent(TypescriptDTO.class) &&
               !javaClass.getAnnotation(TypescriptDTO.class).path().isEmpty();
    }
}