package io.github.daniloarcidiacono.typescript.mapper.mapper.type;

import io.github.daniloarcidiacono.typescript.mapper.registry.TypescriptIdentifierRegistry;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptAnyType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptInterfaceType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;

public class ClassTypeMapper implements TypeMapper {
    private TypescriptIdentifierRegistry idRegistry;

    public ClassTypeMapper(TypescriptIdentifierRegistry idRegistry) {
        this.idRegistry = idRegistry;
    }

    @Override
    public TypescriptType map(final Type type) {
        if (!supports(type)) {
            return null;
        }

        final Class<?> clazz = (Class<?>)type;

        // @TODO
        // If the type is a class, then it must be mapped as well
//        typeMapper.addClass(clazz);

        // Determine the class identifier
        final String identifier = idRegistry.registerIdentifier(clazz);
        final TypescriptInterfaceType result = new TypescriptInterfaceType(identifier);

        // Add the type parameters
        //
        // This handles parametrized classes without arguments, for example:
        //      Map map
        //
        // becomes
        //      Map<Object, Object>
        //
        for (Type typeParameter : clazz.getTypeParameters()) {
            result.argument(TypescriptAnyType.INSTANCE);
        }

        return result;
    }


    private boolean supports(Type type) {
        return type instanceof Class;
    }
}
