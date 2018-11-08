package io.github.daniloarcidiacono.typescript.mapper.processor;

import io.github.daniloarcidiacono.typescript.analyzer.TypescriptAnalyzer;
import io.github.daniloarcidiacono.typescript.mapper.TypescriptSourceBundle;
import io.github.daniloarcidiacono.typescript.template.TypescriptSource;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;
import io.github.daniloarcidiacono.typescript.template.statement.imports.TypescriptImportSelector;
import io.github.daniloarcidiacono.typescript.template.statement.imports.TypescriptNamespaceImport;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImportsSourceBundleProcessor implements SourceBundleProcessor {
    private TypescriptAnalyzer analyzer;

    @Override
    public void process(final TypescriptSourceBundle bundle) {
        analyzer = new TypescriptAnalyzer(bundle);

        // For each source
        for (TypescriptSource source : bundle.getSources()) {
            // Destination source, identifiers to import
            final Map<TypescriptSource, Set<String>> importMap = new HashMap<>();

            // For each dependency of the source
            for (TypescriptDeclaration dependency : analyzer.getSourceDependencies(source)) {
                final TypescriptSource destSource = dependency.getSource();
                if (!importMap.containsKey(destSource)) {
                    importMap.put(destSource, new HashSet<>());
                }

                importMap.get(destSource).add(dependency.getIdentifier());
            }


            // For each import statement neede
            for (Map.Entry<TypescriptSource, Set<String>> importEntry : importMap.entrySet()) {
                // Calculate the import path relative to the source
                final String importPathStr = computeImportPath(bundle, source, importEntry.getKey());

                // Build the import statement
                final TypescriptNamespaceImport importStatement = new TypescriptNamespaceImport(importPathStr);
                for (String importedIdentifier : importEntry.getValue()) {
                    importStatement.selector(new TypescriptImportSelector(importedIdentifier));
                }

                // Inject the import into the source
                source.getStatements().add(0, importStatement);
            }
        }
    }

    private String computeImportPath(final TypescriptSourceBundle bundle, final TypescriptSource source, final TypescriptSource destSource) {
        final Path sourcePath = Paths.get(
            bundle.pathOf(source).toString()
        ).getParent();

        final Path includePath = Paths.get(
            bundle.pathOf(destSource).toString()
        );

        final Path importPath = sourcePath != null ? sourcePath.relativize(includePath) : includePath;
        String importPathStr = removeExtension(importPath.toString());
        if (!importPathStr.startsWith(".")) {
            importPathStr = "./" + importPathStr;
        }

        return importPathStr;
    }

    private String removeExtension(String fileName) {
        if (fileName == null) {
            return null;
        }

        final int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        }

        return fileName.substring(0, index);
    }
}
