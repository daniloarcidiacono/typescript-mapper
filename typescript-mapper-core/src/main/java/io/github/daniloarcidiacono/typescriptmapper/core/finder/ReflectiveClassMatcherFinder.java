package io.github.daniloarcidiacono.typescriptmapper.core.finder;

import io.github.daniloarcidiacono.typescriptmapper.core.matcher.CompositeClassMatcher;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class ReflectiveClassMatcherFinder extends ReflectiveClassFinder {
    private final CompositeClassMatcher matcher = new CompositeClassMatcher();

    public ReflectiveClassMatcherFinder(final Reflections reflections) {
        super(reflections);
    }

    @Override
    public Set<Class<?>> find() {
        // https://stackoverflow.com/questions/35584995/why-doesnt-reflections-getsubtypesofobject-class-find-enums
        Set<Class<?>> collect = reflections.getSubTypesOf(Object.class).stream().filter(clazz -> matcher.matches(clazz)).collect(Collectors.toSet());
        Set<Class<? extends Enum>> collect1 = reflections.getSubTypesOf(Enum.class).stream().filter(clazz -> matcher.matches(clazz)).collect(Collectors.toSet());
        collect.addAll(collect1);
        return collect;
    }

    public CompositeClassMatcher getMatcher() {
        return matcher;
    }
}
