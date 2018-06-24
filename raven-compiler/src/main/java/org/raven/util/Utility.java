package org.raven.util;

import org.raven.antlr.RParser;
import org.raven.antlr.RavenTree;
import org.raven.compiler.Builtin;
import org.raven.compiler.Compiler;
import org.raven.error.Errors;
import org.raven.compiler.JvmMethodAnnotationProcessor;
import org.raven.Application;
import org.raven.core.ByteClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Utility {


//    public static void buildBuiltins() {
//        try {
//            Class.forName("raven.Builtin");
//        } catch (ClassNotFoundException e) {
//            try {
//                Errors.reset();
//                Utility.compile("src/main/raven/raven/", true);
//                Class.forName("raven.Builtin");
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
//    }

    public static void compileAndRun(String path, final String[] args) {
        path = path.replace("/", "\\");
        File file = new File(path);

        if (!file.exists()) {
            System.err.println("Cannot find the path specified: " + path);
            return;
        }

        try {
            HashMap<String, byte[]> classes = compile(path, true);
            if (Errors.getErrorCount() == 0) {
                ByteClassLoader cl = new ByteClassLoader(Application.class.getClassLoader(), classes);
                for (String s : classes.keySet()) {
                    if (file.getAbsolutePath().endsWith(s.replace(".", "\\") + ".rvn")) {
                        Class<?> app = cl.loadClass(s);
                        Method m = app.getMethod("main", String[].class);
                        m.setAccessible(true);
                        m.invoke(null, (Object) args);
                        break;
                    }
                }
            }
        } catch (IOException | NoSuchMethodException | ClassNotFoundException | IllegalAccessException e) {
            System.err.println(e.getMessage());
        } catch (InvocationTargetException e) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e.getCause());
        }
    }

    public static HashMap<String, byte[]> compile(final String relativePath, final boolean save) throws IOException {
        return compile(relativePath, new LinkedList<>(), save);
    }

    public static HashMap<String, byte[]> compile(final String relativePath, List<String> classpath, final boolean save)
            throws IOException {
        File file = new File(relativePath);
        if (!file.isAbsolute()) {
            file = new File(new File(".").getAbsolutePath(), relativePath);
        }

        HashMap<String, byte[]> classes = new HashMap<>();

        if (!file.isDirectory()) {
            file = file.getParentFile();
        }

        List<File> files = Files.walk(Paths.get(file.getAbsolutePath()))
                .filter(p -> p.toString().endsWith(".rvn"))
                .map(p -> new File(p.toString()))
                .collect(Collectors.toList());

        for (String path : classpath) {
            if (!addToClasspath(path)) {
                return classes;
            }
        }

        LinkedList<RavenTree> trees = new LinkedList<>();
        for (File f : files) {
            if (!f.getAbsolutePath().endsWith(".rvn"))
                continue;

            RParser parser = new RParser(f.getPath());
            RavenTree tree = parser.parse();

            trees.add(tree);

            if (tree.getFullName().toString().equalsIgnoreCase("raven.Builtin")) {
                Builtin.addBuiltins(tree);
            }
        }

        for (RavenTree tree : trees) {
            if (tree != null) {
                Compiler compiler = new Compiler(tree.getSourceFile(), new File(tree.getSourceFile()).getName()
                        .replace(".rvn", ""), tree, new JvmMethodAnnotationProcessor());
                classes.putAll(compiler.compile(save));
            }
        }

        return classes;
    }

    private static boolean addToClasspath(final String s) throws IOException {
        return addToClasspath(new File(s).toURI().toURL());
    }

    private static boolean addToClasspath(final URL u) {
        if (!(ClassLoader.getSystemClassLoader() instanceof URLClassLoader)) {
            Errors.put("Invalid Class Loader");
            return false;
        } else if (addUrlMethod == null) {
            try {
                addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                addUrlMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Errors.put("Invalid Class Loader");
                return false;
            }
        }
        if (addUrlMethod == null) {
            Errors.put("Invalid Class Loader");
            return false;
        }

        try {
            addUrlMethod.invoke(ClassLoader.getSystemClassLoader(), u);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Errors.put("Failed to add URL: " + u + " to classpath");
            return false;
        }
        return true;
    }

    private static Method addUrlMethod = null;

}
