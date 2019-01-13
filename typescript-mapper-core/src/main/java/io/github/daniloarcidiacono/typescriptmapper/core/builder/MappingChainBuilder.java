package io.github.daniloarcidiacono.typescriptmapper.core.builder;

import io.github.daniloarcidiacono.typescriptmapper.core.RecursiveMapper;
import io.github.daniloarcidiacono.typescriptmapper.core.SourceBundlerVisitor;
import io.github.daniloarcidiacono.typescriptmapper.core.TypescriptSourceBundle;
import io.github.daniloarcidiacono.typescriptmapper.core.processor.CompositeSourceBundleProcessor;

/**
 * Main façade for Typescript mapper chain.<br>
 * RecursiveMapper =&gt; SourceBundlerVisitor =&gt; { SourceBundleProcessor }
 * @author Danilo Arcidiacono
 */
public class MappingChainBuilder {
    // Mapper
    private final RecursiveMapper mapper = new RecursiveMapper();

    // Visitor
    private final SourceBundlerVisitor visitor = new SourceBundlerVisitor();

    // Processors
    private final CompositeSourceBundleProcessor sourceBundleProcessor = new CompositeSourceBundleProcessor();

    /**
     * Initializes the chain builder with no configurations.
     */
    public MappingChainBuilder() {
    }

    public TypescriptSourceBundle process() {
        mapper.map(visitor);

        final TypescriptSourceBundle bundle = visitor.getBundle();
        sourceBundleProcessor.process(bundle);

        return bundle;
    }

    public RecursiveMapper getMapper() {
        return mapper;
    }

    public SourceBundlerVisitor getVisitor() {
        return visitor;
    }

    public CompositeSourceBundleProcessor getSourceBundleProcessor() {
        return sourceBundleProcessor;
    }
}
