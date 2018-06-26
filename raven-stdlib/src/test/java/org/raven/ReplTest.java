package org.raven;

import org.junit.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.raven.error.Errors;
import org.raven.repl.InteractiveInterpreter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class ReplTest {

    private static final String ANY = "*";
    private static final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private static final PrintStream newOut = new PrintStream(baos);

    private static final java.io.PrintStream sout = System.out;
    private static final java.io.PrintStream serr = System.out;

    private final String path;

    public ReplTest(final String path) {
        this.path = path;
    }

    @AfterClass
    public static void after() {
        System.setOut(sout);
        System.setErr(serr);
    }

    @Test
    public void doTest() {
        System.setErr(newOut);
        System.setOut(newOut);
        InteractiveInterpreter repl = new InteractiveInterpreter();

        try {
            FileInputStream fis = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            LinkedList<String> inputLines = new LinkedList<>();
            LinkedList<String> output = new LinkedList<>();

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(">")) {
                    line = line.substring(1);
                    int comment = line.indexOf("//");
                    if (comment > 0) {
                        line = line.substring(0, comment);
                    }
                    inputLines.add(line);
                } else if (line.length() > 0) {
                    output.add(line.trim());
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
                    actualOutput.add(line.trim());
                }
            }
            baos.reset();

            Assert.assertEquals("Wrong number of output lines: " + actualOutput.toString(), output.size(), actualOutput.size());

            for (int i = 0; i < actualOutput.size() && i < output.size(); i++) {
                if (ANY.equals(output.get(i)))
                    continue;
                Assert.assertEquals(output.get(i), actualOutput.get(i));
            }
        } catch (IOException e) {
            baos.reset();
            Assert.fail("Cannot read file " + path);
        } finally {
            baos.reset();
        }
    }

    @Parameterized.Parameters(name = "ReplTest {0}")
    public static Collection getTests() throws IOException {
        return Files.walk(Paths.get("testData/repl/"))
                .filter(p -> p.toString().endsWith(".repl"))
                .map(p -> new Object[]{p.toString()})
                .collect(Collectors.toList());
    }
}
