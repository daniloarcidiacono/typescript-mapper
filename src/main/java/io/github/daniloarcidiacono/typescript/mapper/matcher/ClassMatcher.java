package io.github.daniloarcidiacono.typescript.mapper.matcher;

@FunctionalInterface
public interface ClassMatcher {
    boolean matches(final Class<?> clazz);
}
