package org.raven.util;

import org.raven.antlr.RParser;
import org.raven.antlr.RavenTree;
import org.raven.compiler.Compiler;
import org.raven.error.Errors;
import org.raven.compiler.JvmMethodAnnotationProcessor;
import org.raven.core.Application;
import org.raven.core.ByteClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Utility {


    public static void buildBuiltins() {
        try {
            Class.forName("raven.Builtin");
        } catch (ClassNotFoundException e) {
            try {
                Errors.reset();
                Utility.compile("src/main/raven/raven/", true);
                Class.forName("raven.Builtin");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

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
                ByteClassLoader cl = new ByteClassLoader(null, Application.class.getClassLoader(), classes);
                if (classes != null) {
                    for (String s : classes.keySet()) {
                        if (file.getAbsolutePath().endsWith(s.replace(".", "\\") + ".tl")) {
                            Class<?> app = cl.loadClass(s);
                            Method m = app.getMethod("main", String[].class);
                            m.setAccessible(true);
                            m.invoke(null, (Object) args);
                            break;
                        }
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
        File file = new File(relativePath);
        if (!file.isAbsolute()) {
            file = new File(new File(".").getAbsolutePath(), relativePath);
        }

        HashMap<String, byte[]> classes = new HashMap<>();

        if (!file.isDirectory()) {
            file = file.getParentFile();
        }

        List<File> files = Files.walk(Paths.get(file.getAbsolutePath()))
                .filter(p -> p.toString().endsWith(".tl"))
                .map(p -> new File(p.toString()))
                .collect(Collectors.toList());


        for (File f : files) {
            if (!f.getAbsolutePath().endsWith(".tl"))
                continue;
            RParser parser = new RParser(f.getPath());
            RavenTree tree = parser.parse();
            Compiler compiler = new Compiler(f.getAbsolutePath(), f.getName().replace(".tl", ""), tree, new JvmMethodAnnotationProcessor());
            classes.putAll(compiler.compile(save));

            if (Errors.getErrorCount() > 0) {
                Errors.printErrors();
                Errors.reset();
            }
        }
        return classes;
    }
}
