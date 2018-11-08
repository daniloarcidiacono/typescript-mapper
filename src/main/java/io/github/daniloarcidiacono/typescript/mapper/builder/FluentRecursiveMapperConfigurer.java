package io.github.daniloarcidiacono.typescript.mapper.builder;

import io.github.daniloarcidiacono.typescript.mapper.RecursiveMapper;
import io.github.daniloarcidiacono.typescript.mapper.finder.ReflectiveClassMatcherFinder;
import io.github.daniloarcidiacono.typescript.mapper.finder.TypescriptClassFinder;
import io.github.daniloarcidiacono.typescript.mapper.mapper.field.FieldMapper;
import io.github.daniloarcidiacono.typescript.mapper.mapper.field.JacksonFieldMapper;
import io.github.daniloarcidiacono.typescript.mapper.mapper.field.StandardFieldMapper;
import io.github.daniloarcidiacono.typescript.mapper.mapper.injector.ClassFieldInjector;
import io.github.daniloarcidiacono.typescript.mapper.mapper.injector.JacksonClassFieldInjector;
import io.github.daniloarcidiacono.typescript.mapper.mapper.injector.StandardClassFieldInjector;
import io.github.daniloarcidiacono.typescript.mapper.mapper.type.*;
import io.github.daniloarcidiacono.typescript.mapper.matcher.ClassMatcher;
import io.github.daniloarcidiacono.typescript.mapper.matcher.PackageClassMatcher;
import io.github.daniloarcidiacono.typescript.template.type.TypescriptType;

import java.lang.reflect.Type;

public class FluentRecursiveMapperConfigurer<T> {
    private final T context;
    private final RecursiveMapper mapper;

    // Configurer state
    private ReflectiveClassMatcherFinder reflectiveClassMatcherFinder;
    private WildcardTypeMapper wildcardTypeMapper;
    private ParametrizedTypeMapper parametrizedTypeMapper;
    private TypeVariableTypeMapper typeVariableTypeMapper;
    private GenericArrayTypeMapper genericArrayTypeMapper;
    private ArrayTypeMapper arrayTypeMapper;
    private EnumTypeMapper enumTypeMapper;
    private ClassTypeMapper classTypeMapper;
    private JacksonFieldMapper jacksonFieldMapper;
    private StandardFieldMapper standardFieldMapper;
    private JacksonClassFieldInjector jacksonClassFieldInjector;
    private StandardClassFieldInjector standardClassFieldInjector;
    private PackageClassMatcher javaClassMatcher;

    // Sub-configurers
    private FluentRegistryConfigurer<FluentRecursiveMapperConfigurer<T>> fluentRegistryConfigurer;
    private FluentReflectiveClassMatcherFinderConfigurer<FluentRecursiveMapperConfigurer<T>> fluentReflectiveClassMatcherFinderConfigurer;
    private FluentStandardClassFieldInjectorConfigurer<FluentRecursiveMapperConfigurer<T>> fluentStandardClassFieldInjectorConfigurer;

    public FluentRecursiveMapperConfigurer(final T context, final RecursiveMapper mapper) {
        this.context = context;
        this.mapper = mapper;
    }

    public FluentRecursiveMapperConfigurer<T> standard(final String rootPackage) {
        return identifierRegistry()
            .withStandardGenerators()
        .and()
        .scanClasses(true)
            .ofPackage(rootPackage)
            .withStandardMatchers()
        .and()
        .withStandardClassExclusions()
        .withStandardClassFieldInjectors()
        .withStandardFieldMappers()
        .withStandardTypeMappers();
    }

    public FluentRegistryConfigurer<FluentRecursiveMapperConfigurer<T>> identifierRegistry() {
        if (fluentRegistryConfigurer == null) {
            fluentRegistryConfigurer = new FluentRegistryConfigurer<>(this, mapper.getIdRegistry());
        }

        return fluentRegistryConfigurer;
    }

    public FluentReflectiveClassMatcherFinderConfigurer<FluentRecursiveMapperConfigurer<T>> scanClasses(final boolean scan) {
        if (scan) {
            if (reflectiveClassMatcherFinder == null) {
                reflectiveClassMatcherFinder = new ReflectiveClassMatcherFinder(null);
                mapper.getFinder().add(reflectiveClassMatcherFinder);
            }

            if (fluentReflectiveClassMatcherFinderConfigurer == null) {
                fluentReflectiveClassMatcherFinderConfigurer = new FluentReflectiveClassMatcherFinderConfigurer<>(this, reflectiveClassMatcherFinder);
            }
        } else {
            if (reflectiveClassMatcherFinder != null) {
                mapper.getFinder().remove(reflectiveClassMatcherFinder);
                reflectiveClassMatcherFinder = null;
            }

            if (fluentReflectiveClassMatcherFinderConfigurer != null) {
                fluentReflectiveClassMatcherFinderConfigurer = null;
            }
        }

        return fluentReflectiveClassMatcherFinderConfigurer;
    }

    public FluentRecursiveMapperConfigurer<T> withClassFinder(final TypescriptClassFinder finder) {
        mapper.getFinder().add(finder);
        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withoutClassFinder(final TypescriptClassFinder finder) {
        mapper.getFinder().remove(finder);
        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withStandardTypeMappers() {
        if (wildcardTypeMapper == null) {
            wildcardTypeMapper = new WildcardTypeMapper(mapper);
            withTypeMapper(wildcardTypeMapper);
        }

        if (parametrizedTypeMapper == null) {
            parametrizedTypeMapper = new ParametrizedTypeMapper(mapper);
            withTypeMapper(parametrizedTypeMapper);
        }

        if (typeVariableTypeMapper == null) {
            typeVariableTypeMapper = new TypeVariableTypeMapper();
            withTypeMapper(typeVariableTypeMapper);
        }

        if (genericArrayTypeMapper == null) {
            genericArrayTypeMapper = new GenericArrayTypeMapper(mapper);
            withTypeMapper(genericArrayTypeMapper);
        }

        if (arrayTypeMapper == null) {
            arrayTypeMapper = new ArrayTypeMapper(mapper);
            withTypeMapper(arrayTypeMapper);
        }

        if (enumTypeMapper == null) {
            enumTypeMapper = new EnumTypeMapper(mapper.getIdRegistry());
            withTypeMapper(enumTypeMapper);
        }

        if (classTypeMapper == null) {
            classTypeMapper = new ClassTypeMapper(mapper.getIdRegistry());
            withTypeMapper(classTypeMapper);
        }

        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withoutStandardTypeMappers() {
        if (wildcardTypeMapper != null) {
            withoutTypeMapper(wildcardTypeMapper);
            wildcardTypeMapper = null;
        }

        if (parametrizedTypeMapper != null) {
            withoutTypeMapper(parametrizedTypeMapper);
            parametrizedTypeMapper = null;
        }

        if (typeVariableTypeMapper != null) {
            withoutTypeMapper(typeVariableTypeMapper);
            typeVariableTypeMapper = null;
        }

        if (genericArrayTypeMapper != null) {
            withoutTypeMapper(genericArrayTypeMapper);
            genericArrayTypeMapper = null;
        }

        if (arrayTypeMapper != null) {
            withoutTypeMapper(arrayTypeMapper);
            arrayTypeMapper = null;
        }

        if (enumTypeMapper != null) {
            withoutTypeMapper(enumTypeMapper);
            enumTypeMapper = null;
        }

        if (classTypeMapper != null) {
            withoutTypeMapper(classTypeMapper);
            classTypeMapper = null;
        }

        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withTypeMapper(final TypeMapper typeMapper) {
        mapper.getTypeMapper().add(typeMapper);
        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withoutTypeMapper(final TypeMapper typeMapper) {
        mapper.getTypeMapper().remove(typeMapper);
        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withStandardFieldMappers() {
        if (jacksonFieldMapper == null) {
            jacksonFieldMapper = new JacksonFieldMapper();
            withFieldMapper(jacksonFieldMapper);
        }

        if (standardFieldMapper == null) {
            standardFieldMapper = new StandardFieldMapper(mapper);
            withFieldMapper(standardFieldMapper);
        }

        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withoutStandardFieldMappers() {
        if (jacksonFieldMapper != null) {
            withoutFieldMapper(jacksonFieldMapper);
            jacksonFieldMapper = null;
        }

        if (standardFieldMapper != null) {
            withoutFieldMapper(standardFieldMapper);
            standardFieldMapper = null;
        }

        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withFieldMapper(final FieldMapper fieldMapper) {
        mapper.getFieldMapper().add(fieldMapper);
        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withoutFieldMapper(final FieldMapper fieldMapper) {
        mapper.getFieldMapper().remove(fieldMapper);
        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withStandardClassFieldInjectors() {
        if (jacksonClassFieldInjector == null) {
            jacksonClassFieldInjector = new JacksonClassFieldInjector();
            withClassFieldInjector(jacksonClassFieldInjector);
        }

        return withStandardClassFieldInjector()
                .withStandardExclusions().and();
    }

    public FluentRecursiveMapperConfigurer<T> withoutStandardClassFieldInjectors() {
        if (jacksonClassFieldInjector != null) {
            withoutClassFieldInjector(jacksonClassFieldInjector);
            jacksonClassFieldInjector = null;
        }

        return withoutStandardClassFieldInjector();
    }

    public FluentStandardClassFieldInjectorConfigurer<FluentRecursiveMapperConfigurer<T>> withStandardClassFieldInjector() {
        if (standardClassFieldInjector == null) {
            standardClassFieldInjector = new StandardClassFieldInjector(mapper.getFieldMapper());
            withClassFieldInjector(standardClassFieldInjector);
        }

        if (fluentStandardClassFieldInjectorConfigurer == null) {
            fluentStandardClassFieldInjectorConfigurer = new FluentStandardClassFieldInjectorConfigurer<>(this, standardClassFieldInjector);
        }

        return fluentStandardClassFieldInjectorConfigurer;
    }

    public FluentRecursiveMapperConfigurer<T> withoutStandardClassFieldInjector() {
        if (standardClassFieldInjector != null) {
            withoutClassFieldInjector(standardClassFieldInjector);
            standardClassFieldInjector = null;
        }

        if (fluentStandardClassFieldInjectorConfigurer != null) {
            fluentStandardClassFieldInjectorConfigurer = null;
        }

        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withClassFieldInjector(final ClassFieldInjector classFieldInjector) {
        mapper.getClassFieldInjector().add(classFieldInjector);
        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withoutClassFieldInjector(final ClassFieldInjector classFieldInjector) {
        mapper.getClassFieldInjector().remove(classFieldInjector);
        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withClassExclusionFilter(final ClassMatcher filter) {
        mapper.getClassFilter().add(filter);
        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withoutClassExclusionFilter(final ClassMatcher filter) {
        mapper.getClassFilter().remove(filter);
        return this;
    }

    public FluentRecursiveMapperConfigurer<T> excludeJavaClasses(final boolean exclude) {
        if (exclude) {
            if (javaClassMatcher == null) {
                javaClassMatcher = PackageClassMatcher.regex("java\\..*");
                mapper.getClassFilter().add(javaClassMatcher);
            }
        } else {
            if (javaClassMatcher != null) {
                mapper.getClassFilter().remove(javaClassMatcher);
                javaClassMatcher = null;
            }
        }

        return this;
    }

    public FluentRecursiveMapperConfigurer<T> withStandardClassExclusions() {
        return excludeJavaClasses(true);
    }

    public FluentRecursiveMapperConfigurer<T> withoutStandardClassExclusions() {
        return excludeJavaClasses(false);
    }

    public FluentRecursiveMapperConfigurer<T> withAlias(final Type type, final TypescriptType alias) {
        mapper.getAliasTypeMapper().alias(type, alias);
        return this;
    }

    public T and() {
        return context;
    }
}
