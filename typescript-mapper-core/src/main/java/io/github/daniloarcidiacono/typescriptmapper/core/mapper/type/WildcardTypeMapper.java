package io.github.daniloarcidiacono.typescriptmapper.core.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptAnyType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public class WildcardTypeMapper implements TypeMapper {
    private TypeMapper typeMapper;

    public WildcardTypeMapper(final TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    @Override
    public TypescriptType map(final Type type) {
        if (!supports(type)) {
            return null;
        }

        final WildcardType wildcardType = (WildcardType)type;
        if (wildcardType.getLowerBounds().length == 0) {
            if (wildcardType.getUpperBounds().length == 0 || Object.class == wildcardType.getUpperBounds()[0]) {
                // "?" wildcard
                return TypescriptAnyType.INSTANCE;
            } else {
                // "? extends" wildcard
                final TypescriptType upperType = typeMapper.map(wildcardType.getUpperBounds()[0]);

                // @TODO: Which policy to apply?
                return upperType != null ? upperType : null;
            }
        }

        // "? super" wildcard
        // No equivalent in TypeScript
        return TypescriptAnyType.INSTANCE;
    }

    private boolean supports(final Type type) {
        return type instanceof WildcardType;
    }
}
