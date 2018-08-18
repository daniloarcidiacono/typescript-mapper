package io.github.daniloarcidiacono.typescript.mapper.resolver;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapped;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CompositeUriResolver implements UriResolver {
    private List<UriResolver> resolvers = new ArrayList<>();

    public CompositeUriResolver() {
    }

    public CompositeUriResolver resolver(final UriResolver resolver) {
        resolvers.add(resolver);
        return this;
    }

    public CompositeUriResolver resolver(final UriResolver...resolvers) {
        for (UriResolver resolver : resolvers) {
            resolver(resolver);
        }
        return this;
    }

    @Override
    public URI resolve(final TypescriptMapped mapped) {
        for (UriResolver resolver : resolvers) {
            if (resolver.canResolve(mapped)) {
                return resolver.resolve(mapped);
            }
        }

        throw new IllegalStateException("No resolvers could resolve " + mapped);
    }

    @Override
    public boolean canResolve(final TypescriptMapped mapped) {
        for (UriResolver resolver : resolvers) {
            if (resolver.canResolve(mapped)) {
                return true;
            }
        }

        return false;
    }
}