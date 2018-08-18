package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeTypeMapper implements TypeMapper {
    private List<TypeMapper> mappers = new ArrayList<>();

    public CompositeTypeMapper mapper(final TypeMapper mapper) {
        mappers.add(mapper);
        return this;
    }

    public CompositeTypeMapper mapper(final TypeMapper ...mapper) {
        Collections.addAll(mappers, mapper);
        return this;
    }

    @Override
    public TypescriptType map(final Type type) {
        for (TypeMapper mapper : mappers) {
            if (mapper.supports(type)) {
                return mapper.map(type);
            }
        }

        throw new IllegalStateException("No type mappers found for " + type);
    }

    @Override
    public boolean supports(final Type type) {
        for (TypeMapper mapper : mappers) {
            if (mapper.supports(type)) {
                return true;
            }
        }

        return false;
    }
}
