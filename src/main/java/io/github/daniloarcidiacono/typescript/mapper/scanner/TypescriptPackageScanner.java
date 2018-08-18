package io.github.daniloarcidiacono.typescript.mapper.scanner;

import io.github.daniloarcidiacono.typescript.mapper.annotation.TypescriptDTO;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

public class TypescriptPackageScanner implements TypescriptScanner {
    private Set<String> packages = new HashSet<>();

    public TypescriptPackageScanner() {
    }

    public TypescriptPackageScanner registerPackage(final String packageName) {
        packages.add(packageName);
        return this;
    }

    public TypescriptPackageScanner registerPackage(final Package pack) {
        packages.add(pack.getName());
        return this;
    }

    public TypescriptPackageScanner registerPackage(final Class<?> clazz) {
        packages.add(clazz.getPackage().getName());
        return this;
    }

    @Override
    public Set<Class<?>> scan() {
        return new Reflections(packages).getTypesAnnotatedWith(TypescriptDTO.class);
    }
}
