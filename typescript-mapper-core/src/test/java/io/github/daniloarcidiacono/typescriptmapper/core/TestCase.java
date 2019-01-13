package io.github.daniloarcidiacono.typescriptmapper.core;

import io.github.daniloarcidiacono.typescriptmapper.core.builder.FluentConfigurer;

import java.net.URI;

@FunctionalInterface
public interface TestCase {
    void configure(final FluentConfigurer configurer, final URI uri);
}
