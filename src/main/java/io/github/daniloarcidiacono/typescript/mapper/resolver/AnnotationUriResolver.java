package io.github.daniloarcidiacono.typescript.mapper.resolver;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapped;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Resolvers uri by checking {@link TypescriptDTO} annotation.
 * @see TypescriptDTO
 * @author Danilo Arcidiacono
 */
public class AnnotationUriResolver implements UriResolver {
    public AnnotationUriResolver() {
    }

    @Override
    public URI resolve(final TypescriptMapped mapped) {
        final TypescriptDTO annotation = mapped.getJavaClass().getAnnotation(TypescriptDTO.class);
        try {
            return new URI(annotation.path());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Could not create uri from " + annotation.path() + " (check your source code)");
        }
    }

    @Override
    public boolean canResolve(final TypescriptMapped mapped) {
        return mapped != null &&
               mapped.getJavaClass() != null &&
               mapped.getJavaClass().isAnnotationPresent(TypescriptDTO.class) &&
               !mapped.getJavaClass().getAnnotation(TypescriptDTO.class).path().isEmpty();
    }
}