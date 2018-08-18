package io.github.daniloarcidiacono.typescript.mapper.renderer;

import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMapped;
import io.github.daniloarcidiacono.typescript.mapper.mapper.TypescriptMappedComparator;
import io.github.daniloarcidiacono.typescript.mapper.resolver.CompositeUriResolver;
import io.github.daniloarcidiacono.typescript.mapper.resolver.UriResolver;
import io.github.daniloarcidiacono.typescript.template.TypescriptStringBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TypescriptStdOutRenderer implements TypescriptRenderer {
    private CompositeUriResolver fileResolver = new CompositeUriResolver();

    public TypescriptStdOutRenderer() {
    }

    public TypescriptStdOutRenderer resolver(final UriResolver resolver) {
        fileResolver.resolver(resolver);
        return this;
    }

    public TypescriptStdOutRenderer resolver(final UriResolver...resolvers) {
        fileResolver.resolver(resolvers);
        return this;
    }

    @Override
    public void render(final Collection<TypescriptMapped> mapped) {
        final Map<URI, List<TypescriptMapped>> distributed = fileResolver.distribute(mapped);
        for (URI path : distributed.keySet()) {
            final TypescriptStringBuilder sb = new TypescriptStringBuilder();
            final List<TypescriptMapped> pathDeclarations = distributed.get(path);
            pathDeclarations.sort(new TypescriptMappedComparator());
            PREAMBLE.render(sb);
            for (TypescriptMapped declaration : pathDeclarations) {
                declaration.getDeclaration().render(sb);
                sb.appendln();
            }

            sb.append("-------------------------------\n");
            sb.append(path.toString());
            sb.append("\n");
            sb.append("-------------------------------\n");
            sb.append(sb.toString());
            sb.append("\n\n");
        }
    }
}
    
