package io.github.daniloarcidiacono.typescriptmapper.core.registry;

import io.github.daniloarcidiacono.commons.lang.patterns.Composite;

/**
 * Queries a list of {@link IdentifierGenerator} until one returns a valid identifier.
 */
public class CompositeIdentifierGenerator extends Composite<IdentifierGenerator> implements IdentifierGenerator {
    public CompositeIdentifierGenerator() {
    }

    @Override
    public void reset() {
        for (IdentifierGenerator generator : components) {
            generator.reset();
        }
    }

    @Override
    public String generateIdentifier(final Object object) {
        for (IdentifierGenerator generator : components) {
            final String identifier = generator.generateIdentifier(object);
            if (identifier != null) {
                return identifier;
            }
        }

        return null;
    }
}
