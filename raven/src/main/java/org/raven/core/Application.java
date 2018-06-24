package org.raven.core;

import org.apache.commons.cli.*;
import org.raven.build.AppBuilder;
import org.raven.error.Errors;
import org.raven.repl.InteractiveInterpreter;
import org.raven.util.Settings;

import java.io.IOException;
import java.util.*;

import static org.raven.util.Utility.compile;
import static org.raven.util.Utility.compileAndRun;

public class Application {

    static {
        Settings.set("OUT", "target/classes");
    }

    public static void main(final String[] args) {
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
        InteractiveInterpreter interactiveInterpreter = new InteractiveInterpreter();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            String line = scanner.nextLine();
            int comment = line.indexOf("//");
            if (comment > 0) {
                line = line.substring(0, comment);
            }
            while (line.endsWith("\\")) {
                line = line.substring(0, line.length() - 1) + scanner.nextLine();
            }
            interactiveInterpreter.exec(line + ";");
        }
    }

    private static void build(final String[] buildOptions) throws IOException {
        HashMap<String, byte[]> classes = compile(buildOptions[0], false);
        if (Errors.getErrorCount() > 0) {
            return;
        }
        String mainClass = null;
        for (String s : classes.keySet()) {
            if (buildOptions[0].replace("/", "\\").endsWith(s.replace(".", "\\") + ".rvn")) {
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

    private static void cmdError(final Options options) {
        System.err.println("Invalid usage");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ant", options);
    }

    private static boolean onlyOneTrue(final boolean... booleans) {
        int c = 0;
        for (boolean b : booleans) {
            c += b ? 1 : 0;
        }
        return c == 1;
    }
}
