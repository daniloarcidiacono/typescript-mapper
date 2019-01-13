package io.github.daniloarcidiacono.typescriptmapper.core.matcher;

import java.util.regex.Pattern;

public class PackageClassMatcher implements ClassMatcher {
    private Pattern pattern;

    private PackageClassMatcher(final String literal) {
        pattern = Pattern.compile(Pattern.quote(literal));
    }

    private PackageClassMatcher(final Pattern pattern) {
        this.pattern = pattern;
    }

    public static PackageClassMatcher literal(final String packageName) {
        return new PackageClassMatcher(packageName);
    }

    public static PackageClassMatcher regex(final String regex) {
        return new PackageClassMatcher(Pattern.compile(regex));
    }

    public static PackageClassMatcher regex(final Pattern pattern) {
        return new PackageClassMatcher(pattern);
    }

    @Override
    public boolean matches(Class<?> clazz) {
        if (clazz.getPackage() != null) {
            return pattern.matcher(clazz.getPackage().getName()).matches();
        }

        return pattern.matcher("").matches();
    }
}
