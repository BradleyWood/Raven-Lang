package org.raven.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.raven.error.Errors;
import org.raven.maven.test.JUnitAnnotationProcessor;
import org.raven.util.Settings;
import org.raven.util.Utility;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Mojo(name = "test-compile", defaultPhase = LifecyclePhase.TEST_COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class RavenTestCompileMojo extends AbstractMojo {

    @Parameter(name = "testSourceDirectory", defaultValue = "${project.basedir}/src/test/raven")
    private String testSourceDirectory;

    @Parameter(defaultValue = "${project.compileClasspathElements}", required = true, readonly = true)
    private List<String> classpath;

    @Parameter(defaultValue = "${project.build.testOutputDirectory}", required = true, readonly = true)
    private String outputDirectory;

    @Override
    public void execute() throws CompilationFailureException {
        Settings.set("OUT", outputDirectory);
        try {
            Utility.compile(testSourceDirectory, classpath, Collections.singletonList(new JUnitAnnotationProcessor()),
                    true);
        } catch (IOException e) {
            throw new CompilationFailureException(e.getMessage());
        } finally {
            final Log logger = getLog();
            Errors.getErrors().forEach(error -> logger.error(error.getMessage()));
        }
        if (Errors.getErrorCount() > 0) {
            throw new CompilationFailureException();
        }
    }

}
