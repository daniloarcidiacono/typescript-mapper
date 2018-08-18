package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapper;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptInterfaceType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParametrizedTypeMapper implements TypeMapper {
    private TypescriptMapper typescriptMapper;

    public ParametrizedTypeMapper(final TypescriptMapper typescriptMapper) {
        this.typescriptMapper = typescriptMapper;
    }

    @Override
    public TypescriptType map(final Type type) {
        final ParameterizedType parameterizedType = (ParameterizedType) type;
        final Class<?> rawTypeClass = (Class<?>) parameterizedType.getRawType();

        // Parse the raw type
        final TypescriptType rawType = typescriptMapper.mapType(rawTypeClass);

        // Add the type parameters
        for (Type type1 : parameterizedType.getActualTypeArguments()) {
            ((TypescriptInterfaceType)rawType).argument(typescriptMapper.mapType(type1));
        }

        return rawType;
    }

    @Override
    public boolean supports(final Type type) {
        return type instanceof ParameterizedType;
    }
}
