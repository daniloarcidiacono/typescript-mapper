package io.github.daniloarcidiacono.typescript.mapper.registry;

/**
 * Generates an identifier for a given object.
 * @author Danilo Arcidiacono
 */
public interface IdentifierGenerator {
    /**
     * Resets the internal state of the generator.
     */
    void reset();

    /**
     * Generates an identifier for the specified object.
     * @param object the object to generate an identifier for
     * @return the identifier of the object, or null if this
     * class does not support it.
     */
    String generateIdentifier(final Object object);
}
