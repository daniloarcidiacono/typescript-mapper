package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.mapper.registry.TypescriptIdentifierRegistry;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptEnumType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;

public class EnumTypeMapper implements TypeMapper {
    private TypescriptIdentifierRegistry idRegistry;

    public EnumTypeMapper(final TypescriptIdentifierRegistry idRegistry) {
        this.idRegistry = idRegistry;
    }

    @Override
    public TypescriptType map(final Type type) {
        if (!supports(type)) {
            return null;
        }

        final Class<?> clazz = (Class<?>)type;

        // @TODO
        // If the type is an enum, then it must be mapped as well
//        typescriptMapper.addClass(clazz);

        final String identifier = idRegistry.registerIdentifier(clazz);
        return new TypescriptEnumType(identifier);
    }


    private boolean supports(Type type) {
        return type instanceof Class && ((Class)type).isEnum();
    }
}
