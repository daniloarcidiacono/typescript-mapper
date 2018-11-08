package io.github.daniloarcidiacono.typescript.mapper.mapper.field;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptStringConstantType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptUnionType;

import java.lang.reflect.Field;

/**
 * Field mapper that integrates with Jackson annotations
 * <ul>
 *     <li>{@link JsonTypeInfo}</li>
 *     <li>{@link JsonSubTypes}</li>
 * </ul>
 *
 * @author Danilo Arcidiacono
 */
public class JacksonFieldMapper implements FieldMapper {
    @Override
    public TypescriptField mapFieldInternal(Field field) {
        if (!supports(field)) {
            return null;
        }

        final JsonSubTypes subTypesAnnotation = field.getDeclaringClass().getAnnotation(JsonSubTypes.class);
        final TypescriptUnionType fieldType = new TypescriptUnionType();
        for (JsonSubTypes.Type subType : subTypesAnnotation.value()) {
            fieldType.type(new TypescriptStringConstantType(subType.name()));
        }

        return new TypescriptField(field.getName(), fieldType);
    }

    private boolean supports(final Field field) {
        return field.getDeclaringClass().isAnnotationPresent(JsonSubTypes.class) &&
               field.getDeclaringClass().isAnnotationPresent(JsonTypeInfo.class) &&
               field.getName().equals(field.getDeclaringClass().getAnnotation(JsonTypeInfo.class).property());
    }
}
