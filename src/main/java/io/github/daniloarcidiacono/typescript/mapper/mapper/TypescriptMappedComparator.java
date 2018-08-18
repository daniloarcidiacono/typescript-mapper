package io.github.daniloarcidiacono.typescript.mapper.mapper;

import java.util.Comparator;

/**
 * Comparator used to sort declarations in renderers.
 * @author Danilo Arcidiacono
 */
public class TypescriptMappedComparator implements Comparator<TypescriptMapped> {
    @Override
    public int compare(final TypescriptMapped o1, final TypescriptMapped o2) {
        // Check if o1 and o2 are in the same type hierarchy
        if (o1.getJavaClass().isAssignableFrom(o2.getJavaClass())) {
            return -1;
        }

        if (o2.getJavaClass().isAssignableFrom(o1.getJavaClass())) {
            return 1;
        }

        // o1 and o2 are not related
        final int h1 = hierarchyLevel(o1.getJavaClass());
        final int h2 = hierarchyLevel(o2.getJavaClass());

        // Check if either one are part of an hierarchy
        if (h1 != h2) {
            return Integer.compare(h1, h2);
        }

        // Order by name
        return o1.getDeclaration().getIdentifier().compareTo(o2.getDeclaration().getIdentifier());
    }

    private int hierarchyLevel(final Class<?> clazz) {
        Class<?> current = clazz;
        int level = 0;
        while ((current = current.getSuperclass()) != null) {
            level++;
        }

        return level;
    }
}