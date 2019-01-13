package io.github.daniloarcidiacono.typescriptmapper.core.resolver;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Gathers every Java class of a given package to a separate TypeScript file.
 * <p>
 * Examples:
 * <ul>
 *  <li>{@code com.foo.bar.ClassOne} =&gt; {@code com/foo/bar.ts}</li>
 *  <li>{@code com.foo.bar.ClassTwo} =&gt; {@code com/foo/bar.ts}</li>
 *  <li>{@code com.foo.bar.baz.ClassThree} =&gt; {@code com/foo/bar/baz.ts}</li>
 * </ul>
 */
public class PackageSourceMapper implements SourceMapper {
    private Map<String, String> packageFixedMappings = new HashMap<>();

    public PackageSourceMapper addMapping(final String packageName, final String mappedUri) {
        packageFixedMappings.put(packageName, mappedUri);
        return this;
    }

    @Override
    public URI map(final Class<?> javaClass, final TypescriptDeclaration declaration) {
        final String packageName = javaClass.getPackage().getName();

        try {
            if (packageFixedMappings.containsKey(packageName)) {
                return new URI(packageFixedMappings.get(packageName));
            }

            return new URI(packageName.replace('.', '/') + ".ts");
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Wrong uri", e);
        }
    }
}
