package io.github.daniloarcidiacono.typescriptmapper.core.processor;

import io.github.daniloarcidiacono.typescriptmapper.core.TypescriptSourceBundle;

@FunctionalInterface
public interface SourceBundleProcessor {
    void process(final TypescriptSourceBundle bundle);
}
