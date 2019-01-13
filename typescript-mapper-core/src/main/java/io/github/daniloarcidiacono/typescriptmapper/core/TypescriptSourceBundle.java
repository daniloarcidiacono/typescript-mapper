package io.github.daniloarcidiacono.typescriptmapper.core;

import io.github.daniloarcidiacono.typescript.template.TypescriptSource;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Danilo Arcidiacono
 */
public class TypescriptSourceBundle {
    private Map<URI, TypescriptSource> bundle = new HashMap<>();
    private Map<TypescriptSource, URI> sourceMap = new HashMap<>();
    private Map<TypescriptDeclaration, Class<?>> classMap = new HashMap<>();

    public TypescriptSourceBundle() {
    }

    public TypescriptSourceBundle source(final URI uri, final TypescriptSource source) {
        bundle.put(uri, source);
        sourceMap.put(source, uri);
        return this;
    }

    public TypescriptSource get(final URI uri) {
        return bundle.get(uri);
    }

    public TypescriptSource getOrCreate(URI uri) {
        if (!bundle.containsKey(uri)) {
            final TypescriptSource source = new TypescriptSource();
            bundle.put(uri, source);
            sourceMap.put(source, uri);
        }

        return bundle.get(uri);
    }

    public URI pathOf(final TypescriptSource source) {
        return sourceMap.get(source);
    }

    public boolean contains(final URI relativePath) {
        return bundle.containsKey(relativePath);
    }

    public TypescriptSourceBundle addMapping(final Class<?> javaClass, final TypescriptDeclaration declaration) {
        classMap.put(declaration, javaClass);
        return this;
    }

    public Class<?> getJavaClass(final TypescriptDeclaration declaration) {
        return classMap.get(declaration);
    }

    public Set<URI> getPaths() {
        return bundle.keySet();
    }

    public Collection<TypescriptSource> getSources() {
        return bundle.values();
    }
}