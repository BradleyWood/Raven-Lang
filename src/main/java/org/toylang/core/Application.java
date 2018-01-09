package org.toylang.core;

import org.apache.commons.cli.*;
import org.toylang.build.AppBuilder;
import org.toylang.compiler.Errors;
import org.toylang.antlr.ToyParser;
import org.toylang.antlr.ToyTree;
import org.toylang.compiler.Compiler;
import org.toylang.compiler.JvmMethodAnnotationProcessor;
import org.toylang.repl.Repl;
import org.toylang.test.Assert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {

    public static boolean REPL = false;

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("test", false, "Runs tests");
        options.addOption("secure", false, "Run with security manager");
        options.addOption("s", true, "Check files for correctness");
        options.addOption("repl", false, "Run in REPL mode");

        Option buildOption = new Option("b", true, "Build program into executable jar");
        buildOption.setArgs(2);
        options.addOption(buildOption);

        options.addOption("r", true, "Run program");

        Option programArgs = new Option("args", true, "Specify command line arguments for your program");
        programArgs.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(programArgs);

        try {
            Class.forName("toylang.Builtin");
        } catch (ClassNotFoundException e) {
            try {
                compile("/src/main/toylang/toylang/", true);
                if (Errors.getErrorCount() > 0) {
                    Errors.printErrors();
                } else {
                    System.out.println("Builtins have been built");
                }
                return;
            } catch (IOException e1) {
                System.err.println("Cannot build builtins");
                return;
            }
        }

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("secure")) {
                System.setSecurityManager(new WebSecurityManager());
            }

            boolean test = cmd.hasOption("test");
            boolean correctness = cmd.hasOption("s");
            boolean build = cmd.hasOption("b");
            boolean run = cmd.hasOption("r");
            REPL = cmd.hasOption("repl");

            if (!onlyOneTrue(test, correctness, build, run, REPL)) {
                cmdError(options);
                return;
            }

            if (test) {
                compileAndTest("/test/org/toylang/test/");
            } else if (correctness) {
                String[] values = cmd.getOptionValues("s");
                compile(values[0], false);
            } else if (build) {
                String[] buildOptions = cmd.getOptionValues("b");
                HashMap<String, byte[]> classes = compile(buildOptions[0], false);
                if (Errors.getErrorCount() > 0) {
                    return;
                }
                String mainClass = null;
                for (String s : classes.keySet()) {
                    if (buildOptions[0].replace("/", "\\").endsWith(s.replace(".", "\\") + ".tl")) {
                        mainClass = s;
                    }
                }
                if (mainClass == null) {
                    System.err.println("Could not determine main class.");
                    return;
                }
                boolean b = AppBuilder.build(classes, mainClass, buildOptions[1]);
                if (!b) {
                    System.err.println("Failed to build executable jarfile");
                }
            } else if (run) {
                String[] values = cmd.getOptionValues("r");
                compileAndRun(values[0], cmd.getOptionValues("args"));
            } else if (REPL) {
                Repl REPL = new Repl();
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    REPL.exec(scanner.nextLine() + ";");
                }
            }
        } catch (ParseException e) {
            cmdError(options);
        } catch (IOException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void cmdError(Options options) {
        System.err.println("Invalid usage");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ant", options);
    }

    private static boolean onlyOneTrue(boolean... booleans) {
        int c = 0;
        for (boolean b : booleans) {
            c += b ? 1 : 0;
        }
        return c == 1;
    }

    public static void compileAndTest(String path) throws IOException, ClassNotFoundException {
        path = path.replace("/", "\\");

        HashMap<String, byte[]> classes = compile(path, true);

        int numTests = 0;
        AtomicInteger fails = new AtomicInteger();

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
                            if (!method.getName().toLowerCase().contains("exception"))
                                fails.getAndIncrement();
                        }
                        Assert.errors.forEach(error -> {
                            if (!method.getName().toLowerCase().contains("fail")) {
                                error.printStackTrace();
                                fails.getAndIncrement();
                            }
                        });
                        Assert.errors.clear();
                    }
                }
            }
        }
        System.err.println("All tests completed, " + (numTests - fails.get()) + "/" + numTests + " passed.");
    }

    public static void compileAndRun(String path, String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        path = path.replace("/", "\\");
        File file = new File(path);

        HashMap<String, byte[]> classes = compile(path, true);
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
