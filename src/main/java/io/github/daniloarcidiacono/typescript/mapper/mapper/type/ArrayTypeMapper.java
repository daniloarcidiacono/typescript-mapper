package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapper;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptArrayType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;

public class ArrayTypeMapper implements TypeMapper {
    private TypescriptMapper typescriptMapper;

    public ArrayTypeMapper(final TypescriptMapper typescriptMapper) {
        this.typescriptMapper = typescriptMapper;
    }

    @Override
    public TypescriptType map(final Type type) {
        final Class<?> clazz = (Class<?>)type;
        return new TypescriptArrayType(typescriptMapper.mapType(clazz.getComponentType()));
    }

    @Override
    public boolean supports(final Type type) {
        return type instanceof Class && ((Class)type).isArray();
    }
}
