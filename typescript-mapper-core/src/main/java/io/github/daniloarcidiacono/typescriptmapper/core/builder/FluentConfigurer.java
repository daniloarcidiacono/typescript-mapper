package io.github.daniloarcidiacono.typescriptmapper.core.builder;

import io.github.daniloarcidiacono.typescriptmapper.core.processor.*;
import io.github.daniloarcidiacono.typescriptmapper.core.resolver.PackageSourceMapper;
import io.github.daniloarcidiacono.typescriptmapper.core.resolver.SourceMapper;
import io.github.daniloarcidiacono.typescript.template.statement.TypescriptStatement;

import java.net.URI;

/**
 * Main configurer of {@link MappingChainBuilder}.
 *
 * @see MappingChainBuilder
 */
public class FluentConfigurer {
    private final MappingChainBuilder chainBuilder;

    // Configurer state
    private PreambleSourceProcessor preambleSourceProcessor;
    private ImportsSourceBundleProcessor importsSourceBundleProcessor;
    private SortSourceBundleProcessor sortSourceBundleProcessor;
    private SimplifySourceBundleProcessor simplifySourceBundleProcessor;
    private FileRendererSourceBundleProcessor fileRendererSourceBundleProcessor;
    private PackageSourceMapper packageSourceMapper;

    // Sub-configurers
    private FluentRecursiveMapperConfigurer<FluentConfigurer> recursiveMapperConfigurer;
    private FluentPackageSourceMapperConfigurer<FluentConfigurer> packageSourceMapperConfigurer;

    public FluentConfigurer(final MappingChainBuilder chainBuilder) {
        this.chainBuilder = chainBuilder;
    }

    public static FluentConfigurer forChain(final MappingChainBuilder chainBuilder) {
        return new FluentConfigurer(chainBuilder);
    }

    public FluentConfigurer standard(final String rootPackage, final URI uri) {
        return withStandardProcessors(uri)
               .withStandardSourceMappers()
               .recursiveMapping()
                   .standard(rootPackage)
               .and();
    }

    public FluentConfigurer withStandardProcessors(final URI uri) {
        return sortDeclarations(true)
                .simplifyResult(true)
                .injectImports(true)
                .injectPreamble(true)
                .renderToFile(true, uri);
    }

    public FluentPackageSourceMapperConfigurer<FluentConfigurer> withPackageSourceMapper() {
        if (packageSourceMapper == null) {
            packageSourceMapper = new PackageSourceMapper();
            withSourceMapper(packageSourceMapper);
        }

        if (packageSourceMapperConfigurer == null) {
            packageSourceMapperConfigurer = new FluentPackageSourceMapperConfigurer<>(this, packageSourceMapper);
        }

        return packageSourceMapperConfigurer;
    }

    public FluentConfigurer withoutPackageSourceMapper() {
        if (packageSourceMapper != null) {
            withoutSourceMapper(packageSourceMapper);
            packageSourceMapper = null;
        }

        if (packageSourceMapperConfigurer != null) {
            packageSourceMapperConfigurer = null;
        }

        return this;
    }

    public FluentConfigurer withStandardSourceMappers() {
        withPackageSourceMapper();
        return this;
    }

    public FluentConfigurer withoutStandardSourceMappers() {
        return withoutPackageSourceMapper();
    }

    public FluentConfigurer noSourceMappers() {
        withoutPackageSourceMapper();
        chainBuilder.getVisitor().getSourceMapper().removeAll();
        return this;
    }

    public FluentConfigurer injectPreamble(final boolean inject) {
        if (inject) {
            if (preambleSourceProcessor == null) {
                preambleSourceProcessor = new PreambleSourceProcessor();
                chainBuilder.getSourceBundleProcessor().add(preambleSourceProcessor);
            } else {
                preambleSourceProcessor.setPreamble(PreambleSourceProcessor.DEFAULT_PREAMBLE);
            }
        } else {
            if (preambleSourceProcessor != null) {
                chainBuilder.getSourceBundleProcessor().remove(preambleSourceProcessor);
                preambleSourceProcessor = null;
            }
        }

        return this;
    }

    public FluentConfigurer preamble(final TypescriptStatement preamble) {
        if (preambleSourceProcessor != null) {
            preambleSourceProcessor.setPreamble(preamble);
        }

        return this;
    }

    public FluentConfigurer injectImports(final boolean inject) {
        if (inject) {
            if (importsSourceBundleProcessor == null) {
                importsSourceBundleProcessor = new ImportsSourceBundleProcessor();
                chainBuilder.getSourceBundleProcessor().add(importsSourceBundleProcessor);
            }
        } else {
            if (importsSourceBundleProcessor != null) {
                chainBuilder.getSourceBundleProcessor().remove(importsSourceBundleProcessor);
                importsSourceBundleProcessor = null;
            }
        }

        return this;
    }

    public FluentConfigurer sortDeclarations(final boolean sort) {
        if (sort) {
            if (sortSourceBundleProcessor == null) {
                sortSourceBundleProcessor = new SortSourceBundleProcessor();
                chainBuilder.getSourceBundleProcessor().add(sortSourceBundleProcessor);
            }
        } else {
            if (sortSourceBundleProcessor != null) {
                chainBuilder.getSourceBundleProcessor().remove(sortSourceBundleProcessor);
                sortSourceBundleProcessor = null;
            }
        }

        return this;
    }

    public FluentConfigurer simplifyResult(final boolean simplify) {
        if (simplify) {
            if (simplifySourceBundleProcessor == null) {
                simplifySourceBundleProcessor = new SimplifySourceBundleProcessor();
                chainBuilder.getSourceBundleProcessor().add(simplifySourceBundleProcessor);
            }
        } else {
            if (simplifySourceBundleProcessor != null) {
                chainBuilder.getSourceBundleProcessor().remove(simplifySourceBundleProcessor);
                simplifySourceBundleProcessor = null;
            }
        }

        return this;
    }

    public FluentConfigurer renderToFile(final boolean render, final URI baseUri) {
        if (render) {
            if (fileRendererSourceBundleProcessor == null) {
                fileRendererSourceBundleProcessor = new FileRendererSourceBundleProcessor(baseUri);
                chainBuilder.getSourceBundleProcessor().add(fileRendererSourceBundleProcessor);
            } else {
                fileRendererSourceBundleProcessor.setBaseUri(baseUri);
            }
        } else {
            if (fileRendererSourceBundleProcessor != null) {
                chainBuilder.getSourceBundleProcessor().remove(fileRendererSourceBundleProcessor);
                fileRendererSourceBundleProcessor = null;
            }
        }

        return this;
    }

    public FluentConfigurer withProcessor(final SourceBundleProcessor processor) {
        chainBuilder.getSourceBundleProcessor().add(processor);
        return this;
    }

    public FluentConfigurer withoutProcessor(final SourceBundleProcessor processor) {
        chainBuilder.getSourceBundleProcessor().remove(processor);
        return this;
    }

    public FluentConfigurer withSourceMapper(final SourceMapper sourceMapper) {
        chainBuilder.getVisitor().getSourceMapper().add(sourceMapper);
        return this;
    }

    public FluentConfigurer withoutSourceMapper(final SourceMapper sourceMapper) {
        chainBuilder.getVisitor().getSourceMapper().remove(sourceMapper);
        return this;
    }

    public FluentRecursiveMapperConfigurer<FluentConfigurer> recursiveMapping() {
        if (recursiveMapperConfigurer == null) {
            recursiveMapperConfigurer = new FluentRecursiveMapperConfigurer<>(this, chainBuilder.getMapper());
        }

        return recursiveMapperConfigurer;
    }
}
