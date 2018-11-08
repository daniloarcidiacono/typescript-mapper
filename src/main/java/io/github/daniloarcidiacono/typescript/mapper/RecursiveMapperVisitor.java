package io.github.daniloarcidiacono.typescript.mapper;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

@FunctionalInterface
public interface RecursiveMapperVisitor {
    void visit(final Class<?> javaClass, final TypescriptDeclaration declaration);
}
