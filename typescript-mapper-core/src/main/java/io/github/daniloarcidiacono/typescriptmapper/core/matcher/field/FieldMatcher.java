package io.github.daniloarcidiacono.typescriptmapper.core.matcher.field;

import java.lang.reflect.Field;

@FunctionalInterface
public interface FieldMatcher {
    boolean matches(final Field field);
}
