package io.github.daniloarcidiacono.typescriptmapper.core.builder;

import io.github.daniloarcidiacono.typescriptmapper.core.resolver.PackageSourceMapper;

public class FluentPackageSourceMapperConfigurer<T> {
    private final T context;
    private final PackageSourceMapper packageSourceMapper;

    public FluentPackageSourceMapperConfigurer(final T context, final PackageSourceMapper packageSourceMapper) {
        this.context = context;
        this.packageSourceMapper = packageSourceMapper;
    }

    public FluentPackageSourceMapperConfigurer<T> withStaticMapping(final String packageName, final String mappedUri) {
        packageSourceMapper.addMapping(packageName, mappedUri);
        return this;
    }

    public T and() {
        return context;
    }
}
