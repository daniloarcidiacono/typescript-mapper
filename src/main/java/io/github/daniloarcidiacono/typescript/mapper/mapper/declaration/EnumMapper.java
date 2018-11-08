package io.github.daniloarcidiacono.typescript.mapper.mapper.declaration;

import io.github.daniloarcidiacono.commons.lang.StringCommons;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescript.mapper.registry.TypescriptIdentifierRegistry;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptEnum;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class EnumMapper implements TypescriptDeclarationMapper {
    private TypescriptIdentifierRegistry idRegistry;

    public EnumMapper(TypescriptIdentifierRegistry idRegistry) {
        this.idRegistry = idRegistry;
    }

    @Override
    public TypescriptEnum map(final Type type) {
        final Class<?> clazz = (Class<?>)type;
        final TypescriptComments docAnnotation = clazz.getAnnotation(TypescriptComments.class);
        final String identifier = idRegistry.registerIdentifier(clazz);
        final TypescriptEnum declaration = new TypescriptEnum(identifier);
        if (docAnnotation != null) {
            declaration.getComments().comment(docAnnotation.value());
        }

        // @TODO: Handle this better
        int i = 0;
        final Object[] enumValues = clazz.getEnumConstants();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isEnumConstant()) {
                declaration.entry(field.getName(), StringCommons.singleQuote(enumValues[i].toString()));
                i++;
            }
        }

        return declaration;
    }

    @Override
    public boolean supports(Type type) {
        return (type instanceof Class) && ((Class<?>)type).isEnum();
    }

    public TypescriptIdentifierRegistry getIdRegistry() {
        return idRegistry;
    }

    public void setIdRegistry(TypescriptIdentifierRegistry idRegistry) {
        this.idRegistry = idRegistry;
    }
}
