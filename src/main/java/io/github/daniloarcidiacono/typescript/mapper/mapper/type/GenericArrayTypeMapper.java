package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptArrayType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

public class GenericArrayTypeMapper implements TypeMapper {
    private TypeMapper typeMapper;

    public GenericArrayTypeMapper(final TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    @Override
    public TypescriptType map(final Type type) {
        if (!supports(type)) {
            return null;
        }

        final GenericArrayType arrayType = (GenericArrayType)type;

        // @TODO: Which policy to apply?
        final TypescriptType innerType = typeMapper.map(arrayType.getGenericComponentType());
        return innerType != null ? new TypescriptArrayType(innerType) : null;
    }

    private boolean supports(final Type type) {
        return type instanceof GenericArrayType;
    }
}
