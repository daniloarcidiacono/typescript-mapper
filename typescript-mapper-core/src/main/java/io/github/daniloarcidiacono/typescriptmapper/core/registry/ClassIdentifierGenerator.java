package io.github.daniloarcidiacono.typescriptmapper.core.registry;

import io.github.daniloarcidiacono.commons.lang.ReflectiveCommons;
import io.github.daniloarcidiacono.commons.lang.StringCommons;

import java.util.List;

/**
 * Generates an identifier for a Java class.
 * Nested classes are concatenated, and common prefixes are stripped.
 * Supports adding a suffix (default "DTO").
 */
public class ClassIdentifierGenerator implements IdentifierGenerator {
    private String identifierSuffix = "DTO";
    private String enclosingSeparator = "";

    public ClassIdentifierGenerator() {
    }

    @Override
    public void reset() {
    }

    @Override
    public String generateIdentifier(final Object object) {
        if (!supports(object)) {
            return null;
        }

        final Class<?> clazz = (Class<?>)object;
        final List<Class<?>> enclosingClasses = ReflectiveCommons.getEnclosingClasses(clazz);

        final StringBuilder sb = new StringBuilder();

        // For each enclosing class
        Class<?> enclosing = null;
        for (Class<?> current : enclosingClasses) {
            String name = current.getSimpleName();

            // If we have an enclosing class
            if (enclosing != null) {
                // Append the separator
                sb.append(enclosingSeparator);

                // Strip the common prefix, if any
                if (name.startsWith(enclosing.getSimpleName())) {
                    name = name.substring(enclosing.getSimpleName().length());
                }
            }

            // Append the chunk
            sb.append(StringCommons.capitalize(name));

            // Keep track of the enclosing class
            enclosing = current;
        }

        final String result = sb.toString();
        return !result.endsWith(identifierSuffix) ? result + identifierSuffix : result;
    }

    private boolean supports(final Object object) {
        return object instanceof Class<?>;
    }

    public String getIdentifierSuffix() {
        return identifierSuffix;
    }

    public ClassIdentifierGenerator setIdentifierSuffix(final String identifierSuffix) {
        this.identifierSuffix = identifierSuffix;
        return this;
    }

    public String getEnclosingSeparator() {
        return enclosingSeparator;
    }

    public ClassIdentifierGenerator setEnclosingSeparator(String enclosingSeparator) {
        this.enclosingSeparator = enclosingSeparator;
        return this;
    }
}
