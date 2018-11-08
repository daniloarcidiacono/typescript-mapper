package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptAnyType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptInterfaceType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParametrizedTypeMapper implements TypeMapper {
    private TypeMapper typeMapper;

    public ParametrizedTypeMapper(final TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    @Override
    public TypescriptType map(final Type type) {
        if (!supports(type)) {
            return null;
        }

        final ParameterizedType parameterizedType = (ParameterizedType) type;
        final Class<?> rawTypeClass = (Class<?>) parameterizedType.getRawType();

        // Parse the raw type
        final TypescriptType rawType = typeMapper.map(rawTypeClass);
        if (rawType == null) {
            return null;
        }

        // If the raw type is a class, ClassTypeMapper uses the any type for generic arguments
        final TypescriptInterfaceType interfaceType = (TypescriptInterfaceType)rawType;

        // We specialize them here
        interfaceType.getGenericsArguments().getArguments().clear();
        for (Type typeArgument : parameterizedType.getActualTypeArguments()) {
            final TypescriptType mappedType = typeMapper.map(typeArgument);
            interfaceType.argument(mappedType != null ? mappedType : TypescriptAnyType.INSTANCE);
        }

        return rawType;
    }

    private boolean supports(final Type type) {
        return type instanceof ParameterizedType;
    }
}
