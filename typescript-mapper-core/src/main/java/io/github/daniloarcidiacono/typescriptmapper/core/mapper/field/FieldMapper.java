package io.github.daniloarcidiacono.typescriptmapper.core.mapper.field;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;

import java.lang.reflect.Field;

public interface FieldMapper {
    default TypescriptField mapField(final Field field) {
        final TypescriptField tsField = mapFieldInternal(field);
        if (tsField == null) {
            return null;
        }

        // Handle mandatoryness
        final io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField annotation = field.getAnnotation(io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField.class);
        final boolean mandatory = annotation == null || annotation.required();
        tsField.setMandatory(mandatory);

        // Inject the documentation
        final TypescriptComments fieldDocAnnotation = field.getAnnotation(TypescriptComments.class);
        if (fieldDocAnnotation != null) {
            tsField.getComments().comment(fieldDocAnnotation.value());
        }

        return tsField;
    }

    /**
     * Maps the specified Java field to the corresponding TypeScript field.
     * @param field the field to map
     * @return The mapped type, or null if this mapper can not handle the passed type.
     */
    TypescriptField mapFieldInternal(final Field field);
}
