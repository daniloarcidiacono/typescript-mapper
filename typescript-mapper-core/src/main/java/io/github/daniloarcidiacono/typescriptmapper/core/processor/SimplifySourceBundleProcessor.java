package io.github.daniloarcidiacono.typescriptmapper.core.processor;

import io.github.daniloarcidiacono.typescriptmapper.analyzer.TypescriptAnalyzer;
import io.github.daniloarcidiacono.typescriptmapper.core.TypescriptSourceBundle;
import io.github.daniloarcidiacono.typescript.template.TypescriptRenderable;
import io.github.daniloarcidiacono.typescript.template.TypescriptSource;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptField;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptInterface;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptTypeParameter;
import io.github.daniloarcidiacono.typescript.template.statement.TypescriptStatement;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptInterfaceType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimplifySourceBundleProcessor implements SourceBundleProcessor {
    private static final Logger logger = LoggerFactory.getLogger(SimplifySourceBundleProcessor.class);
    private TypescriptAnalyzer analyzer;

    @Override
    public void process(final TypescriptSourceBundle bundle) {
        analyzer = new TypescriptAnalyzer(bundle);

        boolean modified;
        do {
            analyzer.process();

            final Set<TypescriptDeclaration> emptyDeclarations = new HashSet<>(analyzer.getEmptyDeclarations());

            modified = false;
            if (!emptyDeclarations.isEmpty()) {
                for (TypescriptSource source : bundle.getSources()) {
                    for (TypescriptStatement statement : source.getStatements()) {
                        if (statement instanceof TypescriptInterface) {
                            final TypescriptInterface iface = (TypescriptInterface) statement;

                            // Fields
                            final List<TypescriptField> fieldsToRemove = new ArrayList<>();
                            for (TypescriptField field : iface.getFields()) {
                                if (referencesAnyType(field, emptyDeclarations)) {
                                    fieldsToRemove.add(field);
                                }
                            }

                            for (TypescriptField fieldToRemove : fieldsToRemove) {
                                modified = true;
                                iface.getFields().remove(fieldToRemove);
                            }

                            // Type parameter
                            final List<TypescriptTypeParameter> typeParametersToRemove = new ArrayList<>();
                            for (TypescriptTypeParameter typeParameter : iface.getTypeParameters().getParameters()) {
                                if (referencesAnyType(typeParameter, emptyDeclarations)) {
                                    typeParametersToRemove.add(typeParameter);
                                }
                            }

                            for (TypescriptTypeParameter typeParameterToRemove : typeParametersToRemove) {
                                modified = true;
                                iface.getTypeParameters().getParameters().remove(typeParameterToRemove);
                            }


                            // Extends
                            final List<TypescriptType> extendedTypesToRemove = new ArrayList<>();
                            for (TypescriptType extendedType : iface.getInheritance().getExtended()) {
                                if (referencesAnyType(extendedType, emptyDeclarations)) {
                                    extendedTypesToRemove.add(extendedType);
                                }
                            }

                            for (TypescriptType extendedTypeToRemove : extendedTypesToRemove) {
                                modified = true;
                                iface.getInheritance().getExtended().remove(extendedTypeToRemove);
                            }
                        }
                    }
                }
            }

            // Remove the empty declarations
            for (TypescriptDeclaration emptyDeclaration : emptyDeclarations) {
                logger.debug("Removing {}", emptyDeclaration.getIdentifier());
                modified = true;
                emptyDeclaration.getSource().getStatements().remove(emptyDeclaration);
            }
        } while (modified);

        // Remove empty sources
        final List<TypescriptSource> sourcesToRemove = new ArrayList<>();
        for (TypescriptSource source : bundle.getSources()) {
            if (source.getStatements().isEmpty()) {
                sourcesToRemove.add(source);
            }
        }

        bundle.getSources().removeAll(sourcesToRemove);
    }


    private boolean referencesAnyType(final TypescriptRenderable referenced, final Set<? extends TypescriptDeclaration> references) {
        final boolean[] result = {
            false
        };

        final Set<String> referenceNames = references.stream().map(TypescriptDeclaration::getIdentifier).collect(Collectors.toSet());
        referenced.accept(renderable -> {
            if (renderable instanceof TypescriptInterfaceType) {
                final String identifier = ((TypescriptInterfaceType) renderable).getIdentifier();
                if (referenceNames.contains(identifier)) {
                    result[0] = true;
                }
            }
        });

        return result[0];
    }
}
