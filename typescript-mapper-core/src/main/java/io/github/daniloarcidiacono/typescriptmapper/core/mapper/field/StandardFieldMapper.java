package io.github.daniloarcidiacono.typescriptmapper.core.mapper.field;

import io.github.daniloarcidiacono.typescriptmapper.core.mapper.type.TypeMapper;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Field;

public class StandardFieldMapper implements FieldMapper {
    private TypeMapper typeMapper;

    public StandardFieldMapper(final TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    @Override
    public TypescriptField mapFieldInternal(Field field) {
        final io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField annotation = field.getAnnotation(io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField.class);
        final TypescriptType mappedType = typeMapper.map(
            annotation != null && !annotation.type().equals(void.class) ? annotation.type() : field.getGenericType()
        );
        if (mappedType == null) {
            return null;
        }

        return new TypescriptField(
            field.getName(),
            mappedType
        );
    }
}
