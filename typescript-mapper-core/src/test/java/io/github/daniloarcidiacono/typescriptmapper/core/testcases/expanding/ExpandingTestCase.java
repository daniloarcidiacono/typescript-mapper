package io.github.daniloarcidiacono.typescriptmapper.core.testcases.expanding;

import io.github.daniloarcidiacono.typescriptmapper.core.TestCase;
import io.github.daniloarcidiacono.typescriptmapper.core.builder.FluentConfigurer;

import java.net.URI;

public class ExpandingTestCase implements TestCase {
    @Override
    public void configure(final FluentConfigurer configurer, final URI uri) {
        configurer.simplifyResult(false);
    }
}