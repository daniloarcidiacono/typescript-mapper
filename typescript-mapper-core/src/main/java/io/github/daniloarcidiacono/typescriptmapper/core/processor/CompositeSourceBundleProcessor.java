package io.github.daniloarcidiacono.typescriptmapper.core.processor;

import io.github.daniloarcidiacono.typescriptmapper.core.TypescriptSourceBundle;
import io.github.daniloarcidiacono.commons.lang.patterns.Composite;

public class CompositeSourceBundleProcessor extends Composite<SourceBundleProcessor> implements SourceBundleProcessor {
    @Override
    public void process(final TypescriptSourceBundle bundle) {
        for (SourceBundleProcessor component : components) {
            component.process(bundle);
        }
    }
}
