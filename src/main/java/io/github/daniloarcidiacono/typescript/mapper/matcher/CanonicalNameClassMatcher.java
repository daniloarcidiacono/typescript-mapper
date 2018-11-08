package io.github.daniloarcidiacono.typescript.mapper.matcher;

import java.util.regex.Pattern;

public class CanonicalNameClassMatcher implements ClassMatcher {
    private Pattern pattern;

    private CanonicalNameClassMatcher(final String literal) {
        pattern = Pattern.compile(Pattern.quote(literal));
    }

    private CanonicalNameClassMatcher(final Pattern pattern) {
        this.pattern = pattern;
    }

    public static CanonicalNameClassMatcher literal(final String packageName) {
        return new CanonicalNameClassMatcher(packageName);
    }

    public static CanonicalNameClassMatcher regex(final String regex) {
        return new CanonicalNameClassMatcher(Pattern.compile(regex));
    }

    public static CanonicalNameClassMatcher regex(final Pattern pattern) {
        return new CanonicalNameClassMatcher(pattern);
    }

    @Override
    public boolean matches(final Class<?> clazz) {
        return clazz != null && pattern.matcher(clazz.getName()).matches();
    }
}
