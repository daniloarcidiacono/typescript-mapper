package io.github.daniloarcidiacono.typescriptmapper.plugin;

import io.github.daniloarcidiacono.typescriptmapper.core.builder.MappingChainBuilder;
import org.apache.maven.plugin.logging.Log;

/**
 * Configurer invoked by {@link TypescriptMapperMojo}.
 */
public interface TypescriptMapperMojoConfigurer {
    /**
     * Configures the mapping chain.
     *
     * @param parameters the plugin parameters
     * @param builder the builder of typescript mapping chain
     * @param log logger object
     * @throws Exception if an error occurs (the plugin will not run)
     */
    void configure(final TypescriptMapperMojoParameters parameters, final MappingChainBuilder builder, final Log log) throws Exception;
}
