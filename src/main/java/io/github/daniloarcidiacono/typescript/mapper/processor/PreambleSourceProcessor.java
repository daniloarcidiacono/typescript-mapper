package io.github.daniloarcidiacono.typescript.mapper.processor;

import io.github.daniloarcidiacono.typescript.mapper.TypescriptSourceBundle;
import io.github.daniloarcidiacono.typescript.template.TypescriptComments;
import io.github.daniloarcidiacono.typescript.template.TypescriptSource;
import io.github.daniloarcidiacono.typescript.template.statement.TypescriptStatement;

public class PreambleSourceProcessor implements SourceBundleProcessor {
    public static final TypescriptStatement DEFAULT_PREAMBLE = new TypescriptComments()
        .comment("This file is automatically generated by TypescriptMapper.")
        .comment("Do not modify this file -- YOUR CHANGES WILL BE ERASED!");

    private TypescriptStatement preamble = DEFAULT_PREAMBLE;

    @Override
    public void process(final TypescriptSourceBundle bundle) {
        for (TypescriptSource source : bundle.getSources()) {
            source.getStatements().add(0, preamble);
        }
    }

    public TypescriptStatement getPreamble() {
        return preamble;
    }

    public PreambleSourceProcessor setPreamble(final TypescriptStatement preamble) {
        this.preamble = preamble;
        return this;
    }
}