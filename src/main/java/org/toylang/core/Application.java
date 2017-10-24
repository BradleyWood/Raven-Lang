package org.toylang.core;

import org.toylang.antlr.Errors;
import org.toylang.antlr.ToyParser;
import org.toylang.antlr.ToyTree;
import org.toylang.compiler.Compiler;
import org.toylang.test.Assert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {

    public static void main(String[] args) {
        try {
            if (args.length >= 1) {
                if (args[0].endsWith(".tl")) {
                    compileAndRun(args[0], Arrays.copyOfRange(args, 1, args.length));
                } else if (args[0].equals("-test")) {
                    compileAndTest("/test/org/toylang/test/");
                } else {
                    compile(args[0], true);
                }
            } else {
                compile("/src/main/toylang/toylang/", true);

                System.out.println("----------------------------------------------");
            }
        } catch (IOException | NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void compileAndTest(String path) throws IOException, ClassNotFoundException {
        path = path.replace("/", "\\");

        HashMap<String, byte[]> classes = compile(path, true);

        int numTests = 0;
        AtomicInteger fails = new AtomicInteger();

        if (Errors.getErrorCount() == 0) {
            ByteClassLoader cl = new ByteClassLoader(null, Application.class.getClassLoader(), classes);
            if (classes != null) {
                for (String s : classes.keySet()) {
                    Class<?> clazz = cl.loadClass(s);
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.getName().toLowerCase().contains("test") && method.getParameterCount() == 0) {
                            try {
                                numTests++;
                                method.setAccessible(true);
                                method.invoke(null);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.getCause().printStackTrace();
                                fails.getAndIncrement();
                            }
                            Assert.errors.forEach(error -> {
                                error.printStackTrace();
                                fails.getAndIncrement();
                            });
                            Assert.errors.clear();
                        }
                    }
                }
            }
        }
        System.err.println("All tests completed, " + (numTests - fails.get()) + "/" + numTests + " passed.");
    }

    public static void compileAndRun(String path, String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        path = path.replace("/", "\\");
        File file = new File(path);

        HashMap<String, byte[]> classes = compile(path, false);
        if (Errors.getErrorCount() == 0) {
            ByteClassLoader cl = new ByteClassLoader(null, Application.class.getClassLoader(), classes);
            if (classes != null) {
                for (String s : classes.keySet()) {
                    if (file.getAbsolutePath().endsWith(s.replace(".", "\\") + ".tl")) {
                        Class<?> app = cl.loadClass(s);
                        Method m = app.getMethod("main", String[].class);
                        m.invoke(null, (Object) args);
                        break;
                    }
                }
            }
        }
    }

    public static HashMap<String, byte[]> compile(String path, boolean save) throws IOException {
        File file = new File(new File(".").getCanonicalPath(), path);

        HashMap<String, byte[]> classes = new HashMap<>();
        LinkedList<File> files = new LinkedList<>();

        if (!file.isDirectory()) {
            files.add(file);
        } else {
            File[] fs = file.listFiles();
            if (fs != null)
                files.addAll(Arrays.asList(fs));
        }

        for (File f : files) {
            if (!f.getAbsolutePath().endsWith(".tl"))
                continue;
            ToyParser parser = new ToyParser(f.getPath());
            ToyTree tree = parser.parse();
            Compiler compiler = new Compiler(f.getAbsolutePath(), f.getName().replace(".tl", ""), tree);
            classes.putAll(compiler.compile(save));
        }
        if (Errors.getErrorCount() == 0) {
            System.out.println("Compilation Completed Successfully!");
            return classes;
        }
        return null;
    }

    public static class ByteClassLoader extends ClassLoader {

        private final Map<String, byte[]> extraClassDefs;

        public ByteClassLoader(URL[] urls, ClassLoader parent, Map<String, byte[]> extraClassDefs) {
            super(parent);
            this.extraClassDefs = new HashMap<>(extraClassDefs);
        }

        @Override
        protected Class<?> findClass(final String name) throws ClassNotFoundException {
            byte[] classBytes = this.extraClassDefs.remove(name);
            if (classBytes != null) {
                return defineClass(name, classBytes, 0, classBytes.length);
            }
            return super.findClass(name);
        }

    }
}
