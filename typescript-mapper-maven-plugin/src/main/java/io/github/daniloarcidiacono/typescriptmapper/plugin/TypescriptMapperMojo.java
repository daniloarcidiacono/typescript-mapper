package io.github.daniloarcidiacono.typescriptmapper.plugin;

import io.github.daniloarcidiacono.typescriptmapper.core.builder.MappingChainBuilder;
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
class TypescriptMapperMojo extends AbstractMojo {
    @Component
    private PluginDescriptor descriptor;

    @Component
    private MavenProject project;

    @Parameter(defaultValue = "file:///${project.build.directory}/mapped/", required = true)
    private URL outputDirectory;

    @Parameter(required = false)
    private String configurerClass;

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

            final TypescriptMapperMojoConfigurer configurer;

            // If we have a configurer specified...
            if (configurerClass != null && !configurerClass.isEmpty()) {
                try {
                    // Instance it
                    final Class<?> aClass = Class.forName(configurerClass);

                    if (!TypescriptMapperMojoConfigurer.class.isAssignableFrom(aClass)) {
                        throw new Exception("Provided configurerClass " + configurerClass + " is of type " + aClass.getSimpleName() + ", which is not a subtype of TypescriptMapperMojoConfigurer.");
                    }

                    configurer = (TypescriptMapperMojoConfigurer) aClass.newInstance();
                } catch (ClassNotFoundException e) {
                    throw new Exception("Provided configurerClass " + configurerClass + " not found");
                } catch (ClassCastException | InstantiationException | IllegalAccessException ex) {
                    throw new Exception("Could not instance configurerClass " + configurerClass, ex);
                }

                // Success
                getLog().info("Using configurer " + configurerClass);
            } else {
                // Otherwise, use the default implementation
                configurer = new DefaultTypescriptMapperMojoConfigurer();
            }

            // Create the mojo parameters object
            final TypescriptMapperMojoParameters parameters = new TypescriptMapperMojoParameters()
                .setOutputDirectory(outputDirectory)
                .setConfigurerClass(configurerClass)
                .setBasePackage(basePackage)
                .setInjectPreamble(injectPreamble)
                .setPreamble(preamble)
                .setSuffix(suffix)
                .setEnclosingSeparator(enclosingSeparator)
                .setMappings(mappings)
                .setInjectImports(injectImports)
                .setSortDeclarations(sortDeclarations)
                .setSimplifyResult(simplifyResult)
                .setExcludeJavaClasses(excludeJavaClasses)
                .setAnnotationConfig(annotationConfig)
                .setExcludeStaticFields(excludeStaticFields);

            // Configure the mapping chain
            final MappingChainBuilder builder = new MappingChainBuilder();
            configurer.configure(parameters, builder, getLog());

            // Run the mapping chain
            builder.process();
        } catch (Exception e) {
            getLog().error("Error while executing the plugin: " + e.getMessage());
            throw new MojoExecutionException("Error while executing the plugin", e);
        }
    }
}
