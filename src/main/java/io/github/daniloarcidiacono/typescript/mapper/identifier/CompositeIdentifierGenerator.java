package io.github.daniloarcidiacono.typescript.mapper.identifier;

import java.util.ArrayList;
import java.util.List;

public class CompositeIdentifierGenerator implements IdentifierGenerator {
    private List<IdentifierGenerator> generators = new ArrayList<>();

    public CompositeIdentifierGenerator() {
    }

    public CompositeIdentifierGenerator generator(final IdentifierGenerator ...generators) {
        for (IdentifierGenerator generator : generators) {
            generator(generator);
        }

        return this;
    }

    public CompositeIdentifierGenerator generator(final IdentifierGenerator generator) {
        generators.add(generator);
        return this;
    }

    @Override
    public void clear() {
        for (IdentifierGenerator generator : generators) {
            generator.clear();
        }
    }

    @Override
    public String generate(final Object object) {
        for (IdentifierGenerator generator : generators) {
            if (generator.supports(object)) {
                return generator.generate(object);
            }
        }

        return null;
    }

    @Override
    public boolean supports(final Object object) {
        for (IdentifierGenerator generator : generators) {
            if (generator.supports(object)) {
                return true;
            }
        }

        return false;
    }

    public List<IdentifierGenerator> getGenerators() {
        return generators;
    }

    public void setGenerators(List<IdentifierGenerator> generators) {
        this.generators = generators;
    }
}
