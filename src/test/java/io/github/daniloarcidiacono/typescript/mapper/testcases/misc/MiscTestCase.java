package io.github.daniloarcidiacono.typescript.mapper.testcases.misc;

import io.github.daniloarcidiacono.typescript.mapper.TestCase;
import io.github.daniloarcidiacono.typescript.mapper.builder.FluentConfigurer;

import java.net.URI;

public class MiscTestCase implements TestCase {
    @Override
    public void configure(final FluentConfigurer configurer, final URI uri) {
        configurer.simplifyResult(false).recursiveMapping().identifierRegistry().withClassIdentifierGenerator().withSuffix("");
    }
}