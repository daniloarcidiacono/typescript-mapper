package io.github.daniloarcidiacono.typescript.mapper.resolver;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapped;

import java.net.URI;
import java.util.*;

public interface UriResolver {
    default Map<URI, List<TypescriptMapped>> distribute(final Collection<TypescriptMapped> mapped) {
        final Map<URI, List<TypescriptMapped>> result = new HashMap<>();
        for (TypescriptMapped current : mapped) {
            if (!canResolve(current)) {
                throw new IllegalStateException("Could not resolve " + current);
            }

            final URI path = resolve(current);
            if (!result.containsKey(path)) {
                result.put(path, new ArrayList<>());
            }

            result.get(path).add(current);
        }

        return result;
    }

    URI resolve(final TypescriptMapped mapped);
    boolean canResolve(final TypescriptMapped mapped);
}