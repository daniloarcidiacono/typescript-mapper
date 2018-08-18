package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptGenericType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;

public class GenericTypeMapper implements TypeMapper {
    public GenericTypeMapper() {
    }

    @Override
    public TypescriptType map(final Type type) {
        return new TypescriptGenericType(type.getTypeName());
    }


    @Override
    public boolean supports(Type type) {
        return !(type instanceof Class);
    }
}
