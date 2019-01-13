package io.github.daniloarcidiacono.typescriptmapper.core.mapper.injector;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescriptmapper.core.mapper.field.FieldMapper;
import io.github.daniloarcidiacono.typescriptmapper.core.matcher.CompositeClassMatcher;
import io.github.daniloarcidiacono.typescriptmapper.core.matcher.field.CompositeFieldMatcher;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptInterface;

import java.lang.reflect.Field;

public class StandardClassFieldInjector implements ClassFieldInjector {
    // Field mapper
    private final FieldMapper fieldMapper;

    // Class filters
    private final CompositeClassMatcher classFilter = new CompositeClassMatcher();

    // Field filters
    private final CompositeFieldMatcher fieldFilter = new CompositeFieldMatcher();

    /**
     * Initializers the injector with no configuration.
     * @param fieldMapper the field mapper
     */
    public StandardClassFieldInjector(final FieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
    }

    @Override
    public void injectFields(final TypescriptInterface iface, final Class<?> javaClass) {
        final TypescriptDTO dtoAnnotation = javaClass.getAnnotation(TypescriptDTO.class);

        // This does not get inherited fields
        for (Field field : javaClass.getDeclaredFields()) {
            // @TODO: Implement as field filters!
            // Do not parse synthetic fields
            if (field.isSynthetic()) {
                continue;
            }

            if (fieldFilter.matches(field)) {
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
            if (field.isAnnotationPresent(io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField.class)) {
                final io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField annotation = field.getAnnotation(io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptField.class);
                if (annotation.ignore()) {
                    continue;
                }
            }

            if (classFilter.matches(field.getType())) {
                continue;
            }

            // Map the field
            final TypescriptField tsField = fieldMapper.mapField(field);

            // Skip unsupported fields
            if (tsField == null) {
                continue;
            }

            iface.getFields().add(tsField);
        }
    }

    @Override
    public boolean supports(final Class<?> javaClass) {
        return true;
    }

    public FieldMapper getFieldMapper() {
        return fieldMapper;
    }

    public CompositeClassMatcher getClassFilter() {
        return classFilter;
    }

    public CompositeFieldMatcher getFieldFilter() {
        return fieldFilter;
    }
}