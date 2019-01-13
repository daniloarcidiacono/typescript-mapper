package io.github.daniloarcidiacono.typescriptmapper.core.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptArrayType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;

public class ArrayTypeMapper implements TypeMapper {
    private TypeMapper typeMapper;

    public ArrayTypeMapper(final TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    @Override
    public TypescriptType map(final Type type) {
        if (!supports(type)) {
            return null;
        }

        final Class<?> clazz = (Class<?>)type;

        // @TODO: Which policy to apply?
        final TypescriptType innerType = typeMapper.map(clazz.getComponentType());
        return innerType != null ? new TypescriptArrayType(innerType) : null;
    }

    private boolean supports(final Type type) {
        return type instanceof Class && ((Class)type).isArray();
    }
}
