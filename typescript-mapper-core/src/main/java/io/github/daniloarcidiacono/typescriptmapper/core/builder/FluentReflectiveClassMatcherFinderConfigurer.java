package io.github.daniloarcidiacono.typescriptmapper.core.builder;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;
import io.github.daniloarcidiacono.typescriptmapper.core.finder.ReflectiveClassMatcherFinder;
import io.github.daniloarcidiacono.typescriptmapper.core.matcher.AnnotationClassMatcher;
import io.github.daniloarcidiacono.typescriptmapper.core.matcher.ClassMatcher;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.annotation.Annotation;

public class FluentReflectiveClassMatcherFinderConfigurer<T> {
    private final T context;
    private final ReflectiveClassMatcherFinder finder;

    // Configurer context
    private Reflections reflections;
    private AnnotationClassMatcher typescriptDtoAnnotationClassMatcher;

    public FluentReflectiveClassMatcherFinderConfigurer(final T context, final ReflectiveClassMatcherFinder finder) {
        this.context = context;
        this.finder = finder;
    }

    public FluentReflectiveClassMatcherFinderConfigurer<T> ofPackage(final String packageName) {
        reflections = new Reflections(packageName, new SubTypesScanner(false));
        finder.setReflections(reflections);

        return this;
    }

    public FluentReflectiveClassMatcherFinderConfigurer<T> withStandardMatchers() {
        return matchTypescriptDTOAnnotation(true);
    }

    public FluentReflectiveClassMatcherFinderConfigurer<T> withoutStandardMatchers() {
        return matchTypescriptDTOAnnotation(false);
    }

    public FluentReflectiveClassMatcherFinderConfigurer<T> matchTypescriptDTOAnnotation(final boolean match) {
        if (match) {
            if (typescriptDtoAnnotationClassMatcher == null) {
                typescriptDtoAnnotationClassMatcher = new AnnotationClassMatcher(TypescriptDTO.class);
                withClassMatcher(typescriptDtoAnnotationClassMatcher);
            }
        } else {
            if (typescriptDtoAnnotationClassMatcher != null) {
                withoutClassMatcher(typescriptDtoAnnotationClassMatcher);
                typescriptDtoAnnotationClassMatcher = null;
            }
        }

        return this;
    }

    public FluentReflectiveClassMatcherFinderConfigurer<T> matchClassesAnnotatedWith(final Class<? extends Annotation> annotationClass) {
        return withClassMatcher(new AnnotationClassMatcher(annotationClass));
    }

    public FluentReflectiveClassMatcherFinderConfigurer<T> withClassMatcher(final ClassMatcher matcher) {
        finder.getMatcher().add(matcher);
        return this;
    }

    public FluentReflectiveClassMatcherFinderConfigurer<T> withoutClassMatcher(final ClassMatcher matcher) {
        finder.getMatcher().remove(matcher);
        return this;
    }

    public T and() {
        if (reflections == null) {
            throw new IllegalStateException("No package has been specified (use .ofPackage())");
        }

        return context;
    }
}
