package io.github.daniloarcidiacono.typescript.mapper.processor;

import io.github.daniloarcidiacono.typescript.mapper.TypescriptSourceBundle;

@FunctionalInterface
public interface SourceBundleProcessor {
    void process(final TypescriptSourceBundle bundle);
}
