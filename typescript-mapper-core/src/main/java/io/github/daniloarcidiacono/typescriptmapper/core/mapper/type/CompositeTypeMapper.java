package io.github.daniloarcidiacono.typescriptmapper.core.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;
import io.github.daniloarcidiacono.commons.lang.patterns.Composite;

import java.lang.reflect.Type;

public class CompositeTypeMapper extends Composite<TypeMapper> implements TypeMapper {
    @Override
    public TypescriptType map(final Type type) {
        for (TypeMapper mapper : components) {
            final TypescriptType attempt = mapper.map(type);
            if (attempt != null) {
                return attempt;
            }
        }

        return null;
    }
}
