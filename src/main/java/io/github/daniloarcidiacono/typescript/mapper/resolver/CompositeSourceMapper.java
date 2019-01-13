package io.github.daniloarcidiacono.typescript.mapper.resolver;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;
import io.github.daniloarcidiacono.commons.lang.patterns.Composite;

import java.net.URI;

public class CompositeSourceMapper extends Composite<SourceMapper> implements SourceMapper {
    public CompositeSourceMapper() {
    }

    @Override
    public URI map(final Class<?> javaClass, final TypescriptDeclaration declaration) {
        for (SourceMapper mapper : components) {
            final URI mapAttempt = mapper.map(javaClass, declaration);
            if (mapAttempt != null) {
                return mapAttempt;
            }
        }

        return null;
    }
}