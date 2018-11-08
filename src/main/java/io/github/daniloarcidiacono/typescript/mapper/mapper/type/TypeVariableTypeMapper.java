package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptGenericType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeVariableTypeMapper implements TypeMapper {
    public TypeVariableTypeMapper() {
    }

    @Override
    public TypescriptType map(final Type type) {
        if (!supports(type)) {
            return null;
        }

        return new TypescriptGenericType(type.getTypeName());
    }

    private boolean supports(Type type) {
        return type instanceof TypeVariable;
    }
}
