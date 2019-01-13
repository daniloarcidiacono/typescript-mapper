package io.github.daniloarcidiacono.typescriptmapper.core;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

@FunctionalInterface
public interface RecursiveMapperVisitor {
    void visit(final Class<?> javaClass, final TypescriptDeclaration declaration);
}
