package io.github.daniloarcidiacono.typescriptmapper.core.finder;

import org.reflections.Reflections;

public abstract class ReflectiveClassFinder implements TypescriptClassFinder {
    protected Reflections reflections;

    public ReflectiveClassFinder(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Reflections getReflections() {
        return reflections;
    }

    public void setReflections(Reflections reflections) {
        this.reflections = reflections;
    }
}
