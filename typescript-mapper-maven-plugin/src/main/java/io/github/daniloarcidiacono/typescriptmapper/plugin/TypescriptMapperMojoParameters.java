package io.github.daniloarcidiacono.typescriptmapper.plugin;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates the parameters that are accepted by {@link TypescriptMapperMojo}.
 */
public class TypescriptMapperMojoParameters {
    private URL outputDirectory;
    private String configurerClass;
    private String basePackage;
    private boolean injectPreamble;
    private List<String> preamble;
    private String suffix;
    private String enclosingSeparator;
    private Map<String, URI> mappings;
    private boolean injectImports;
    private boolean sortDeclarations;
    private boolean simplifyResult;
    private boolean excludeJavaClasses;
    private boolean annotationConfig;
    private boolean excludeStaticFields;

    public TypescriptMapperMojoParameters() {
    }

    public URL getOutputDirectory() {
        return outputDirectory;
    }

    public String getConfigurerClass() {
        return configurerClass;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public boolean isInjectPreamble() {
        return injectPreamble;
    }

    public List<String> getPreamble() {
        return preamble;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getEnclosingSeparator() {
        return enclosingSeparator;
    }

    public Map<String, URI> getMappings() {
        return mappings;
    }

    public boolean isInjectImports() {
        return injectImports;
    }

    public boolean isSortDeclarations() {
        return sortDeclarations;
    }

    public boolean isSimplifyResult() {
        return simplifyResult;
    }

    public boolean isExcludeJavaClasses() {
        return excludeJavaClasses;
    }

    public boolean isAnnotationConfig() {
        return annotationConfig;
    }

    public boolean isExcludeStaticFields() {
        return excludeStaticFields;
    }

    TypescriptMapperMojoParameters setOutputDirectory(URL outputDirectory) {
        this.outputDirectory = outputDirectory;
        return this;
    }

    TypescriptMapperMojoParameters setConfigurerClass(String configurerClass) {
        this.configurerClass = configurerClass;
        return this;
    }

    TypescriptMapperMojoParameters setBasePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }

    TypescriptMapperMojoParameters setInjectPreamble(boolean injectPreamble) {
        this.injectPreamble = injectPreamble;
        return this;
    }

    TypescriptMapperMojoParameters setPreamble(List<String> preamble) {
        this.preamble = preamble;
        return this;
    }

    TypescriptMapperMojoParameters setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    TypescriptMapperMojoParameters setEnclosingSeparator(String enclosingSeparator) {
        this.enclosingSeparator = enclosingSeparator;
        return this;
    }

    TypescriptMapperMojoParameters setMappings(Map<String, URI> mappings) {
        this.mappings = mappings;
        return this;
    }

    TypescriptMapperMojoParameters setInjectImports(boolean injectImports) {
        this.injectImports = injectImports;
        return this;
    }

    TypescriptMapperMojoParameters setSortDeclarations(boolean sortDeclarations) {
        this.sortDeclarations = sortDeclarations;
        return this;
    }

    TypescriptMapperMojoParameters setSimplifyResult(boolean simplifyResult) {
        this.simplifyResult = simplifyResult;
        return this;
    }

    TypescriptMapperMojoParameters setExcludeJavaClasses(boolean excludeJavaClasses) {
        this.excludeJavaClasses = excludeJavaClasses;
        return this;
    }

    TypescriptMapperMojoParameters setAnnotationConfig(boolean annotationConfig) {
        this.annotationConfig = annotationConfig;
        return this;
    }

    TypescriptMapperMojoParameters setExcludeStaticFields(boolean excludeStaticFields) {
        this.excludeStaticFields = excludeStaticFields;
        return this;
    }
}
