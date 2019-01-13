package io.github.daniloarcidiacono.typescriptmapper.core.testcases.expanding.sources;

import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptDTO;

@TypescriptDTO
public class Expanding<T> {
    Class otherClass;
    Expanding<Dependency> other;
}
