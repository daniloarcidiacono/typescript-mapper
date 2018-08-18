package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapper;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptEnumType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptInterfaceType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;

public class ClassTypeMapper implements TypeMapper {
    private TypescriptMapper typescriptMapper;

    public ClassTypeMapper(final TypescriptMapper typescriptMapper) {
        this.typescriptMapper = typescriptMapper;
    }

    @Override
    public TypescriptType map(final Type type) {
        final Class<?> clazz = (Class<?>)type;

        // If the type is a class, then it must be mapped as well
        typescriptMapper.addClass(clazz);

        // Determine the class identifier
        final String identifier = typescriptMapper.getIdRegistry().registerIdentifier(clazz);
        return new TypescriptInterfaceType(identifier);
    }


    @Override
    public boolean supports(Type type) {
        return type instanceof Class;
    }
}
