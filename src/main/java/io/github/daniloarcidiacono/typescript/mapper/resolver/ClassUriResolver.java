package io.github.daniloarcidiacono.typescript.mapper.resolver;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapped;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ClassUriResolver implements UriResolver {
    private Map<Class<?>, URI> mappings = new HashMap<>();

    public ClassUriResolver() {
    }

    public ClassUriResolver add(final Class<?> javaClass, final URI resolved) {
        mappings.put(javaClass, resolved);
        return this;
    }

    @Override
    public URI resolve(final TypescriptMapped mapped) {
        if (!mappings.containsKey(mapped.getJavaClass())) {
            throw new IllegalStateException("Called with " + mapped + " but no mappings match");
        }

        return mappings.get(mapped.getJavaClass());
    }

    @Override
    public boolean canResolve(final TypescriptMapped mapped) {
        return mapped != null && mappings.containsKey(mapped.getJavaClass());
    }

    public Map<Class<?>, URI> getMappings() {
        return mappings;
    }

    public void setMappings(Map<Class<?>, URI> mappings) {
        this.mappings = mappings;
    }
}