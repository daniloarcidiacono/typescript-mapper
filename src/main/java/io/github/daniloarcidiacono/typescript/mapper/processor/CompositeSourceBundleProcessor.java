package io.github.daniloarcidiacono.typescript.mapper.processor;

import io.github.daniloarcidiacono.typescript.mapper.TypescriptSourceBundle;
import io.github.daniloarcidiacono.commons.lang.Composite;

public class CompositeSourceBundleProcessor extends Composite<SourceBundleProcessor> implements SourceBundleProcessor {
    @Override
    public void process(final TypescriptSourceBundle bundle) {
        for (SourceBundleProcessor component : components) {
            component.process(bundle);
        }
    }
}
