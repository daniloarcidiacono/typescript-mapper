package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;

@FunctionalInterface
public interface TypeMapper {
    /**
     * Maps the specified Java type to the corresponding TypeScript type.
     * @param type the type to convert
     * @return The mapped type, or null if this mapper can not handle the passed type.
     */
    TypescriptType map(final Type type);
}
