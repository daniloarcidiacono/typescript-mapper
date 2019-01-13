package io.github.daniloarcidiacono.typescriptmapper.core.resolver;

import io.github.daniloarcidiacono.typescriptmapper.core.matcher.ClassMatcher;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ClassMatcherSourceMapper implements SourceMapper {
    private Map<ClassMatcher, URI> mappings = new HashMap<>();

    public ClassMatcherSourceMapper() {
    }

    public ClassMatcherSourceMapper classMatcher(final ClassMatcher classMatcher, final URI resolved) {
        mappings.put(classMatcher, resolved);
        return this;
    }

    @Override
    public URI map(final Class<?> javaClass, final TypescriptDeclaration declaration) {
        if (!canMap(javaClass, declaration)) {
            return null;
        }

        for (ClassMatcher classMatcher : mappings.keySet()) {
            if (classMatcher.matches(javaClass)) {
                return mappings.get(classMatcher);
            }
        }

        throw new IllegalStateException("Called with " + javaClass.getCanonicalName() + " but no matches");
    }

    private boolean canMap(final Class<?> javaClass, final TypescriptDeclaration declaration) {
        for (ClassMatcher classMatcher : mappings.keySet()) {
            if (classMatcher.matches(javaClass)) {
                return true;
            }
        }

        return false;
    }

    public Map<ClassMatcher, URI> getMappings() {
        return mappings;
    }

    public ClassMatcherSourceMapper setMappings(final Map<ClassMatcher, URI> mappings) {
        this.mappings = mappings;
        return this;
    }
}