package io.github.daniloarcidiacono.typescript.mapper.mapper;

import io.github.daniloarcidiacono.commons.lang.StringCommons;
import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptComments;
import io.github.daniloarcidiacono.typescript.mapper.identifier.TypescriptIdentifierRegistry;
import io.github.daniloarcidiacono.typescript.mapper.mapper.field.CompositeFieldMapper;
import io.github.daniloarcidiacono.typescript.mapper.mapper.field.JacksonFieldMapper;
import io.github.daniloarcidiacono.typescript.mapper.mapper.field.StandardFieldMapper;
import io.github.daniloarcidiacono.typescript.mapper.mapper.injector.CompositeClassFieldInjector;
import io.github.daniloarcidiacono.typescript.mapper.mapper.injector.JacksonClassFieldInjector;
import io.github.daniloarcidiacono.typescript.mapper.mapper.injector.StandardClassFieldInjector;
import io.github.daniloarcidiacono.typescript.mapper.mapper.type.*;
import io.github.daniloarcidiacono.typescript.mapper.scanner.TypescriptCompositePackageScanner;
import io.github.daniloarcidiacono.typescript.mapper.scanner.TypescriptPackageScanner;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptEnum;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptInterface;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptTypeParameters;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Main class for mapping.
 * @author Danilo Arcidiacono
 */
public class TypescriptMapper {
    // Package scanner
    private TypescriptPackageScanner packageScanner = new TypescriptPackageScanner();
    private TypescriptCompositePackageScanner scanner = new TypescriptCompositePackageScanner().scanner(packageScanner);

    // Id registry
    private TypescriptIdentifierRegistry idRegistry = new TypescriptIdentifierRegistry();

    // Type mapper
    private CollectionTypeMapper collectionTypeMapper = new CollectionTypeMapper(this);
    private ParametrizedTypeMapper parametrizedTypeMapper = new ParametrizedTypeMapper(this);
    private GenericTypeMapper genericTypeMapper = new GenericTypeMapper();
    private PrimitiveTypeMapper primitiveTypeMapper = new PrimitiveTypeMapper();
    private ArrayTypeMapper arrayTypeMapper = new ArrayTypeMapper(this);
    private EnumTypeMapper enumTypeMapper = new EnumTypeMapper(this);
    private ClassTypeMapper classTypeMapper = new ClassTypeMapper(this);
    private CompositeTypeMapper typeMapper = new CompositeTypeMapper()
        .mapper(collectionTypeMapper)
        .mapper(parametrizedTypeMapper)
        .mapper(genericTypeMapper)
        .mapper(primitiveTypeMapper)
        .mapper(arrayTypeMapper)
        .mapper(enumTypeMapper)
        .mapper(classTypeMapper);

    // Field mapper
    private JacksonFieldMapper jacksonFieldMapper = new JacksonFieldMapper();
    private StandardFieldMapper standardFieldMapper = new StandardFieldMapper(this);
    private CompositeFieldMapper fieldMapper = new CompositeFieldMapper().mapper(jacksonFieldMapper).mapper(standardFieldMapper);

    // Class field injector
    private JacksonClassFieldInjector jacksonClassFieldInjector = new JacksonClassFieldInjector(fieldMapper);
    private StandardClassFieldInjector standardClassFieldInjector = new StandardClassFieldInjector(fieldMapper);
    private CompositeClassFieldInjector classFieldInjector = new CompositeClassFieldInjector().injector(jacksonClassFieldInjector).injector(standardClassFieldInjector);

    // Working set
    private List<TypescriptMapped> mapped = new ArrayList<>();
    private Set<Class<?>> classesToParse = new HashSet<>();

    public TypescriptMapper() {
    }

    public TypescriptMapper scanPackage(final String packageName) {
        packageScanner.registerPackage(packageName);
        return this;
    }

    public TypescriptMapper scanPackage(final Package pack) {
        packageScanner.registerPackage(pack);
        return this;
    }

    public TypescriptMapper scanPackage(final Class<?> clazz) {
        packageScanner.registerPackage(clazz.getPackage());
        return this;
    }

    public void clear() {
        mapped.clear();
        classesToParse.clear();
        idRegistry.clear();
    }

    public List<TypescriptMapped> map() {
        classesToParse.clear();
        classesToParse.addAll(scanner.scan());

        while (!classesToParse.isEmpty()) {
            final Class<?> clazz = classesToParse.iterator().next();
            parseClass(clazz);
            classesToParse.remove(clazz);
        }

        return mapped;
    }

    public void addClass(final Class<?> clazz) {
        for (TypescriptMapped map : mapped) {
            if (map.getJavaClass().equals(clazz)) {
                return;
            }
        }

        classesToParse.add(clazz);
    }

    private void addClass(final Class<?> ...clazz) {
        for (Class<?> current : clazz) {
            addClass(current);
        }
    }

    private void parseClass(final Class<?> clazz) {
        if (clazz.isEnum()) {
            parseEnum(clazz);
        } else {
            parseObject(clazz);
        }
    }

    private TypescriptEnum parseEnum(final Class<?> clazz) {
        final TypescriptComments docAnnotation = (TypescriptComments)clazz.getAnnotation(TypescriptComments.class);
        final String identifier = idRegistry.registerIdentifier(clazz);
        final TypescriptEnum declaration = new TypescriptEnum(identifier);
        if (docAnnotation != null) {
            declaration.getComments().comment(docAnnotation.value());
        }

        // @TODO: Handle this better
        int i = 0;
        final Object[] enumValues = clazz.getEnumConstants();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isEnumConstant()) {
                declaration.entry(field.getName(), StringCommons.singleQuote(enumValues[i].toString()));
                i++;
            }
        }

        mapped.add(new TypescriptMapped(clazz, declaration));
        return declaration;
    }

    private TypescriptTypeParameters parseTypeParams(final Class<?> clazz) {
        final TypescriptTypeParameters result = new TypescriptTypeParameters();
        for (TypeVariable<? extends Class<?>> classTypeVariable : clazz.getTypeParameters()) {
            final Type[] bounds = classTypeVariable.getBounds();
            if (bounds.length == 1 && !(bounds[0].equals(Object.class))) {
                result.parameter(classTypeVariable.getName(), mapType(bounds[0]));
            } else {
                result.parameter(classTypeVariable.getName());
            }
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
            declaration.extend(mapType(clazz.getSuperclass()));
        }

        final Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> superInterface : interfaces) {
            declaration.extend(mapType(superInterface));
            addClass(superInterface);
        }

        // Explore subclasses
        // @TODO: Reflections injection
//        addClass(reflections.getSubTypesOf(injector));

        // Inject the documentation
        final TypescriptComments docAnnotation = clazz.getAnnotation(TypescriptComments.class);
        if (docAnnotation != null) {
            declaration.getComments().comment(docAnnotation.value());
        }

        // Mark the class as parsed before calling mapType
        // This avoids infinite recursion when a class has a field of the same type (e.g. a parent pointer)
        mapped.add(new TypescriptMapped(clazz, declaration));

        // This does not get inherited fields
        classFieldInjector.injectFields(declaration, clazz);
        return declaration;
    }

    public TypescriptType mapType(final Type type) {
        return typeMapper.map(type);
    }

    public List<TypescriptMapped> getMapped() {
        return mapped;
    }

    public TypescriptCompositePackageScanner getScanner() {
        return scanner;
    }

    public TypescriptPackageScanner getPackageScanner() {
        return packageScanner;
    }

    public TypescriptIdentifierRegistry getIdRegistry() {
        return idRegistry;
    }

    public CompositeFieldMapper getFieldMapper() {
        return fieldMapper;
    }

    public void setFieldMapper(CompositeFieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
    }
}