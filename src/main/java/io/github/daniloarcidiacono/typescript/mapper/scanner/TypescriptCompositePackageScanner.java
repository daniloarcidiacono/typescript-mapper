package io.github.daniloarcidiacono.typescript.mapper.scanner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TypescriptCompositePackageScanner implements TypescriptScanner {
    private List<TypescriptScanner> scanners = new ArrayList<>();

    public TypescriptCompositePackageScanner() {
    }

    public TypescriptCompositePackageScanner scanner(final TypescriptPackageScanner scanner) {
        scanners.add(scanner);
        return this;
    }

    @Override
    public Set<Class<?>> scan() {
        final Set<Class<?>> result = new HashSet<>();
        for (TypescriptScanner scanner : scanners) {
            result.addAll(scanner.scan());
        }

        return result;
    }
}
