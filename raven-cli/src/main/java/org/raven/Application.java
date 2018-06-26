package org.raven;

import org.apache.commons.cli.*;
import org.raven.core.ByteClassLoader;
import org.raven.core.wrappers.TObject;
import org.raven.core.wrappers.TVoid;
import org.raven.error.Errors;
import org.raven.repl.InteractiveInterpreter;
import org.raven.util.Settings;
import org.raven.util.Utility;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Application {

    private static final HelpFormatter HELP_FORMATTER = new HelpFormatter();

    private static final Option CLASSPATH = new Option("cp", "classpath", true,
            "Specify the classpath of any external class files");
    private static final Option OUTPUT_PATH = new Option("o", "out", true,
            "Specify the location to save output classes");
    private static final Option HELP = new Option("h", "help", false,
            "Displays the command line usage options");
    private static final Option BUILD = new Option("b", "build", false,
            "Save the class file output");
    private static final Option SECURE = new Option("s", "Run a script with the security manager");

    static {
        Settings.set("OUT", new File(".").getAbsolutePath());
    }

    public static void main(final String[] args) {
        final Options options = new Options();
        options.addOption(HELP);
        options.addOption(BUILD);
        options.addOption(SECURE);
        options.addOption(CLASSPATH);
        options.addOption(OUTPUT_PATH);

        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine commandLine = parser.parse(options, args);

            if (verifyArguments(commandLine)) {
                if (commandLine.hasOption("help")) {
                    HELP_FORMATTER.printHelp("ant", options);
                    return;
                }

                if (commandLine.getArgList().isEmpty()) {
                    repl();
                } else {
                    final LinkedList<String> classpath = new LinkedList<>();
                    final String srcFile = commandLine.getArgList().get(0);
                    final boolean save = commandLine.hasOption("b") || commandLine.hasOption("o");
                    final SecurityManager securityManager = commandLine.hasOption("s") ? new SecurityManager() : null;
                    final String[] arguments = commandLine.getArgList().subList(1, commandLine.getArgList().size()).toArray(new String[0]);

                    if (commandLine.hasOption("cp")) {
                        classpath.addAll(Arrays.asList(commandLine.getOptionValue("cp").split(";")));
                    }

                    if (commandLine.hasOption("o")) {
                        final File outputDir = new File(commandLine.getOptionValue("o")).getAbsoluteFile();
                        if (outputDir.isDirectory() || outputDir.mkdirs()) {
                            Settings.set("OUT", outputDir.getAbsolutePath());
                        } else {
                            System.err.println("Invalid output directory: " + outputDir);
                            return;
                        }
                    }
                    compileAndRun(srcFile, classpath, securityManager, save, arguments);
                }
            } else {
                System.err.println("Invalid usage");
                HELP_FORMATTER.printHelp("ant", options);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void compileAndRun(final String srcFile, final List<String> classpath, final SecurityManager manager,
                                      final boolean save, final String[] args)
            throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        final HashMap<String, byte[]> classMap = Utility.compile(srcFile, classpath, save);
        final File file = new File(srcFile);

        if (Errors.getErrorCount() == 0) {
            final ByteClassLoader cl = new ByteClassLoader(Utility.class.getClassLoader(), classMap);
            for (final String s : classMap.keySet()) {
                final Class<?> app = cl.loadClass(s);
                if (file.getAbsolutePath().endsWith(app.getName().replace(".", File.separator) + ".rvn")) {
                    final Method m = app.getMethod("main", String[].class);

                    if (manager != null) {
                        System.setSecurityManager(manager);
                    }

                    m.setAccessible(true);
                    m.invoke(null, (Object) args);
                    return;
                }
            }
        }
    }

    private static boolean verifyArguments(final CommandLine commandLine) {
        if (commandLine.hasOption("help") && (commandLine.hasOption("cp") || commandLine.hasOption("b") ||
                commandLine.hasOption("s") || commandLine.hasOption("o"))) {
            return false;
        }

        return commandLine.getArgList().size() != 0 || (!commandLine.hasOption("s") &&
                !commandLine.hasOption("build") && !commandLine.hasOption("cp") && !commandLine.hasOption("o"));
    }

    private static void repl() {
        final InteractiveInterpreter interactiveInterpreter = new InteractiveInterpreter();
        final Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(">>> ");
            final TObject result = interactiveInterpreter.eval(scanner.nextLine());
            if (result != TVoid.VOID) {
                System.out.println(result);
            }
        }
    }
}
