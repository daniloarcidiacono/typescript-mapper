package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.*;

import java.lang.reflect.Type;

public class PrimitiveTypeMapper implements TypeMapper {
    @Override
    public TypescriptType map(Type type) {
        if (!supports(type)) {
            return null;
        }

        final Class<?> clazz = (Class<?>)type;
        if (Object.class.equals(clazz)) {
            return TypescriptAnyType.INSTANCE;
        }

        if (String.class.equals(clazz)) {
            return TypescriptStringType.INSTANCE;
        }

        if (void.class.equals(clazz) || Void.class.equals(clazz)) {
            return TypescriptVoidType.INSTANCE;
        }

        if (byte.class.equals(clazz) || Byte.class.equals(clazz) ||
            short.class.equals(clazz) || Short.class.equals(clazz) ||
            int.class.equals(clazz) || Integer.class.equals(clazz) ||
            long.class.equals(clazz) || Long.class.equals(clazz) ||
            float.class.equals(clazz) || Float.class.equals(clazz) ||
            double.class.equals(clazz) || Double.class.equals(clazz)) {
            return TypescriptNumberType.INSTANCE;
        }

        if (boolean.class.equals(clazz) || Boolean.class.equals(clazz)) {
            return TypescriptBooleanType.INSTANCE;
        }

        if (char.class.equals(clazz) || Character.class.equals(clazz)) {
            return TypescriptStringType.INSTANCE;
        }

        // @TODO: Throw an exception
        return TypescriptNullType.INSTANCE;
    }

    private boolean supports(Type type) {
        return type instanceof Class && (isBoxedType((Class)type) || isPrimitiveType((Class)type));
    }

    private boolean isPrimitiveType(final Class<?> clazz) {
        return clazz.isPrimitive();
    }

    private boolean isBoxedType(final Class<?> clazz) {
        return Boolean.class.equals(clazz) ||
                Character.class.equals(clazz) ||
                Byte.class.equals(clazz) ||
                Short.class.equals(clazz) ||
                Integer.class.equals(clazz) ||
                Long.class.equals(clazz) ||
                Float.class.equals(clazz) ||
                Double.class.equals(clazz) ||
                Void.class.equals(clazz) ||
                Object.class.equals(clazz) ||
                String.class.equals(clazz);
    }
}
