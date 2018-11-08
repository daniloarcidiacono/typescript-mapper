package io.github.daniloarcidiacono.typescript.mapper.mapper.injector;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptInterface;
import io.github.daniloarcidiacono.commons.lang.Composite;

public class CompositeClassFieldInjector extends Composite<ClassFieldInjector> implements ClassFieldInjector {
    @Override
    public void injectFields(final TypescriptInterface iface, final Class<?> javaClass) {
        for (ClassFieldInjector injector : components) {
            if (injector.supports(javaClass)) {
                injector.injectFields(iface, javaClass);
            }
        }
    }

    @Override
    public boolean supports(final Class<?> javaClass) {
        for (ClassFieldInjector injector : components) {
            if (injector.supports(javaClass)) {
                return true;
            }
        }

        return false;
    }
}
