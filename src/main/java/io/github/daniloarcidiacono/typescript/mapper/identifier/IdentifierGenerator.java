package io.github.daniloarcidiacono.typescript.mapper.identifier;

public interface IdentifierGenerator {
    void clear();
    String generate(final Object object);
    boolean supports(final Object object);
}
