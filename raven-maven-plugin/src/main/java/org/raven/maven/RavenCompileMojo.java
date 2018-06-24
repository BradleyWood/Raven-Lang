package org.raven.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "compile", defaultPhase = LifecyclePhase.COMPILE)
public class RavenCompileMojo extends AbstractMojo {

    @Override
    public void execute() {
    }
}
