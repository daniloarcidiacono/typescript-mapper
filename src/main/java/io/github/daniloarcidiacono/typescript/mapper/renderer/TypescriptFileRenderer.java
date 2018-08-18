package io.github.daniloarcidiacono.typescript.mapper.renderer;

import io.github.daniloarcidiacono.commons.lang.FileCommons;
import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapped;
import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMappedComparator;
import io.github.daniloarcidiacono.typescript.mapper.resolver.AnnotationUriResolver;
import io.github.daniloarcidiacono.typescript.mapper.resolver.CompositeUriResolver;
import io.github.daniloarcidiacono.typescript.mapper.resolver.UriResolver;
import io.github.daniloarcidiacono.typescript.template.TypescriptStringBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TypescriptFileRenderer implements TypescriptRenderer {
    private CompositeUriResolver fileResolver = new CompositeUriResolver().resolver(new AnnotationUriResolver());
    private URI baseUri;

    public TypescriptFileRenderer(final URI baseUri) {
        this.baseUri = baseUri;
    }

    public TypescriptFileRenderer resolver(final UriResolver resolver) {
        fileResolver.resolver(resolver);
        return this;
    }

    public TypescriptFileRenderer resolver(final UriResolver...resolvers) {
        fileResolver.resolver(resolvers);
        return this;
    }

    @Override
    public void render(final Collection<TypescriptMapped> mapped) {
        final Map<URI, List<TypescriptMapped>> distributed = fileResolver.distribute(mapped);
        try {
            // Purge the output folder
            final Path basePath = Paths.get(baseUri);
            FileCommons.deleteFolder(basePath);

            // For each URI
            for (URI path : distributed.keySet()) {
                // Create the necessary directory tree
                final Path finalPath = Paths.get(baseUri.resolve(path));
                Files.createDirectories(finalPath.getParent());

                // Render
                final TypescriptStringBuilder sb = new TypescriptStringBuilder();
                PREAMBLE.render(sb);

                final List<TypescriptMapped> pathDeclarations = distributed.get(path);
                pathDeclarations.sort(new TypescriptMappedComparator());
                for (TypescriptMapped declaration : pathDeclarations) {
                    declaration.getDeclaration().render(sb);
                    sb.appendln();
                }

                // Write to file
                final String content = sb.toString();
                Files.write(finalPath, content.getBytes(), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Exception while rendering", e);
        }
    }
}
