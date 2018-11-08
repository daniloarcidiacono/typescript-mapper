package io.github.daniloarcidiacono.typescript.mapper.testcases.expanding.sources;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;

@TypescriptDTO
public class Expanding<T> {
    Class otherClass;
    Expanding<Dependency> other;
}
