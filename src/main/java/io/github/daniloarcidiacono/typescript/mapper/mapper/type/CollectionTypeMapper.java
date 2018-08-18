package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapper;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptArrayType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptMapType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class CollectionTypeMapper implements TypeMapper {
    private TypescriptMapper typescriptMapper;

    public CollectionTypeMapper(final TypescriptMapper typescriptMapper) {
        this.typescriptMapper = typescriptMapper;
    }

    @Override
    public TypescriptType map(final Type type) {
        final ParameterizedType parameterizedType = (ParameterizedType) type;
        final Class<?> rawTypeClass = (Class<?>) parameterizedType.getRawType();

        // Handle collections
        if (Collection.class.isAssignableFrom(rawTypeClass)) {
            return new TypescriptArrayType(typescriptMapper.mapType(parameterizedType.getActualTypeArguments()[0]));
        } else if (Map.class.isAssignableFrom(rawTypeClass)) {
            // Handle maps
            final TypescriptType keyType = typescriptMapper.mapType(parameterizedType.getActualTypeArguments()[0]);
            final TypescriptType valueType = typescriptMapper.mapType(parameterizedType.getActualTypeArguments()[1]);
            return new TypescriptMapType(keyType, valueType);
        }

        throw new IllegalStateException("CollectionTypeMapper invoked with unrecognized dtype " + type);
    }

    @Override
    public boolean supports(final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            final Class<?> rawTypeClass = (Class<?>) parameterizedType.getRawType();

            // Handle collections
            return Collection.class.isAssignableFrom(rawTypeClass) || Map.class.isAssignableFrom(rawTypeClass);
        }

        return false;
    }
}
