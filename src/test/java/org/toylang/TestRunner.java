package org.toylang;

import org.junit.Assert;
import org.toylang.antlr.ToyParser;
import org.toylang.antlr.ToyTree;
import org.toylang.compiler.Compiler;
import org.toylang.compiler.Errors;
import org.toylang.compiler.JvmMethodAnnotationProcessor;
import org.toylang.core.ByteClassLoader;
import org.toylang.util.Utility;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TestRunner {

    private static final ByteClassLoader byteClassLoader = new ByteClassLoader(new URL[0], TestRunner.class.getClassLoader(), new HashMap<>());
    private static final HashMap<String, Class<?>> classes = new HashMap<>();

    public static void doTest(final String cl, final String method) throws Throwable {
        Class<?> clazz = classes.get(cl);
        if (clazz == null) {
            Assert.fail("Test class is null");
        }

        Method m = clazz.getDeclaredMethod(method);
        m.setAccessible(true);
        m.invoke(null);
        if (org.toylang.test.Assert.errors.size() > 0) {
            AssertionError e = org.toylang.test.Assert.errors.get(0);
            org.toylang.test.Assert.errors.clear();
            throw e;
        }
    }

    private static void buildBuiltins() {
        try {
            Class.forName("toylang.Builtin");
        } catch (ClassNotFoundException e) {
            try {
                Errors.reset();
                Utility.compile("/src/main/toylang/toylang/", true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void loadClass(final String file) {
        buildBuiltins();
        File f = new File(file);
        if (f.isDirectory() || classes.containsKey(file)) {
            return;
        }
        try {
            ToyParser parser = new ToyParser(f.getPath());
            ToyTree tree = parser.parse();
            Compiler compiler = new Compiler(f.getAbsolutePath(), f.getName().replace(".tl", ""), tree, new JvmMethodAnnotationProcessor());
            HashMap<String, byte[]> clazzes = compiler.compile(false);
            Errors.printErrors();
            Errors.reset();
            for (Map.Entry<String, byte[]> entry : clazzes.entrySet()) {
                byteClassLoader.addDef(entry.getKey(), entry.getValue());
            }
            Class<?> clazz = byteClassLoader.loadClass(tree.getFullName().toString());
            classes.put(file, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            classes.put(file, null);
        }
    }
}
