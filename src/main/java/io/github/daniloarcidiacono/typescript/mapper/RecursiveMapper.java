package io.github.daniloarcidiacono.typescript.mapper;

import io.github.daniloarcidiacono.typescript.mapper.finder.CompositeClassFinder;
import io.github.daniloarcidiacono.typescript.mapper.mapper.declaration.ClassMapper;
import io.github.daniloarcidiacono.typescript.mapper.mapper.declaration.CompositeDeclarationMapper;
import io.github.daniloarcidiacono.typescript.mapper.mapper.declaration.EnumMapper;
import io.github.daniloarcidiacono.typescript.mapper.mapper.field.CompositeFieldMapper;
import io.github.daniloarcidiacono.typescript.mapper.mapper.injector.CompositeClassFieldInjector;
import io.github.daniloarcidiacono.typescript.mapper.mapper.type.*;
import io.github.daniloarcidiacono.typescript.mapper.matcher.CompositeClassMatcher;
import io.github.daniloarcidiacono.typescript.mapper.registry.TypescriptIdentifierRegistry;
import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * Main class for mapping.
 * @author Danilo Arcidiacono
 */
public class RecursiveMapper implements TypeMapper {
    private static final Logger logger = LoggerFactory.getLogger(RecursiveMapper.class);

    // Id registry
    private final TypescriptIdentifierRegistry idRegistry = new TypescriptIdentifierRegistry();

    // Class finder
    private final CompositeClassFinder finder = new CompositeClassFinder();

    // Class filters
    private final CompositeClassMatcher classFilter = new CompositeClassMatcher();

    // Type mapper
    private final AliasTypeMapper aliasTypeMapper = new AliasTypeMapper();
    private final PrimitiveTypeMapper primitiveTypeMapper = new PrimitiveTypeMapper();
    private final CollectionTypeMapper collectionTypeMapper = new CollectionTypeMapper(this);
    private final CompositeTypeMapper standardTypeMapper = new CompositeTypeMapper();
    private final CompositeTypeMapper typeMapper = new CompositeTypeMapper();

    // Field mapper
    private final CompositeFieldMapper fieldMapper = new CompositeFieldMapper();

    // Class field injector
    private final CompositeClassFieldInjector classFieldInjector = new CompositeClassFieldInjector();

    // Declaration mapper
    private final EnumMapper enumMapper;
    private final ClassMapper classMapper;
    private final CompositeDeclarationMapper declarationMapper = new CompositeDeclarationMapper();

    // Working sets
    private final Set<Class<?>> classesToParse = new HashSet<>();
    private final Set<Class<?>> parsedClasses = new HashSet<>();

    /**
     * Initializes the mapper with no configuration.
     */
    public RecursiveMapper() {
        standardTypeMapper.add(aliasTypeMapper, primitiveTypeMapper, collectionTypeMapper);

        // Add the standard type mappers
        typeMapper.add(aliasTypeMapper, primitiveTypeMapper, collectionTypeMapper);

        // Add the standard declaration mappers
        enumMapper = new EnumMapper(idRegistry);
        classMapper = new ClassMapper(idRegistry, classFieldInjector, this);
        declarationMapper.add(enumMapper, classMapper);

    }

    public void clear() {
        parsedClasses.clear();
        classesToParse.clear();
        idRegistry.reset();
    }

    public void map(final RecursiveMapperVisitor visitor) {
        clear();
        classesToParse.addAll(finder.find());
        logger.info("Found {} classes", classesToParse.size());

        while (!classesToParse.isEmpty()) {
            final Class<?> clazz = classesToParse.iterator().next();
            parsedClasses.add(clazz);
            classesToParse.remove(clazz);

            final TypescriptDeclaration declaration = declarationMapper.map(clazz);
            visitor.visit(clazz, declaration);
        }
    }

    public void addClass(final Class<?> clazz) {
        if (classFilter.matches(clazz)) {
            logger.warn("Class " + clazz.getName() + " ignored");
            return;
        }

        for (Class<?> parsedClass : parsedClasses) {
            if (parsedClass.equals(clazz)) {
                return;
            }
        }

        logger.debug("Adding class {}", clazz.getCanonicalName());
        classesToParse.add(clazz);
    }

    @Override
    public TypescriptType map(final Type type) {
        // Before checking for filtered types, apply some standard mappers
        TypescriptType result = standardTypeMapper.map(type);

        // If we don't have a standard mapping
        if (result == null) {
            // Check that we are not attempting to map a filtered type
            if (type instanceof Class) {
                final Class<?> clazz = (Class<?>)type;
                if (classFilter.matches(clazz)) {
                    return null;
                }
            }


            // Otherwise, invoke the rest of the mappers
            result = typeMapper.map(type);
        }

        // Failure
        if (result == null) {
            return null;
        }

        // Mapped with success
        if (type instanceof Class) {
            final Class<?> clazz = (Class<?>)type;
            if (!clazz.isPrimitive() && !clazz.isArray()) {
                addClass((Class<?>) type);
            }
        }

        return result;
    }

    public TypescriptIdentifierRegistry getIdRegistry() {
        return idRegistry;
    }

    public CompositeClassFinder getFinder() {
        return finder;
    }

    public CompositeClassMatcher getClassFilter() {
        return classFilter;
    }

    public AliasTypeMapper getAliasTypeMapper() {
        return aliasTypeMapper;
    }

    public CompositeTypeMapper getTypeMapper() {
        return typeMapper;
    }

    public CompositeFieldMapper getFieldMapper() {
        return fieldMapper;
    }

    public CompositeClassFieldInjector getClassFieldInjector() {
        return classFieldInjector;
    }

    public CompositeDeclarationMapper getDeclarationMapper() {
        return declarationMapper;
    }
}