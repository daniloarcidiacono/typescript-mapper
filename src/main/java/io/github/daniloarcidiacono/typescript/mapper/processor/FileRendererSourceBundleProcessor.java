package io.github.daniloarcidiacono.typescript.mapper.processor;

import io.github.daniloarcidiacono.commons.lang.FileCommons;
import io.github.daniloarcidiacono.typescript.mapper.TypescriptSourceBundle;
import io.github.daniloarcidiacono.typescript.template.TypescriptSource;
import io.github.daniloarcidiacono.typescript.template.TypescriptStringBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileRendererSourceBundleProcessor implements SourceBundleProcessor {
    private URI baseUri;

    public FileRendererSourceBundleProcessor(final URI baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public void process(final TypescriptSourceBundle bundle) {
        try {
            // Purge the output folder
            final Path basePath = Paths.get(baseUri);
            FileCommons.deleteFolder(basePath);

            // For each URI
            for (URI path : bundle.getPaths()) {
                final TypescriptSource source = bundle.get(path);
                final TypescriptStringBuilder sb = new TypescriptStringBuilder();

                // Render
                source.render(sb);

                // Create the necessary directory tree
                final Path finalPath = Paths.get(baseUri.resolve(path));
                Files.createDirectories(finalPath.getParent());

                // Write to file
                final String content = sb.toString();
                Files.write(finalPath, content.getBytes(), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Exception while rendering", e);
        }
    }

    public URI getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(URI baseUri) {
        this.baseUri = baseUri;
    }
}
