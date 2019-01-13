package io.github.daniloarcidiacono.typescriptmapper.core.testcases.generics.sources;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

import java.util.List;
import java.util.Map;

@TypescriptDTO
class Generic<T> {
}

@TypescriptDTO
public class BoundedTypes<T extends A, A extends String, B> {
    @TypescriptDTO
    class NestedGeneric {
        T t;
    }

    public Generic<?>[] genericArray;
    public Generic<? extends A> wildcardExtends;
    public Generic<? super A> wildcardSuper;
    public Generic<?> wildcard;
    public T first;
    public A second;
    public BoundedTypes omittedParams;
    public BoundedTypes<T, A, B> self;
    public List<BoundedTypes<T, A, B>> nestedGenerics;
    public List<T> genericList;
    public List<List<T>> nestedList;
    public Map<String, List<BoundedTypes<T, A, List<B>>>> map1;
    public Map<Integer, List<BoundedTypes<T, A, List<B>>>> map2;
    public Map<Double, List<BoundedTypes<T, A, List<B>>>> map3;
    public Map<List<BoundedTypes<T, A, B>>, List<BoundedTypes<T, A, List<B>>>> map4;
}

@TypescriptDTO
class Base<K> {
    @TypescriptDTO
    static class DerivedStatic<K, V> {
        K a;
        V b;
    }

    @TypescriptDTO
    class Derived<V> {
        K a;
        V b;
    }

    @TypescriptDTO
    interface NestedInterface<K, V> {
        K m1();
        V m2();
    }
}