package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapper;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptEnumType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;

public class EnumTypeMapper implements TypeMapper {
    private TypescriptMapper typescriptMapper;

    public EnumTypeMapper(final TypescriptMapper typescriptMapper) {
        this.typescriptMapper = typescriptMapper;
    }

    @Override
    public TypescriptType map(final Type type) {
        final Class<?> clazz = (Class<?>)type;
        typescriptMapper.addClass(clazz);

        final String identifier = typescriptMapper.getIdRegistry().registerIdentifier(clazz);
        return new TypescriptEnumType(identifier);
    }


    @Override
    public boolean supports(Type type) {
        return type instanceof Class && ((Class)type).isEnum();
    }
}
