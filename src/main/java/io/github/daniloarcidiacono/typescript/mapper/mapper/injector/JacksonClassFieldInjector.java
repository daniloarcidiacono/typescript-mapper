package io.github.daniloarcidiacono.typescript.mapper.mapper.injector;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.daniloarcidiacono.typescript.mapper.mapper.field.FieldMapper;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptInterface;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptStringConstantType;

import java.lang.reflect.Field;

/**
 * Class field injector that integrates with Jackson annotations
 * <ul>
 *     <li>{@link JsonTypeInfo}</li>
 *     <li>{@link JsonSubTypes}</li>
 * </ul>
 *
 * @author Danilo Arcidiacono
 */
public class JacksonClassFieldInjector implements ClassFieldInjector {
    // Field mapper
    private FieldMapper fieldMapper;

    public JacksonClassFieldInjector(final FieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
    }

    @Override
    public void injectFields(final TypescriptInterface iface, final Class<?> javaClass) {
        final JsonTypeInfo typeInfoAnnotation = javaClass.getSuperclass().getAnnotation(JsonTypeInfo.class);
        final JsonSubTypes subTypesAnnotation = javaClass.getSuperclass().getAnnotation(JsonSubTypes.class);
        for (JsonSubTypes.Type subType : subTypesAnnotation.value()) {
            if (subType.value().equals(javaClass)) {
                final TypescriptField field = new TypescriptField(typeInfoAnnotation.property(), new TypescriptStringConstantType(subType.name()), TypescriptField.MANDATORY);
                iface.field(field);
            }
        }
    }

    @Override
    public boolean supports(Class<?> javaClass) {
        return javaClass.getSuperclass() != null &&
               javaClass.getSuperclass().isAnnotationPresent(JsonSubTypes.class) &&
               javaClass.getSuperclass().isAnnotationPresent(JsonTypeInfo.class);
    }

    public FieldMapper getFieldMapper() {
        return fieldMapper;
    }

    public void setFieldMapper(FieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
    }
}