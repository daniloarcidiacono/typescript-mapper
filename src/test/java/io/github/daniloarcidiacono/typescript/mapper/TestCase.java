package io.github.daniloarcidiacono.typescript.mapper;

import io.github.daniloarcidiacono.typescript.mapper.builder.FluentConfigurer;

import java.net.URI;

@FunctionalInterface
public interface TestCase {
    void configure(final FluentConfigurer configurer, final URI uri);
}
