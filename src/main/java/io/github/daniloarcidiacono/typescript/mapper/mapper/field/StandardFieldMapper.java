package io.github.daniloarcidiacono.typescript.mapper.mapper.field;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapper;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;

import java.lang.reflect.Field;

public class StandardFieldMapper implements FieldMapper {
    private TypescriptMapper typescriptMapper;

    public StandardFieldMapper(final TypescriptMapper typescriptMapper) {
        this.typescriptMapper = typescriptMapper;
    }

    @Override
    public TypescriptField mapFieldInternal(Field field) {
        final io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField annotation = field.getAnnotation(io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField.class);
        return new TypescriptField(
            field.getName(),
            typescriptMapper.mapType(
                annotation != null && !annotation.type().equals(void.class) ? annotation.type() : field.getGenericType()
            )
        );
    }

    @Override
    public boolean supports(Field field) {
        return true;
    }
}
