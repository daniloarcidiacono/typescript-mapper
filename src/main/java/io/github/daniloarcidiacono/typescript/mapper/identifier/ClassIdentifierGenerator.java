package io.github.daniloarcidiacono.typescript.mapper.identifier;

import io.github.daniloarcidiacono.commons.lang.StringCommons;

public class ClassIdentifierGenerator implements IdentifierGenerator {
    private String identifierSuffix = "DTO";

    public ClassIdentifierGenerator() {
    }

    public ClassIdentifierGenerator(String identifierSuffix) {
        this.identifierSuffix = identifierSuffix;
    }

    @Override
    public void clear() {
    }

    @Override
    public String generate(final Object object) {
        final Class<?> clazz = (Class<?>)object;


        final String prefix = generateEnclosingClass(clazz);
        if (!clazz.getSimpleName().endsWith(identifierSuffix)) {
            return prefix + StringCommons.capitalize(clazz.getSimpleName() + identifierSuffix);
        }
        return prefix + StringCommons.capitalize(clazz.getSimpleName());
    }

    private String generateEnclosingClass(final Class<?> clazz) {
        final StringBuilder result = new StringBuilder();
        Class<?> current = clazz;
        while ((current = current.getEnclosingClass()) != null) {
            result.append(StringCommons.capitalize(current.getSimpleName()));
        }

        return result.toString();
    }

    @Override
    public boolean supports(final Object object) {
        return object instanceof Class<?>;
    }

    public String getIdentifierSuffix() {
        return identifierSuffix;
    }

    public ClassIdentifierGenerator setIdentifierSuffix(final String identifierSuffix) {
        this.identifierSuffix = identifierSuffix;
        return this;
    }
}
