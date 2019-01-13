package io.github.daniloarcidiacono.typescriptmapper.plugin;

import io.github.daniloarcidiacono.typescript.template.TypescriptComments;
import io.github.daniloarcidiacono.typescriptmapper.core.builder.FluentConfigurer;
import io.github.daniloarcidiacono.typescriptmapper.core.builder.MappingChainBuilder;
import org.apache.maven.plugin.logging.Log;

import java.net.URI;
import java.util.Map;

/**
 * Default plugin configurer, invoked if {@link TypescriptMapperMojoParameters#configurerClass} is <strong>not</strong> set.
 * <p>Custom configurers may extend this class to apply additional logic.
 *
 * @see TypescriptMapperMojo
 */
public class DefaultTypescriptMapperMojoConfigurer implements TypescriptMapperMojoConfigurer {
    private FluentConfigurer fluentConfigurer;

    @Override
    public void configure(final TypescriptMapperMojoParameters parameters,
                          final MappingChainBuilder builder,
                          final Log log) throws Exception {
        fluentConfigurer = FluentConfigurer.forChain(builder)
            .standard(parameters.getBasePackage(), parameters.getOutputDirectory().toURI())
                .injectPreamble(parameters.isInjectPreamble())
                .injectImports(parameters.isInjectImports())
                .sortDeclarations(parameters.isSortDeclarations())
                .simplifyResult(parameters.isSimplifyResult())
                .recursiveMapping()
                    .scanClasses(true)
                        .matchTypescriptDTOAnnotation(parameters.isAnnotationConfig())
                    .and()
                    .excludeJavaClasses(parameters.isExcludeJavaClasses())
                    .identifierRegistry()
                        .checkAnnotations(parameters.isAnnotationConfig())
                        .withClassIdentifierGenerator()
                            .withSuffix(parameters.getSuffix() != null ? parameters.getSuffix() : "")
                            .withEnclosingSeparator(parameters.getEnclosingSeparator() != null ? parameters.getEnclosingSeparator() : "")
                        .and()
                    .and()
                    .withStandardClassFieldInjector()
                        .excludeStaticFields(parameters.isExcludeStaticFields())
                    .and()
                .and();

        if (parameters.getPreamble() != null) {
            fluentConfigurer.preamble(new TypescriptComments(parameters.getPreamble()));
        }

        final Map<String, URI> mappings = parameters.getMappings();
        if (mappings != null) {
            for (String packageName : mappings.keySet()) {
                fluentConfigurer.withPackageSourceMapper().withStaticMapping(packageName, mappings.get(packageName).toString());
            }
        }
    }

    /**
     * Returns the fluent configurer used by {@link #configure(TypescriptMapperMojoParameters, MappingChainBuilder, Log)}.
     * Subclasses must use this configurer instead of creating a new one, if they want to alter
     * some configurations.
     *
     * @return the fluent configurer (must be invoked after {@link #configure(TypescriptMapperMojoParameters, MappingChainBuilder, Log)}
     */
    public FluentConfigurer getFluentConfigurer() {
        return fluentConfigurer;
    }

}
