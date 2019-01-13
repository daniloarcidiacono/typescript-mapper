package io.github.daniloarcidiacono.typescriptmapper.core.matcher.field;

import io.github.daniloarcidiacono.commons.lang.patterns.Composite;

import java.lang.reflect.Field;

public class CompositeFieldMatcher extends Composite<FieldMatcher> implements FieldMatcher {
    @Override
    public boolean matches(final Field field) {
        for (FieldMatcher matcher : components) {
            if (matcher.matches(field)) {
                return true;
            }
        }

        return false;
    }
}
