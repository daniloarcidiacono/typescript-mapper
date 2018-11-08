package io.github.daniloarcidiacono.typescript.mapper.registry;

import java.util.HashMap;
import java.util.Map;

/**
 * Identifier registry for Java objects.
 * Delegates the generation logic to a list of {@link IdentifierGenerator}.
 * @author Danilo Arcidiacono
 */
public class TypescriptIdentifierRegistry {
    /**
     * Identifier generator
     */
    private final CompositeIdentifierGenerator generator = new CompositeIdentifierGenerator();

    /**
     * Registered identifiers
     */
    private final Map<Object, String> identifiers = new HashMap<>();

    /**
     * Initializes the registry with no configuration.
     */
    public TypescriptIdentifierRegistry() {
    }

    /**
     * Resets the internal state of the registry.
     */
    public void reset() {
        identifiers.clear();
        generator.reset();
    }

    /**
     * Generates an identifier for the given object.
     * @param object the object
     * @return the identifier for the object
     */
    public String registerIdentifier(final Object object) {
        if (!identifiers.containsKey(object)) {
            final String objectId = generator.generateIdentifier(object);
            if (objectId == null) {
                throw new IllegalStateException("Object " + object + " is not supported by any id generator!");
            }

            identifiers.put(object, objectId);
        }

        return identifiers.get(object);
    }

    public CompositeIdentifierGenerator getGenerator() {
        return generator;
    }
}
