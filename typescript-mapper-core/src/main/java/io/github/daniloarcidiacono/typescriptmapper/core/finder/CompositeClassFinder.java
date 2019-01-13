package io.github.daniloarcidiacono.typescriptmapper.core.finder;

import io.github.daniloarcidiacono.commons.lang.patterns.Composite;

import java.util.HashSet;
import java.util.Set;

public class CompositeClassFinder extends Composite<TypescriptClassFinder> implements TypescriptClassFinder {
    public CompositeClassFinder() {
    }

    @Override
    public Set<Class<?>> find() {
        final Set<Class<?>> result = new HashSet<>();
        for (TypescriptClassFinder finder : components) {
            result.addAll(finder.find());
        }

        return result;
    }
}
