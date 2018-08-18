package io.github.daniloarcidiacono.typescript.mapper;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapper;
import io.github.daniloarcidiacono.typescript.mapper.resolver.PackageUriResolver;
import org.junit.jupiter.api.Test;

import java.net.URI;

/**
 * @author Danilo Arcidiacono
 */
class MapperTest {
    @Test
    void generics() throws Exception {
        testBundle("generics");
    }

    @Test
    void misc() throws Exception {
        testBundle("misc");
    }

    private void testBundle(final String name) throws Exception {
        final TypescriptTestRenderer renderer = new TypescriptTestRenderer();

        renderer.resolver(
            new PackageUriResolver()
                .add("io.github.daniloarcidiacono.typescript.mapper." + name, new URI(name + ".ts"))
        )
        .render(
            new TypescriptMapper()
                .scanPackage("io.github.daniloarcidiacono.typescript.mapper." + name)
                .map()
        );

        renderer.assertMatches(name);
    }
}