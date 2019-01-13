package io.github.daniloarcidiacono.typescriptmapper.core.resolver;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

import java.net.URI;

@FunctionalInterface
public interface SourceMapper {
    URI map(final Class<?> javaClass, final TypescriptDeclaration declaration);
}

