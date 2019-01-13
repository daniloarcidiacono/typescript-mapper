package io.github.daniloarcidiacono.typescriptmapper.core.builder;

import io.github.daniloarcidiacono.typescriptmapper.core.registry.AnnotationIdentifierGenerator;
import io.github.daniloarcidiacono.typescriptmapper.core.registry.ClassIdentifierGenerator;
import io.github.daniloarcidiacono.typescriptmapper.core.registry.IdentifierGenerator;
import io.github.daniloarcidiacono.typescriptmapper.core.registry.TypescriptIdentifierRegistry;

public class FluentRegistryConfigurer<T> {
    private final T context;
    private final TypescriptIdentifierRegistry registry;

    // Configurer context
    private AnnotationIdentifierGenerator annotationIdentifierGenerator;
    private ClassIdentifierGenerator generator;

    // Sub-configurers
    private FluentClassIdentifierGeneratorConfigurer<FluentRegistryConfigurer<T>> fluentClassIdentifierGeneratorConfigurer;

    public FluentRegistryConfigurer(final T context, final TypescriptIdentifierRegistry registry) {
        this.context = context;
        this.registry = registry;
    }

    public FluentRegistryConfigurer<T> withIdentifierGenerator(final IdentifierGenerator generator) {
        registry.getGenerator().add(generator);
        return this;
    }

    public FluentRegistryConfigurer<T> withoutIdentifierGenerator(final IdentifierGenerator generator) {
        registry.getGenerator().remove(generator);
        return this;
    }

    public FluentRegistryConfigurer<T> withStandardGenerators() {
        return checkAnnotations(true).withClassIdentifierGenerator().and();
    }

    public FluentRegistryConfigurer<T> withoutStandardGenerators() {
        return checkAnnotations(false).withoutClassIdentifierGenerator();
    }

    public FluentRegistryConfigurer<T> checkAnnotations(final boolean check) {
        if (check) {
            if (annotationIdentifierGenerator == null) {
                annotationIdentifierGenerator = new AnnotationIdentifierGenerator();
                withIdentifierGenerator(annotationIdentifierGenerator);
            }
        } else {
            if (annotationIdentifierGenerator != null) {
                withoutIdentifierGenerator(annotationIdentifierGenerator);
                annotationIdentifierGenerator = null;
            }
        }

        return this;
    }

    public FluentClassIdentifierGeneratorConfigurer<FluentRegistryConfigurer<T>> withClassIdentifierGenerator() {
        if (generator == null) {
            generator = new ClassIdentifierGenerator();
            withIdentifierGenerator(generator);
        }

        if (fluentClassIdentifierGeneratorConfigurer == null) {
            fluentClassIdentifierGeneratorConfigurer = new FluentClassIdentifierGeneratorConfigurer<>(this, generator);
        }

        return fluentClassIdentifierGeneratorConfigurer;
    }

    public FluentRegistryConfigurer<T> withoutClassIdentifierGenerator() {
        if (generator != null) {
            withoutIdentifierGenerator(generator);
            generator = null;
        }

        if (fluentClassIdentifierGeneratorConfigurer != null) {
            fluentClassIdentifierGeneratorConfigurer = null;
        }

        return this;
    }

    public T and() {
        return context;
    }
}
