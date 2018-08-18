package io.github.daniloarcidiacono.typescript.mapper.mapper.injector;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptInterface;

public interface ClassFieldInjector {
    void injectFields(final TypescriptInterface iface, final Class<?> javaClass);
    boolean supports(final Class<?> javaClass);
}
