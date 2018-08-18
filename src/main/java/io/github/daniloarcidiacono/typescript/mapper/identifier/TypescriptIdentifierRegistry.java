package io.github.daniloarcidiacono.typescript.mapper.identifier;

import java.util.HashMap;
import java.util.Map;

public class TypescriptIdentifierRegistry {
    private AnnotationIdentifierGenerator annotationIdGenerator = new AnnotationIdentifierGenerator();
    private ClassIdentifierGenerator classIdGenerator = new ClassIdentifierGenerator();
    private CompositeIdentifierGenerator generator = new CompositeIdentifierGenerator()
            .generator(annotationIdGenerator)
            .generator(classIdGenerator);
    private Map<Object, String> identifiers = new HashMap<>();

    public void clear() {
        identifiers.clear();
        generator.clear();
    }

    public String registerIdentifier(final Object object) {
        if (!identifiers.containsKey(object)) {
            if (!generator.supports(object)) {
                throw new IllegalStateException("Object " + object + " is not supported by any id generator!");
            }

            final String objectId = generator.generate(object);
            identifiers.put(object, objectId);
        }

        return identifiers.get(object);
    }

    public Map<Object, String> getIdentifiers() {
        return identifiers;
    }

    public ClassIdentifierGenerator getClassIdGenerator() {
        return classIdGenerator;
    }

    public CompositeIdentifierGenerator getGenerator() {
        return generator;
    }
}
