package io.github.daniloarcidiacono.typescript.mapper.generics;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

import java.util.List;
import java.util.Map;

@TypescriptDTO
public class BoundedTypes<T extends A, A extends String, B> {
    public T first;
    public A second;
    public BoundedTypes<T, A, B> self;
    public List<BoundedTypes<T, A, B>> nestedGenerics;
    public List<T> genericList;
    public List<List<T>> nestedList;
    public Map<String, List<BoundedTypes<T, A, List<B>>>> map1;
    public Map<Integer, List<BoundedTypes<T, A, List<B>>>> map2;
    public Map<Double, List<BoundedTypes<T, A, List<B>>>> map3;
    public Map<List<BoundedTypes<T, A, B>>, List<BoundedTypes<T, A, List<B>>>> map4;
}
