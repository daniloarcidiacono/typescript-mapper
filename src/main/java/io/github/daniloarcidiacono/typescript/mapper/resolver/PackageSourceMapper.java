package io.github.daniloarcidiacono.typescript.mapper.resolver;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Gathers every Java class of a given package to a separate TypeScript file.
 * For example: com.foo.bar.ClassOne =&gt; com/foo/bar.ts
 * For example: com.foo.bar.ClassTwo =&gt; com/foo/bar.ts
 * For example: com.foo.bar.baz.ClassThree =&gt; com/foo/bar/baz.ts
 *
 * @author Danilo Arcidiacono
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
