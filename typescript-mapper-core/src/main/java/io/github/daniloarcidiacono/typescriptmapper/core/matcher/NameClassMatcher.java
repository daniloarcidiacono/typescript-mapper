package io.github.daniloarcidiacono.typescriptmapper.core.matcher;

import java.util.regex.Pattern;

public class NameClassMatcher implements ClassMatcher {
    private Pattern pattern;

    private NameClassMatcher(final String literal) {
        pattern = Pattern.compile(Pattern.quote(literal));
    }

    private NameClassMatcher(final Pattern pattern) {
        this.pattern = pattern;
    }

    public static NameClassMatcher literal(final String packageName) {
        return new NameClassMatcher(packageName);
    }

    public static NameClassMatcher regex(final String regex) {
        return new NameClassMatcher(Pattern.compile(regex));
    }

    public static NameClassMatcher regex(final Pattern pattern) {
        return new NameClassMatcher(pattern);
    }

    @Override
    public boolean matches(final Class<?> clazz) {
        return clazz != null && pattern.matcher(clazz.getName()).matches();
    }
}
