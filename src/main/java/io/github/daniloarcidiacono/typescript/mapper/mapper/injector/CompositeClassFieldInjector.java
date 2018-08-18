package io.github.daniloarcidiacono.typescript.mapper.mapper.injector;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeClassFieldInjector implements ClassFieldInjector {
    private List<ClassFieldInjector> injectors = new ArrayList<>();

    public CompositeClassFieldInjector injector(final ClassFieldInjector injector) {
        injectors.add(injector);
        return this;
    }

    public CompositeClassFieldInjector injector(final ClassFieldInjector ...injector) {
        Collections.addAll(injectors, injector);
        return this;
    }

    @Override
    public void injectFields(final TypescriptInterface iface, final Class<?> javaClass) {
        for (ClassFieldInjector injector : injectors) {
            if (injector.supports(javaClass)) {
                injector.injectFields(iface, javaClass);
            }
        }
    }

    @Override
    public boolean supports(final Class<?> javaClass) {
        for (ClassFieldInjector injector : injectors) {
            if (injector.supports(javaClass)) {
                return true;
            }
        }

        return false;
    }
}
