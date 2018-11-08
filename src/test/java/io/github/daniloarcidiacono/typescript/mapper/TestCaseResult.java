package io.github.daniloarcidiacono.typescript.mapper;

import io.github.daniloarcidiacono.typescript.mapper.builder.MappingChainBuilder;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
