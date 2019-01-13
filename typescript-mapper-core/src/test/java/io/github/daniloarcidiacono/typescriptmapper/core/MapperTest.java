package io.github.daniloarcidiacono.typescriptmapper.core;

import io.github.daniloarcidiacono.commons.lang.StringCommons;
import io.github.daniloarcidiacono.typescriptmapper.core.builder.FluentConfigurer;
import io.github.daniloarcidiacono.typescriptmapper.core.builder.MappingChainBuilder;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for Typescript mapper.<br>
 * Sample Java snippets (contained in {@value #TEST_CASE_SOURCE_PACKAGE} package) are mapped into {@value #TEST_CASE_GENERATED} and checked against the expected results (see {@value #TEST_CASE_EXPECTED} in resources).
 */
class MapperTest {
    private static final String TEST_CASE_NAME_VARIABLE = "${TEST_CASE}";

    private static final String TEST_CASE_GENERATED = "testcases/${TEST_CASE}/generated/";
    private static final String TEST_CASE_EXPECTED = "testcases/${TEST_CASE}/expected/";
    private static final String TEST_CASE_SOURCE_PACKAGE = "io.github.daniloarcidiacono.typescriptmapper.core.testcases.${TEST_CASE}.sources";
    private static final String TEST_CASE_CONFIG_CLASS_FQN = "io.github.daniloarcidiacono.typescriptmapper.core.testcases.${TEST_CASE}";

    @Test
    void testEnclosing() throws Exception {
        runTestCase("enclosing").assertMatchesFolder();
    }

    @Test
    void testExpanding() throws Exception {
        runTestCase("expanding").assertMatchesFolder();
    }

    @Test
    void testGenerics() throws Exception {
        runTestCase("generics").assertMatchesFolder();
    }

    @Test
    void testMisc() throws Exception {
        runTestCase("misc").assertMatchesFolder();
    }

    /**
     * Runs a specific test case.
     * A test case is expected to have the following folder structure:
     * <pre>
     * testCaseName/
     *      sources/
     *          source1.java
     *          source2.java
     *
     *      testcaseNameTestCase.java
     * </pre>
     *
     * @param testCaseName the name of the test case to run
     * @return the test case result
     * @throws Exception if any error occurs
     */
    TestCaseResult runTestCase(final String testCaseName) throws Exception {
        final URI outputUri = MapperTest.class.getClassLoader().getResource("").toURI().resolve(
            TEST_CASE_GENERATED.replace(TEST_CASE_NAME_VARIABLE, testCaseName)
        );
        final URI expectedUri = new URI(TEST_CASE_EXPECTED.replace(TEST_CASE_NAME_VARIABLE, testCaseName));
        final String packageDomain = TEST_CASE_SOURCE_PACKAGE.replace(TEST_CASE_NAME_VARIABLE, testCaseName);

        final TestCase testCase = forName(testCaseName);
        final MappingChainBuilder chainBuilder = new MappingChainBuilder();

        testCase.configure(
            FluentConfigurer.forChain(chainBuilder)
                .standard(packageDomain, outputUri)
                .withoutStandardSourceMappers()
                .withSourceMapper((javaClass, declaration) -> {
                    try {
                        return new URI(testCaseName + ".ts");
                    } catch (URISyntaxException e) {
                        throw new IllegalStateException("Invalid uri", e);
                    }
                }),
            outputUri
        );

        chainBuilder.process();
        return new TestCaseResult(testCaseName, chainBuilder, outputUri, expectedUri);
    }

    /**
     * Instances the {@link TestCase} implementation based on the name.
     * For example, if testCaseName is "generics", the class "GenericsTestCase" is instanced.
     * {@link TestCase} implementations are expected to be defined in {@value #TEST_CASE_CONFIG_CLASS_FQN} packages.
     *
     * @param testCaseName the name of the test case.
     * @return the {@link TestCase} implementation instance.
     */
    TestCase forName(final String testCaseName) {
        final String packageName = TEST_CASE_CONFIG_CLASS_FQN.replace(TEST_CASE_NAME_VARIABLE, testCaseName);
        final String className = StringCommons.capitalize(testCaseName) + "TestCase";
        final String fqName = packageName + "." + className;
        try {
            Class<?> aClass = Class.forName(fqName);
            assertTrue(aClass.getInterfaces().length > 0 && aClass.getInterfaces()[0].equals(TestCase.class), fqName + " does not extend TestCase!");

            return (TestCase) aClass.newInstance();
        } catch (ClassNotFoundException e) {
            // No test case class found, use standard one
            return (configurer, uri) -> {
            };
        } catch (IllegalAccessException | InstantiationException e) {
            fail("Could not instance " + fqName + " (does it have the no-args constructor?)");
        }

        return null;
    }
}