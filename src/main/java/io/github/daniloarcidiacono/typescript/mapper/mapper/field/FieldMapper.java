package io.github.daniloarcidiacono.typescript.mapper.mapper.field;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;
import java.lang.reflect.Field;

public interface FieldMapper {
    default TypescriptField mapField(final Field field) {
        final TypescriptField tsField = mapFieldInternal(field);

        // Handle mandatoryness
        final io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField annotation = field.getAnnotation(io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField.class);
        final boolean mandatory = annotation == null || annotation.required();
        tsField.setMandatory(mandatory);

        // Inject the documentation
        final TypescriptComments fieldDocAnnotation = field.getAnnotation(TypescriptComments.class);
        if (fieldDocAnnotation != null) {
            tsField.getComments().comment(fieldDocAnnotation.value());
        }

        return tsField;
    }

    TypescriptField mapFieldInternal(final Field field);
    boolean supports(final Field field);
}
