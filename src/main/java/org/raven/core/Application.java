package org.raven.core;

import org.apache.commons.cli.*;
import org.raven.build.AppBuilder;
import org.raven.error.Errors;
import org.raven.repl.Repl;
import org.raven.util.Settings;

import java.io.IOException;
import java.util.*;

import static org.raven.util.Utility.compile;
import static org.raven.util.Utility.compileAndRun;

public class Application {

    public static void main(String[] args) {
        Options options = new Options();
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
            Class.forName("raven.Builtin");
        } catch (ClassNotFoundException e) {
            try {
                compile("/src/main/raven/raven/", true);
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

            boolean correctness = cmd.hasOption("s");
            boolean build = cmd.hasOption("b");
            boolean run = cmd.hasOption("r");
            boolean REPL = cmd.hasOption("repl");

            if (!onlyOneTrue(correctness, build, run, REPL)) {
                cmdError(options);
                return;
            }

            if (correctness) {
                String[] values = cmd.getOptionValues("s");
                compile(values[0], false);
            } else if (build) {
                String[] buildOptions = cmd.getOptionValues("b");
                build(buildOptions);
            } else if (run) {
                String[] values = cmd.getOptionValues("r");
                compileAndRun(values[0], cmd.hasOption("args") ? cmd.getOptionValues("args") : new String[0]);
            } else if (REPL) {
                repl();
            }
        } catch (ParseException e) {
            cmdError(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void repl() {
        Repl REPL = new Repl();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            int comment = line.indexOf("//");
            if (comment > 0) {
                line = line.substring(0, comment);
            }
            while (line.endsWith("\\")) {
                System.out.print(">");
                line = line.substring(0, line.length() - 1) + scanner.nextLine();
            }
            REPL.exec(line + ";");
        }
    }

    private static void build(String[] buildOptions) throws IOException {
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
}
