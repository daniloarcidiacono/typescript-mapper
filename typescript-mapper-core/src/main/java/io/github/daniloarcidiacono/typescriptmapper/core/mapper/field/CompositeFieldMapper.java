package io.github.daniloarcidiacono.typescriptmapper.core.mapper.field;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;
import io.github.daniloarcidiacono.commons.lang.patterns.Composite;

import java.lang.reflect.Field;

public class CompositeFieldMapper extends Composite<FieldMapper> implements FieldMapper {
    @Override
    public TypescriptField mapFieldInternal(Field field) {
        for (FieldMapper mapper : components) {
            final TypescriptField attempt = mapper.mapFieldInternal(field);
            if (attempt != null) {
                return attempt;
            }
        }

        return null;
    }
}
