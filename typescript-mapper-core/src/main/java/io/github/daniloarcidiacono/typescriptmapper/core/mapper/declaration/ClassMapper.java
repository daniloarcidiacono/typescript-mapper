package io.github.daniloarcidiacono.typescriptmapper.core.mapper.declaration;

import io.github.daniloarcidiacono.commons.lang.ReflectiveCommons;
import io.github.daniloarcidiacono.typescriptmapper.core.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescriptmapper.core.mapper.injector.ClassFieldInjector;
import io.github.daniloarcidiacono.typescriptmapper.core.mapper.type.TypeMapper;
import io.github.daniloarcidiacono.typescriptmapper.core.registry.TypescriptIdentifierRegistry;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptInterface;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptTypeParameters;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptAnyType;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassMapper implements TypescriptDeclarationMapper {
    private TypescriptIdentifierRegistry idRegistry;
    private ClassFieldInjector classFieldInjector;
    private TypeMapper typeMapper;

    public ClassMapper(TypescriptIdentifierRegistry idRegistry, ClassFieldInjector classFieldInjector, TypeMapper typeMapper) {
        this.idRegistry = idRegistry;
        this.classFieldInjector = classFieldInjector;
        this.typeMapper = typeMapper;
    }

    @Override
    public TypescriptInterface map(final Type type) {
        final Class<?> clazz = (Class<?>)type;
        return parseObject(clazz);
    }

    private TypescriptTypeParameters parseTypeParams(final Class<?> clazz) {
        final List<Class<?>> enclosing = ReflectiveCommons.getEnclosingClasses(clazz);
        final List<TypescriptTypeParameters> typeParams = new ArrayList<>();

        // Traverse the enclosing classes beginning from the most nested one
        int index = enclosing.size() - 1;
        while (index >= 0) {
            final Class<?> current = enclosing.get(index);
            final TypescriptTypeParameters currentParams = new TypescriptTypeParameters();

            // Build the type arguments for current
            for (TypeVariable<? extends Class<?>> classTypeVariable : current.getTypeParameters()) {
                final Type[] bounds = classTypeVariable.getBounds();
                if (bounds.length == 1 && !(bounds[0].equals(Object.class))) {
                    final TypescriptType argumentType = typeMapper.map(bounds[0]);
                    currentParams.parameter(classTypeVariable.getName(), argumentType != null ? argumentType : TypescriptAnyType.INSTANCE);
                } else {
                    currentParams.parameter(classTypeVariable.getName());
                }
            }

            typeParams.add(currentParams);
            index--;

            // Static contexts do not inherit type parameters
            if ((current.getModifiers() & Modifier.STATIC) != 0) {
                break;
            }
        }

        // Reverse the order of the type parameters (begin from the least nested)
        Collections.reverse(typeParams);

        // Build the parameters in correct order (enclosing first)
        final TypescriptTypeParameters result = new TypescriptTypeParameters();
        for (TypescriptTypeParameters typeParam : typeParams) {
            result.getParameters().addAll(typeParam.getParameters());
        }

        return result;
    }

    private TypescriptInterface parseObject(final Class<?> clazz) {
        final String identifier = idRegistry.registerIdentifier(clazz);

        // Create the declaration for the current class
        final TypescriptInterface declaration = new TypescriptInterface(identifier);

        // Handle type parameters
        final TypescriptTypeParameters typescriptTypeParameters = parseTypeParams(clazz);
        declaration.setTypeParameters(typescriptTypeParameters);

        // Inheritance (if injector is an interface, getSuperClass() is null)
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Object.class)) {
            final TypescriptType superType = typeMapper.map(clazz.getSuperclass());
            if (superType != null) {
                declaration.extend(superType);
            }
        }

        final Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> superInterface : interfaces) {
            final TypescriptType superType = typeMapper.map(superInterface);
            if (superType != null) {
                declaration.extend(superType);
            }
        }

        // Explore subclasses
        // @TODO: Reflections injection
//        addClass(reflections.getSubTypesOf(injector));

        // Inject the documentation
        final TypescriptComments docAnnotation = clazz.getAnnotation(TypescriptComments.class);
        if (docAnnotation != null) {
            declaration.getComments().comment(docAnnotation.value());
        }

        // @TODO
        // Mark the class as parsed before calling mapType
        // This avoids infinite recursion when a class has a field of the same type (e.g. a parent pointer)
//        mapped.add(new TypescriptMapped(clazz, declaration));

        // This does not get inherited fields
        classFieldInjector.injectFields(declaration, clazz);
        return declaration;
    }

    @Override
    public boolean supports(Type type) {
        return (type instanceof Class) && (
                ((Class<?>)type).isInterface() ||
                ((Class<?>)type).isAnonymousClass() ||
                ((Class<?>)type).isLocalClass() ||
                ((Class<?>)type).isMemberClass() ||
                !((Class) type).isEnum()
        );
    }

    public TypescriptIdentifierRegistry getIdRegistry() {
        return idRegistry;
    }

    public void setIdRegistry(TypescriptIdentifierRegistry idRegistry) {
        this.idRegistry = idRegistry;
    }

    public ClassFieldInjector getClassFieldInjector() {
        return classFieldInjector;
    }

    public void setClassFieldInjector(ClassFieldInjector classFieldInjector) {
        this.classFieldInjector = classFieldInjector;
    }

    public TypeMapper getTypeMapper() {
        return typeMapper;
    }

    public void setTypeMapper(TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }
}
