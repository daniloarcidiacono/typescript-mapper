package io.github.daniloarcidiacono.typescript.mapper.mapper.field;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeFieldMapper implements FieldMapper {
    private List<FieldMapper> mappers = new ArrayList<>();

    public CompositeFieldMapper() {
    }

    public CompositeFieldMapper mapper(final FieldMapper mapper) {
        mappers.add(mapper);
        return this;
    }

    public CompositeFieldMapper mapper(final FieldMapper ...mapper) {
        Collections.addAll(mappers, mapper);
        return this;
    }

    @Override
    public TypescriptField mapFieldInternal(Field field) {
        for (FieldMapper mapper : mappers) {
            if (mapper.supports(field)) {
                return mapper.mapFieldInternal(field);
            }
        }

        throw new IllegalStateException("No field mapper for " + field);
    }

    @Override
    public boolean supports(Field field) {
        for (FieldMapper mapper : mappers) {
            if (mapper.supports(field)) {
                return true;
            }
        }

        return false;
    }
}
