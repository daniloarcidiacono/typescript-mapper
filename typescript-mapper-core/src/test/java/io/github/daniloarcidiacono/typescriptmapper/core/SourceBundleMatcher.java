package io.github.daniloarcidiacono.typescriptmapper.core;

import io.github.daniloarcidiacono.commons.lang.FileCommons;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SourceBundleMatcher {
    /**
     * Checks that the folder structure matches the one generated by the renderer.
     * @param path the path (in resources folder) of the base folder to compare.
     * @throws Exception
     */
    public static void assertMatches(final String path, final TypescriptSourceBundle bundle) throws Exception {
        final Path folder = Paths.get(
            SourceBundleMatcher.class.getClassLoader().getResource(path).toURI()
        );

        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                try {
                    final URI relativePath = new URI(folder.relativize(file).toString());
                    assertTrue(bundle.contains(relativePath), "Expected to find " + relativePath);

                    final String resourcePath = Paths.get(path, relativePath.toString()).toString();
                    final String resource = FileCommons.loadResource(resourcePath);
                    assertEquals(resource, bundle.get(relativePath).render());
                    return FileVisitResult.CONTINUE;
                } catch (URISyntaxException e) {
                    throw new IOException(e);
                }
            }
        });
    }
}
