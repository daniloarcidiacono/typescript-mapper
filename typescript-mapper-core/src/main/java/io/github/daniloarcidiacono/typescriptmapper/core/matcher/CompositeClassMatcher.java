package io.github.daniloarcidiacono.typescriptmapper.core.matcher;

import io.github.daniloarcidiacono.commons.lang.patterns.Composite;

public class CompositeClassMatcher extends Composite<ClassMatcher> implements ClassMatcher {
    @Override
    public boolean matches(final Class<?> clazz) {
        for (ClassMatcher matcher : components) {
            if (matcher.matches(clazz)) {
                return true;
            }
        }

        return false;
    }
}
