package io.github.daniloarcidiacono.typescript.mapper.matcher;

import java.util.regex.Pattern;

public class SimpleNameMatcher implements ClassMatcher {
    private Pattern pattern;

    private SimpleNameMatcher(final String literal) {
        pattern = Pattern.compile(Pattern.quote(literal));
    }

    private SimpleNameMatcher(final Pattern pattern) {
        this.pattern = pattern;
    }

    public static SimpleNameMatcher literal(final String packageName) {
        return new SimpleNameMatcher(packageName);
    }

    public static SimpleNameMatcher regex(final String regex) {
        return new SimpleNameMatcher(Pattern.compile(regex));
    }

    public static SimpleNameMatcher regex(final Pattern pattern) {
        return new SimpleNameMatcher(pattern);
    }

    @Override
    public boolean matches(final Class<?> clazz) {
        return clazz != null && pattern.matcher(clazz.getSimpleName()).matches();
    }
}
