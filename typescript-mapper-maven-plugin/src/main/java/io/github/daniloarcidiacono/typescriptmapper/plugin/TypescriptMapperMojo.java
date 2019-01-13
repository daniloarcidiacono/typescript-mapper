package io.github.daniloarcidiacono.typescriptmapper.plugin;

import io.github.daniloarcidiacono.typescriptmapper.core.builder.FluentConfigurer;
import io.github.daniloarcidiacono.typescriptmapper.core.builder.MappingChainBuilder;
import io.github.daniloarcidiacono.typescript.template.TypescriptComments;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.classworlds.realm.ClassRealm;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Maven plugin that invokes the typescript mapper.
 */
@Mojo(
    name = "map",
    defaultPhase = LifecyclePhase.COMPILE,
    requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME
)
public class TypescriptMapperMojo extends AbstractMojo {
    @Component
    private PluginDescriptor descriptor;

    @Component
    private MavenProject project;

    @Parameter(defaultValue = "file:///${project.build.directory}/mapped/", required = true)
    private URL outputDirectory;

    @Parameter(required = true)
    private String basePackage;

    @Parameter(defaultValue = "true")
    private boolean injectPreamble;

    @Parameter
    private List<String> preamble;

    @Parameter
    private String suffix;

    @Parameter
    private String enclosingSeparator;

    @Parameter
    private Map<String, URI> mappings;

    @Parameter(defaultValue = "true")
    private boolean injectImports;

    @Parameter(defaultValue = "true")
    private boolean sortDeclarations;

    @Parameter(defaultValue = "true")
    private boolean simplifyResult;

    @Parameter(defaultValue = "true")
    private boolean excludeJavaClasses;

    @Parameter(defaultValue = "true")
    private boolean annotationConfig;

    @Parameter(defaultValue = "true")
    private boolean excludeStaticFields;

    private void init() throws MojoExecutionException {
        final ClassRealm realm = descriptor.getClassRealm();
        try {
            for (String element : project.getCompileClasspathElements()) {
                final File elementFile = new File(element);
                getLog().debug("Added URL: " + elementFile.toURI().toURL());
                realm.addURL(elementFile.toURI().toURL());
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Couldn't create a classloader.", e);
        }
    }

    @Override
    public void execute() throws MojoExecutionException {
        init();

        try {
            getLog().info("Starting");
            getLog().info("Base package: " + basePackage);
            getLog().info("Output directory: " + outputDirectory);

            // Map
            final MappingChainBuilder builder = new MappingChainBuilder();
            final FluentConfigurer configurer = FluentConfigurer.forChain(builder)
                .standard(basePackage, outputDirectory.toURI())
                    .injectPreamble(injectPreamble)
                    .injectImports(injectImports)
                    .sortDeclarations(sortDeclarations)
                    .simplifyResult(simplifyResult)
                    .recursiveMapping()
                        .scanClasses(true)
                            .matchTypescriptDTOAnnotation(annotationConfig)
                        .and()
                        .excludeJavaClasses(excludeJavaClasses)
                        .identifierRegistry()
                            .checkAnnotations(annotationConfig)
                            .withClassIdentifierGenerator()
                                .withSuffix("")
                                .withEnclosingSeparator("")
                            .and()
                        .and()
                        .withStandardClassFieldInjector()
                            .excludeStaticFields(excludeStaticFields)
                        .and()
                    .and();

            if (suffix != null) {
                configurer
                    .recursiveMapping()
                        .identifierRegistry()
                            .withClassIdentifierGenerator()
                                .withSuffix(suffix);
            }

            if (enclosingSeparator != null) {
                configurer
                    .recursiveMapping()
                        .identifierRegistry()
                            .withClassIdentifierGenerator()
                                .withEnclosingSeparator(enclosingSeparator);
            }

            if (preamble != null) {
                configurer.preamble(new TypescriptComments(preamble));
            }

            if (mappings != null) {
                for (String packageName : mappings.keySet()) {
                    configurer.withPackageSourceMapper().withStaticMapping(packageName, mappings.get(packageName).toString());
                }
            }

            builder.process();
        } catch (Exception e) {
            throw new MojoExecutionException("Error while executing the plugin", e);
        }
    }
}
