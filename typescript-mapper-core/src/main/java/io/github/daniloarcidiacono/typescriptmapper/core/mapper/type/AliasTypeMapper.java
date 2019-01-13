package io.github.daniloarcidiacono.typescriptmapper.core.mapper.type;

import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class AliasTypeMapper implements TypeMapper {
    private Map<Type, TypescriptType> aliases = new HashMap<>();

    public AliasTypeMapper() {
    }

    public AliasTypeMapper alias(final Type type, final TypescriptType alias) {
        aliases.put(type, alias);
        return this;
    }

    @Override
    public TypescriptType map(final Type type) {
        if (!supports(type)) {
            return null;
        }

        return aliases.get(type);
    }

    private boolean supports(final Type type) {
        return aliases.containsKey(type);
    }
}
