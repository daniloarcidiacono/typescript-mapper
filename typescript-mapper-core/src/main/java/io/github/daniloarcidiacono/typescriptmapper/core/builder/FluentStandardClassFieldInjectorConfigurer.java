package io.github.daniloarcidiacono.typescriptmapper.core.builder;

import io.github.daniloarcidiacono.typescriptmapper.core.mapper.injector.StandardClassFieldInjector;
import io.github.daniloarcidiacono.typescriptmapper.core.matcher.ClassMatcher;
import io.github.daniloarcidiacono.typescriptmapper.core.matcher.field.FieldMatcher;

import java.lang.reflect.Modifier;

public class FluentStandardClassFieldInjectorConfigurer<T> {
    private final T context;
    private final StandardClassFieldInjector standardClassFieldInjector;

    // Configurer context
    private FieldMatcher staticFieldMatcher;

    public FluentStandardClassFieldInjectorConfigurer(final T context, final StandardClassFieldInjector standardClassFieldInjector) {
        this.context = context;
        this.standardClassFieldInjector = standardClassFieldInjector;
    }

    public FluentStandardClassFieldInjectorConfigurer<T> withStandardExclusions() {
        return excludeStaticFields(true);
    }

    public FluentStandardClassFieldInjectorConfigurer<T> withoutStandardExclusions() {
        return excludeStaticFields(false);
    }

    public FluentStandardClassFieldInjectorConfigurer<T> excludeStaticFields(final boolean exclude) {
        if (exclude) {
            if (staticFieldMatcher == null) {
                staticFieldMatcher = field -> (field.getModifiers() & Modifier.STATIC) != 0;
                standardClassFieldInjector.getFieldFilter().add(staticFieldMatcher);
            }
        } else {
            if (staticFieldMatcher != null) {
                standardClassFieldInjector.getFieldFilter().remove(staticFieldMatcher);
                staticFieldMatcher = null;
            }
        }

        return this;
    }

    public FluentStandardClassFieldInjectorConfigurer<T> withFieldExclusionMatcher(final FieldMatcher matcher) {
        standardClassFieldInjector.getFieldFilter().add(matcher);
        return this;
    }

    public FluentStandardClassFieldInjectorConfigurer<T> withoutFieldExclusionMatcher(final FieldMatcher matcher) {
        standardClassFieldInjector.getFieldFilter().remove(matcher);
        return this;
    }

    public FluentStandardClassFieldInjectorConfigurer<T> withClassExclusionMatcher(final ClassMatcher matcher) {
        standardClassFieldInjector.getClassFilter().add(matcher);
        return this;
    }

    public FluentStandardClassFieldInjectorConfigurer<T> withoutClassExclusionMatcher(final ClassMatcher matcher) {
        standardClassFieldInjector.getClassFilter().add(matcher);
        return this;
    }

    public T and() {
        return context;
    }
}
