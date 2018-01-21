package org.toylang;

import org.junit.Assert;
import org.toylang.compiler.Errors;
import org.toylang.repl.Repl;

import java.io.*;
import java.util.LinkedList;

public class ReplTestRunner {

    private static final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private static final PrintStream newOut = new PrintStream(baos);

    public static void doTest(final String file) {
        System.setErr(newOut);
        System.setOut(newOut);
        Repl repl = new Repl();

        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            LinkedList<String> inputLines = new LinkedList<>();
            LinkedList<String> output = new LinkedList<>();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(">")) {
                    inputLines.add(line.substring(1) + ";");
                } else if (line.length() > 0) {
                    output.add(line);
                }
            }
            Errors.reset();
            for (String inputLine : inputLines) {
                repl.exec(inputLine);
            }
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray())));
            LinkedList<String> actualOutput = new LinkedList<>();
            while ((line = outputReader.readLine()) != null) {
                if (!line.equals("")) {
                    actualOutput.add(line);
                }
            }
            baos.reset();
            Assert.assertEquals(output, actualOutput);
        } catch (IOException e) {
            baos.reset();
            Assert.fail("Cannot read file " + file);
        } finally {
            baos.reset();
        }
    }
}
