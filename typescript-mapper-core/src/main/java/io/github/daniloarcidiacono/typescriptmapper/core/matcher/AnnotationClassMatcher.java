package io.github.daniloarcidiacono.typescriptmapper.core.matcher;

import java.lang.annotation.Annotation;

public class AnnotationClassMatcher implements ClassMatcher {
    private final Class<? extends Annotation> annotation;

    public AnnotationClassMatcher(final Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return clazz != null && clazz.isAnnotationPresent(annotation);
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }
}
