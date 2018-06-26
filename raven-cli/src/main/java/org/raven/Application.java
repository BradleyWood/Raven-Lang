package org.raven;

import org.apache.commons.cli.*;
import org.raven.core.wrappers.TObject;
import org.raven.core.wrappers.TVoid;
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

        options.addOption("r", true, "Run program");

        Option programArgs = new Option("args", true, "Specify command line arguments for your program");
        programArgs.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(programArgs);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("secure")) {
                System.setSecurityManager(new SecurityManager());
            }

            boolean correctness = cmd.hasOption("s");
            boolean run = cmd.hasOption("r");
            boolean REPL = cmd.hasOption("repl");

            if (!onlyOneTrue(correctness, run, REPL)) {
                cmdError(options);
                return;
            }

            if (correctness) {
                String[] values = cmd.getOptionValues("s");
                compile(values[0], false);
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
            System.out.print(">>> ");
            TObject result = interactiveInterpreter.eval(scanner.nextLine());
            if (result != TVoid.VOID) {
                System.out.println(result);
            }
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
