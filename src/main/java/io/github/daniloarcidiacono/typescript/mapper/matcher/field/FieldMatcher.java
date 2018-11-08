package io.github.daniloarcidiacono.typescript.mapper.matcher.field;

import java.lang.reflect.Field;

@FunctionalInterface
public interface FieldMatcher {
    boolean matches(final Field field);
}
