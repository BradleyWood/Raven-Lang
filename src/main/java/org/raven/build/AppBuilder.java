package org.raven.build;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.*;

public class AppBuilder {

    private static final String[] IGNORE = {
            "org/raven/core/Application$ByteClassLoader.class",
            "org/raven/core/Application.class"
    };

    private static final String RT = "org/raven/core/";
    private static final String BUILTINS = "raven/";

    private static Manifest mf = new Manifest();

    static {
        mf.getMainAttributes().put(new Attributes.Name("Manifest-Version"), "1.0");
        mf.getMainAttributes().put(new Attributes.Name("Created-By"), "JVM-Lang-Archiver");
        mf.getMainAttributes().put(new Attributes.Name("Built-By"), System.getProperty("user.name"));
        mf.getMainAttributes().put(new Attributes.Name("Build-Jdk"), System.getProperty("java.version"));
    }

    /**
     * Build an executable jar file
     *
     * @param classes   The class name, bytecode map
     * @param mainClass The main class
     * @param path      The output file
     * @return True on success
     */
    public static boolean build(final HashMap<String, byte[]> classes, final String mainClass, final String path) {
        try {
            mf.getMainAttributes().put(new Attributes.Name("Main-Class"), mainClass);

            File rtFile = new File(AppBuilder.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            File outputFile = new File(path);

            JarFile rt = new JarFile(rtFile);
            JarOutputStream jos = new JarOutputStream(new FileOutputStream(outputFile), mf);
            Enumeration<JarEntry> entries = rt.entries();

            boolean hasBuiltin = false;
            while (entries.hasMoreElements()) {
                JarEntry je = entries.nextElement();
                if (je.isDirectory())
                    continue;
                if (je.getName().startsWith(RT) && !ignore(je.getName()) || (hasBuiltin |= je.getName().startsWith(BUILTINS))) {
                    jos.putNextEntry(je);
                    copy(rt.getInputStream(je), jos);
                    jos.closeEntry();
                }
            }

            for (String s : classes.keySet()) {
                String file = s.replace(".", "/") + ".class";
                JarEntry je = new JarEntry(file);
                jos.putNextEntry(je);
                copy(new ByteArrayInputStream(classes.get(s)), jos);
                jos.closeEntry();
            }

            jos.close();
            return hasBuiltin;
        } catch (IOException e) {
        } catch (URISyntaxException e) {
            System.err.println("Cannot build in dev env!");
        }
        return false;
    }

    private static void copy(final InputStream in, final OutputStream out) throws IOException {
        byte[] buffer = new byte[2048];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
    }

    private static boolean ignore(final String entry) {
        for (String s : IGNORE) {
            if (entry.equals(s))
                return true;
        }
        return false;
    }
}
