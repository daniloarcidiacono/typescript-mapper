package io.github.daniloarcidiacono.typescript.mapper.resolver;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

import java.net.URI;

@FunctionalInterface
public interface SourceMapper {
    URI map(final Class<?> javaClass, final TypescriptDeclaration declaration);
}

