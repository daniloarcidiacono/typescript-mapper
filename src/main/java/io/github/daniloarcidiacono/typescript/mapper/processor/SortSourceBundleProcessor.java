package io.github.daniloarcidiacono.typescript.mapper.processor;

import io.github.daniloarcidiacono.typescript.mapper.TypescriptSourceBundle;
import io.github.daniloarcidiacono.typescript.template.TypescriptSource;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

public class SortSourceBundleProcessor implements SourceBundleProcessor {
    @Override
    public void process(final TypescriptSourceBundle bundle) {
        for (TypescriptSource source : bundle.getSources()) {
            source.getStatements().sort((o1, o2) -> {
                if (!(o1 instanceof TypescriptDeclaration) || !(o2 instanceof TypescriptDeclaration)) {
                    return 0;
                }

                final TypescriptDeclaration o1Decl = (TypescriptDeclaration) o1;
                final TypescriptDeclaration o2Decl = (TypescriptDeclaration) o2;
                final Class<?> o1Class = bundle.getJavaClass(o1Decl);
                final Class<?> o2Class = bundle.getJavaClass(o2Decl);

                // Check if o1 and o2 are in the same type hierarchy
                if (o1Class.isAssignableFrom(o2Class)) {
                    return -1;
                }

                if (o2Class.isAssignableFrom(o1Class)) {
                    return 1;
                }

                // o1 and o2 are not related
                final int h1 = hierarchyLevel(o1Class);
                final int h2 = hierarchyLevel(o2Class);

                // Check if either one are part of an hierarchy
                if (h1 != h2) {
                    return Integer.compare(h1, h2);
                }

                // Order by name
                return o1Decl.getIdentifier().compareTo(o2Decl.getIdentifier());
            });
        }
    }

    private static int hierarchyLevel(final Class<?> clazz) {
        Class<?> current = clazz;
        int level = 0;
        while ((current = current.getSuperclass()) != null) {
            level++;
        }

        return level;
    }
}
