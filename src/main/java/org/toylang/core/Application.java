package org.toylang.core;

import org.toylang.antlr.Errors;
import org.toylang.antlr.ToyParser;
import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.Statement;
import org.toylang.compiler.Compiler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Application {

    public static void main(String[] args) {
        try {
            if (args.length >= 1) {
                if(args[0].endsWith(".tl")) {
                    compileAndRun(args[0], Arrays.copyOfRange(args, 1, args.length));
                } else {
                    compile(args[0]);
                }
            } else {
                compile("/src/main/toylang/toylang/lang/");

                System.out.println("----------------------------------------------");

                compile("/scripts/test/");
            }

        } catch (IOException | NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void compileAndRun(String path, String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        path = path.replace("/", "\\");
        File file = new File(path);
        ToyParser parser = new ToyParser(file.getAbsolutePath());
        ToyTree tree = parser.parse();
        String name = file.getName().replace(".tl", "");
        Compiler compiler = new Compiler(file.getAbsolutePath(), name, tree);
        HashMap<String, byte[]> classes = compiler.compile(false);
        if(Errors.getErrorCount() == 0) {
            ByteClassLoader cl = new ByteClassLoader(null, Application.class.getClassLoader(), classes);
            for (String s : classes.keySet()) {
                if(file.getAbsolutePath().endsWith(s.replace(".","\\") + ".tl")) {
                    Class<?> app = cl.loadClass(s);
                    Method m = app.getMethod("main", String[].class);
                    m.invoke(null, (Object) args);
                    break;
                }
            }
        }
    }

    public static void compile(String path) throws IOException {
        Compiler compiler;
        ToyParser parser;
        ToyTree tree;
        File file = new File(new File(".").getCanonicalPath(), path);

        for (File f : Objects.requireNonNull(file.listFiles())) {
            if(!f.getAbsolutePath().endsWith(".tl"))
                continue;
            parser = new ToyParser(f.getPath());
            tree = parser.parse();
            compiler = new Compiler(f.getAbsolutePath(), f.getName().replace(".tl", ""), tree);
            compiler.compile();
        }
        if(Errors.getErrorCount() == 0) {
            System.out.println("Compilation Completed Successfully!");
        }
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
