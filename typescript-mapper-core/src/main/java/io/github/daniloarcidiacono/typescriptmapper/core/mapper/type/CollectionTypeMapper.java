package io.github.daniloarcidiacono.typescriptmapper.core.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptArrayType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptMapType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptStringType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public class CollectionTypeMapper implements TypeMapper {
    private TypeMapper typeMapper;

    public CollectionTypeMapper(final TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    @Override
    public TypescriptType map(final Type type) {
        if (!supports(type)) {
            return null;
        }

        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            final Class<?> rawTypeClass = (Class<?>) parameterizedType.getRawType();

            // Handle collections
            if (Collection.class.isAssignableFrom(rawTypeClass)) {
                final TypescriptType innerType = typeMapper.map(parameterizedType.getActualTypeArguments()[0]);

                // @TODO: Which policy to apply?
                return innerType != null ? new TypescriptArrayType(innerType) : null;
            } else if (Map.class.isAssignableFrom(rawTypeClass)) {
                // Handle maps
                final TypescriptType keyType = typeMapper.map(parameterizedType.getActualTypeArguments()[0]);
                final TypescriptType valueType = typeMapper.map(parameterizedType.getActualTypeArguments()[1]);

                // @TODO: Which policy to apply?
                return keyType != null && valueType != null ? new TypescriptMapType(keyType, valueType) : null;
            }
        } else if (type instanceof Class) {
            final Class<?> typeClass = (Class<?>)type;
            if (Properties.class.isAssignableFrom(typeClass)) {
                // Handle properties
                return new TypescriptMapType(TypescriptStringType.INSTANCE, TypescriptStringType.INSTANCE);
            }
        }

        throw new IllegalStateException("CollectionTypeMapper invoked with unrecognized dtype " + type);
    }

    private boolean supports(final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            final Class<?> rawTypeClass = (Class<?>) parameterizedType.getRawType();

            // Handle collections
            return Collection.class.isAssignableFrom(rawTypeClass) || Map.class.isAssignableFrom(rawTypeClass);
        } else if (type instanceof Class) {
            // Handle Properties
            final Class<?> typeClass = (Class<?>)type;
            return Properties.class.isAssignableFrom(typeClass);
        }

        return false;
    }
}
