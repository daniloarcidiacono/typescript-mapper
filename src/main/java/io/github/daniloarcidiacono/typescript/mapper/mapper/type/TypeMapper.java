package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;

public interface TypeMapper {
    TypescriptType map(final Type type);
    boolean supports(final Type type);
}
