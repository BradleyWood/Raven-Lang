package org.toylang.gen;

import org.stringtemplate.v4.*;
import org.toylang.antlr.ToyParser;
import org.toylang.antlr.ToyTree;
import org.toylang.antlr.ast.Fun;
import org.toylang.antlr.ast.Statement;

import java.io.*;

public class TestGenerator {

    public static void main(String[] args) throws IOException {
        String testSetup = readFile("testData/rt_tests/TestSetup.st");
        String tlTest = readFile("testData/rt_tests/TLTest.st");

        ST testFile = new ST(readFile("testData/rt_tests/TestFile.st"));

        StringBuilder body = new StringBuilder();

        File testFolder = new File("testData/rt_tests/org/toylang/test/");

        for (File file : testFolder.listFiles()) {
            String name = file.getName().replace(".tl", "");
            String f = "\"" + file.getPath().replace("\\", "/") + "\"";
            ST setup = new ST(testSetup);
            setup.add("name", name);
            setup.add("file", f);

            body.append(setup.render());

            ToyParser parser = new ToyParser(file.getPath());
            ToyTree tree = parser.parse();

            for (Statement statement : tree.getStatements()) {
                if (statement instanceof Fun) {
                    Fun fun = (Fun) statement;
                    if (fun.getName().toString().toLowerCase().contains("test")) {
                        ST test = new ST(tlTest);
                        test.add("name", name);
                        test.add("test", fun.getName().toString());
                        test.add("file", f);
                        body.append(test.render());
                    }
                }
            }
        }

        testFile.add("package", "org.toylang");
        testFile.add("name", "TLFiles");
        testFile.add("body", body.toString());

        File output = new File("src/test/java/org/toylang/TestTLFiles.java");
        FileOutputStream fos = new FileOutputStream(output);
        fos.write(testFile.render().getBytes());
        fos.close();
    }

    private static String readFile(String str) throws IOException {
        return readFile(new File(str));
    }

    private static String readFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] ba = new byte[1024];
        int len;
        while ((len = fis.read(ba)) > 0) {
            baos.write(ba, 0, len);
        }
        return new String(baos.toByteArray());
    }
}
