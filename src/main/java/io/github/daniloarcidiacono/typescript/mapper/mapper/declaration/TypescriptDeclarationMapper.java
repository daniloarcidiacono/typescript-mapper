package io.github.daniloarcidiacono.typescript.mapper.mapper.declaration;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

import java.lang.reflect.Type;

public interface TypescriptDeclarationMapper {
    TypescriptDeclaration map(final Type type);
    boolean supports(final Type type);
}
