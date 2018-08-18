package io.github.daniloarcidiacono.typescript.mapper.resolver;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapped;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class PackageUriResolver implements UriResolver {
    private Map<String, URI> packageMap = new HashMap<>();

    public PackageUriResolver add(final String packageName, final URI pathName) {
        packageMap.put(packageName, pathName);
        return this;
    }

    public PackageUriResolver add(final Class<?> clazz, final URI pathName) {
        return add(clazz.getPackage().getName(), pathName);
    }

    public PackageUriResolver add(final Package pack, final URI pathName) {
        return add(pack.getName(), pathName);
    }

    @Override
    public URI resolve(final TypescriptMapped mapped) {
        final String packageName = mapped.getJavaClass().getPackage().getName();

        try {
            return packageMap.containsKey(packageName) ?
                   packageMap.get(packageName) :
                   new URI(packageName.replace('.', '/') + ".ts");
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Wrong uri", e);
        }
    }

    @Override
    public boolean canResolve(final TypescriptMapped mapped) {
        return mapped != null;
    }
}

