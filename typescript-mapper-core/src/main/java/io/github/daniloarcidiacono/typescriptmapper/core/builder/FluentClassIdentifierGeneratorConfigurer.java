package io.github.daniloarcidiacono.typescriptmapper.core.builder;

import io.github.daniloarcidiacono.typescriptmapper.core.registry.ClassIdentifierGenerator;

public class FluentClassIdentifierGeneratorConfigurer<T> {
    private final T context;
    private final ClassIdentifierGenerator generator;

    public FluentClassIdentifierGeneratorConfigurer(final T context, final ClassIdentifierGenerator generator) {
        this.context = context;
        this.generator = generator;
    }

    public FluentClassIdentifierGeneratorConfigurer<T> withSuffix(final String suffix) {
        generator.setIdentifierSuffix(suffix);
        return this;
    }

    public FluentClassIdentifierGeneratorConfigurer<T> withEnclosingSeparator(final String enclosingSeparator) {
        generator.setEnclosingSeparator(enclosingSeparator);
        return this;
    }

    public T and() {
        return context;
    }
}
