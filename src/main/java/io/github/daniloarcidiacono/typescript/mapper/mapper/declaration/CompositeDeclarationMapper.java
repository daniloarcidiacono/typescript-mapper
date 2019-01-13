package io.github.daniloarcidiacono.typescript.mapper.mapper.declaration;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;
import io.github.daniloarcidiacono.commons.lang.patterns.Composite;

import java.lang.reflect.Type;

public class CompositeDeclarationMapper extends Composite<TypescriptDeclarationMapper> implements TypescriptDeclarationMapper {
    @Override
    public TypescriptDeclaration map(final Type type) {
        for (TypescriptDeclarationMapper mapper : components) {
            if (mapper.supports(type)) {
                return mapper.map(type);
            }
        }

        throw new IllegalStateException("No type mappers found for " + type);
    }

    @Override
    public boolean supports(final Type type) {
        for (TypescriptDeclarationMapper mapper : components) {
            if (mapper.supports(type)) {
                return true;
            }
        }

        return false;
    }
}
