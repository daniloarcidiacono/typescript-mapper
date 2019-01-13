package io.github.daniloarcidiacono.typescriptmapper.analyzer;

import io.github.daniloarcidiacono.typescriptmapper.core.TypescriptSourceBundle;
import io.github.daniloarcidiacono.typescript.template.TypescriptSource;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;
import io.github.daniloarcidiacono.typescript.template.statement.TypescriptStatement;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptEnumType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptInterfaceType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.util.*;

/**
 * Main class for static analysis of a {@link TypescriptSourceBundle}.
 *
 * @see TypescriptSourceBundle
 * @author Danilo Arcidiacono
 */
public class TypescriptAnalyzer {
    private final TypescriptSourceBundle sourceBundle;

    /**
     * Declaration identifier -> Declaration
     */
    private final Map<String, TypescriptDeclaration> declarationMap = new HashMap<>();

    /**
     * Statement -> { TypescriptDeclaration }
     * Maps each statement to the set of its declaration dependencies.
     */
    private final Map<TypescriptStatement, Set<TypescriptDeclaration>> statementDependencies = new HashMap<>();

    /**
     * Source -> { TypescriptDeclaration }
     * Maps each source to the set of its (external) declaration dependencies.
     */
    private final Map<TypescriptSource, Set<TypescriptDeclaration>> sourceDependencies = new HashMap<>();

    /**
     * List of empty declarations
     */
    private final List<TypescriptDeclaration> emptyDeclarations = new ArrayList<>();

    public TypescriptAnalyzer(final TypescriptSourceBundle sourceBundle) {
        this.sourceBundle = sourceBundle;
        process();
    }

    public void reset() {
        declarationMap.clear();
        statementDependencies.clear();
        sourceDependencies.clear();
        emptyDeclarations.clear();
    }

    public void process() {
        reset();

        buildDeclarationMap();
        buildDependencies();
        buildEmptyDeclarations();
    }

    private void buildEmptyDeclarations() {
        for (TypescriptSource source : sourceBundle.getSources()) {
            for (TypescriptStatement statement : source.getStatements()) {
                if (statement instanceof TypescriptDeclaration) {
                    final TypescriptDeclaration declaration = (TypescriptDeclaration) statement;
                    if (declaration.isEmpty()) {
                        emptyDeclarations.add(declaration);
                    }
                }
            }
        }
    }

    private void buildDependencies() {
        for (TypescriptSource source : sourceBundle.getSources()) {
            for (TypescriptStatement statement : source.getStatements()) {
                statement.accept(renderable -> {
                    if (renderable instanceof TypescriptType) {
                        final TypescriptType type = (TypescriptType)renderable;

                        final TypescriptDeclaration declaration = findDeclaration(type);
                        if (declaration != null) {
                            // Register the statement -> declaration dependency
                            if (!statementDependencies.containsKey(statement)) {
                                statementDependencies.put(statement, new HashSet<>());
                            }

                            statementDependencies.get(statement).add(declaration);

                            // Register the source -> declaration dependency
                            if (declaration.getSource() != source) {
                                if (!sourceDependencies.containsKey(source)) {
                                    sourceDependencies.put(source, new HashSet<>());
                                }

                                sourceDependencies.get(source).add(declaration);
                            }
                        }
                    }
                });
            }
        }
    }

    private void buildDeclarationMap() {
        for (TypescriptSource source : sourceBundle.getSources()) {
            for (TypescriptStatement statement : source.getStatements()) {
                if (statement instanceof TypescriptDeclaration) {
                    final TypescriptDeclaration declaration = (TypescriptDeclaration) statement;
                    final String identifier = declaration.getIdentifier();

                    if (declarationMap.containsKey(identifier)) {
                        throw new IllegalStateException("Duplicate identifier found: " + identifier);
                    }

                    declarationMap.put(identifier, declaration);
                }
            }
        }
    }

    /**
     * Returns the set of (external) declarations to which the source depends on.
     * @param source the source to get the external dependencies
     * @return the external dependencies of the source.
     */
    public Set<TypescriptDeclaration> getSourceDependencies(final TypescriptSource source) {
        return sourceDependencies.getOrDefault(source, new HashSet<>());
    }

    /**
     * Returns the declaration matching the specified type, or null if it does not exist.
     * Note: arrays always return null.
     *
     * @param type the type to search for declaration
     * @return the declaration corresponding to the type.
     */
    public TypescriptDeclaration findDeclaration(final TypescriptType type) {
        String identifier = null;

        if (type instanceof TypescriptEnumType) {
            identifier = ((TypescriptEnumType)type).getIdentifier();
        }

        if (type instanceof TypescriptInterfaceType) {
            identifier = ((TypescriptInterfaceType)type).getIdentifier();
        }

        return identifier != null ? declarationMap.get(identifier) : null;
    }

    public TypescriptSourceBundle getSourceBundle() {
        return sourceBundle;
    }

    public List<TypescriptDeclaration> getEmptyDeclarations() {
        return emptyDeclarations;
    }
}
