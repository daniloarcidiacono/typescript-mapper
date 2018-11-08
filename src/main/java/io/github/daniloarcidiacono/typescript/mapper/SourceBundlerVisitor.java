package io.github.daniloarcidiacono.typescript.mapper;

import io.github.daniloarcidiacono.typescript.mapper.resolver.CompositeSourceMapper;
import io.github.daniloarcidiacono.typescript.mapper.resolver.PackageSourceMapper;
import io.github.daniloarcidiacono.typescript.template.TypescriptSource;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

import java.net.URI;

public class SourceBundlerVisitor implements RecursiveMapperVisitor {
    private final TypescriptSourceBundle bundle = new TypescriptSourceBundle();
    private final CompositeSourceMapper sourceMapper = new CompositeSourceMapper();

    public SourceBundlerVisitor() {
    }

    @Override
    public void visit(final Class<?> javaClass, final TypescriptDeclaration declaration) {
        final URI uri = sourceMapper.map(javaClass, declaration);
        if (uri != null) {
            final TypescriptSource source = bundle.getOrCreate(uri);
            source.statement(declaration);
            bundle.addMapping(javaClass, declaration);
        } else {
            throw new IllegalStateException("Could not map class " + javaClass.getCanonicalName() + " to a typescript source");
        }
    }

    public TypescriptSourceBundle getBundle() {
        return bundle;
    }

    public CompositeSourceMapper getSourceMapper() {
        return sourceMapper;
    }
}
