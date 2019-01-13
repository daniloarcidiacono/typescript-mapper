package io.github.daniloarcidiacono.typescriptmapper.core;

import io.github.daniloarcidiacono.typescriptmapper.core.builder.MappingChainBuilder;

import java.net.URI;

public class TestCaseResult {
    private final String name;
    private final MappingChainBuilder chainBuilder;
    private final URI generatedUri;
    private final URI expectedUri;

    public TestCaseResult(final String name, final MappingChainBuilder chainBuilder, final URI generatedUri, final URI expectedUri) {
        this.name = name;
        this.chainBuilder = chainBuilder;
        this.generatedUri = generatedUri;
        this.expectedUri = expectedUri;
    }

    public TestCaseResult assertMatchesFolder() throws Exception {
        SourceBundleMatcher.assertMatches(expectedUri.toString(), chainBuilder.getVisitor().getBundle());
        return this;
    }
}
