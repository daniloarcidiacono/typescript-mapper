package io.github.daniloarcidiacono.typescriptmapper.core.matcher;

@FunctionalInterface
public interface ClassMatcher {
    boolean matches(final Class<?> clazz);
}
