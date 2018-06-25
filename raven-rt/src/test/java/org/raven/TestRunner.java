package org.raven;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.raven.antlr.RParser;
import org.raven.antlr.RavenTree;
import org.raven.compiler.Compiler;
import org.raven.error.Errors;
import org.raven.compiler.JvmMethodAnnotationProcessor;
import org.raven.core.ByteClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class TestRunner {

    private static final ByteClassLoader byteClassLoader = new ByteClassLoader(TestRunner.class.getClassLoader(), new HashMap<>());
    private static final HashMap<String, Class<?>> classes = new HashMap<>();

    private final String cl;
    private final String method;

    public TestRunner(final String cl, final String method) {
        this.cl = cl;
        this.method = method;
    }

    @Before
    public void verifyState() {
        Assert.assertNotNull("Test class failed to compile!", classes.get(cl));
    }

    @Test
    public void doTest() throws Throwable {
        Class<?> clazz = classes.get(cl);
        if (clazz == null) {
            Assert.fail("Test class is null");
        }

        Method m = clazz.getDeclaredMethod(method);
        m.setAccessible(true);
        try {
            m.invoke(null);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    private static Class<?> loadClass(final String file) {
        File f = new File(file);
        if (f.isDirectory()) {
            return null;
        } else if (classes.containsKey(file)) {
            return classes.get(file);
        }
        try {
            RParser parser = new RParser(f.getPath());
            RavenTree tree = parser.parse();
            Compiler compiler = new Compiler(f.getAbsolutePath(), f.getName().replace(".rvn", ""), tree, new JvmMethodAnnotationProcessor());
            HashMap<String, byte[]> clazzes = compiler.compile(false);
            Errors.printErrors();
            Errors.reset();

            for (Map.Entry<String, byte[]> entry : clazzes.entrySet()) {
                byteClassLoader.addDef(entry.getKey(), entry.getValue());
            }
            Class<?> clazz = byteClassLoader.loadClass(tree.getFullName().toString());

            if (clazz != null)
                clazz.getMethods(); // force verifier to run

            classes.put(file, clazz);
            return clazz;
        } catch (Exception | VerifyError e) {
            e.printStackTrace();
            classes.put(file, null);
        }
        return null;
    }

    @Parameterized.Parameters(name = "{0} {1}()")
    public static Collection getTests() throws IOException {
        List<String> paths = Files.walk(Paths.get("testData/rt_tests/"))
                .filter(p -> p.toString().endsWith(".rvn"))
                .map(Path::toString).collect(Collectors.toList());

        List<Object[]> tests = new LinkedList<>();

        paths.forEach(p -> {
            Class<?> cl = loadClass(p);
            if (cl == null) {
                tests.add(new Object[]{p, null});
            } else {
                Arrays.stream(cl.getDeclaredMethods())
                        .filter(m -> m.getName().toLowerCase().contains("test"))
                        .filter(m -> !m.isSynthetic())
                        .forEach(m -> tests.add(new Object[]{p, m.getName()}));
            }
        });
        return tests;
    }
}
