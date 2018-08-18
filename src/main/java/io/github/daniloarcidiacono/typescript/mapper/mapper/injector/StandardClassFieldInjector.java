package io.github.daniloarcidiacono.typescript.mapper.mapper.injector;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescript.mapper.mapper.field.FieldMapper;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptInterface;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class StandardClassFieldInjector implements ClassFieldInjector {
    // Field mapper
    private FieldMapper fieldMapper;

    public StandardClassFieldInjector(final FieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
    }

    @Override
    public void injectFields(final TypescriptInterface iface, final Class<?> javaClass) {
        final TypescriptDTO dtoAnnotation = javaClass.getAnnotation(TypescriptDTO.class);

        // This does not get inherited fields
        for (Field field : javaClass.getDeclaredFields()) {
            // Do not parse synthetic fields
            if (field.isSynthetic()) {
                continue;
            }

            // Skip fields with black-listed modifiers in TypescriptDTO ignoreFieldModifiers
            // This can be used to skip static/private fields etc...
            if (dtoAnnotation != null) {
                boolean ignore = false;
                for (int modifier : dtoAnnotation.ignoreFieldModifiers()) {
                    if ((field.getModifiers() & modifier) == modifier) {
                        ignore = true;
                        break;
                    }
                }

                if (ignore) {
                    continue;
                }
            }

            // Skip ignored fields
            if (field.isAnnotationPresent(io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField.class)) {
                final io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField annotation = field.getAnnotation(io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptField.class);
                if (annotation.ignore()) {
                    continue;
                }
            }

            final TypescriptField tsField = fieldMapper.mapField(field);
            iface.getFields().add(tsField);
        }
    }

    @Override
    public boolean supports(Class<?> javaClass) {
        return true;
    }

    public FieldMapper getFieldMapper() {
        return fieldMapper;
    }

    public void setFieldMapper(FieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
    }
}