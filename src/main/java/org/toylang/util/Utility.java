package org.toylang.util;

import org.toylang.antlr.ToyParser;
import org.toylang.antlr.ToyTree;
import org.toylang.compiler.Compiler;
import org.toylang.compiler.Errors;
import org.toylang.compiler.JvmMethodAnnotationProcessor;
import org.toylang.core.Application;
import org.toylang.core.ByteClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Utility {


    public static void buildBuiltins() {
        try {
            Class.forName("toylang.Builtin");
        } catch (ClassNotFoundException e) {
            try {
                Errors.reset();
                Utility.compile("/src/main/toylang/toylang/", true);
                Class.forName("toylang.Builtin");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void compileAndRun(String path, String[] args) {
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
            e.getCause().printStackTrace();
        }
    }

    public static HashMap<String, byte[]> compile(String path, boolean save) throws IOException {
        File file = new File(path);
        if (!file.isAbsolute())
            file = new File(new File(".").getCanonicalPath(), path);

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
