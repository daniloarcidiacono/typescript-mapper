package io.github.daniloarcidiacono.typescript.mapper.mapper;

import io.github.daniloarcidiacono.typescript.template.declaration.TypescriptDeclaration;

public class TypescriptMapped {
    // Optional
    private Class<?> javaClass;
    private TypescriptDeclaration declaration;

    public TypescriptMapped(final TypescriptDeclaration declaration) {
        this.declaration = declaration;
    }

    public TypescriptMapped(final Class<?> javaClass, final TypescriptDeclaration declaration) {
        this.javaClass = javaClass;
        this.declaration = declaration;
    }

    public Class<?> getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class<?> javaClass) {
        this.javaClass = javaClass;
    }

    public TypescriptDeclaration getDeclaration() {
        return declaration;
    }

    public void setDeclaration(final TypescriptDeclaration declaration) {
        this.declaration = declaration;
    }

    @Override
    public String toString() {
        if (javaClass != null) {
            return declaration.getIdentifier() + " bound to " + javaClass.getName();
        }

        return declaration.getIdentifier();
    }
}