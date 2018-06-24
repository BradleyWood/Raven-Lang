package org.raven.core;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

public class WebSecurityManager extends SecurityManager {

    public WebSecurityManager() {
        super();
    }

    @Override
    protected Class[] getClassContext() {
        return super.getClassContext();
    }

    @Override
    public Object getSecurityContext() {
        return super.getSecurityContext();
    }

    @Override
    public void checkPermission(final Permission perm) {
        super.checkPermission(perm);
    }

    @Override
    public void checkPermission(final Permission perm, final Object context) {
        super.checkPermission(perm, context);
    }

    @Override
    public void checkCreateClassLoader() {
        throw new SecurityException("Program cannot cannot create class loader");
    }

    @Override
    public void checkAccess(final Thread t) {
        super.checkAccess(t);
    }

    @Override
    public void checkAccess(final ThreadGroup g) {
        super.checkAccess(g);
    }

    @Override
    public void checkExit(final int status) {
        super.checkExit(status);
    }

    @Override
    public void checkExec(final String cmd) {
        throw new SecurityException("Program cannot call exec()");
    }

    @Override
    public void checkLink(final String lib) {
        throw new SecurityException("Program cannot load dynamic link library");
    }

    @Override
    public void checkRead(final FileDescriptor fd) {
        throw new SecurityException("Program cannot cannot read from file");
    }

    @Override
    public void checkRead(final String file) {
        throw new SecurityException("Program cannot cannot read from file");
    }

    @Override
    public void checkRead(final String file, final Object context) {
        throw new SecurityException("Program cannot cannot read from file");
    }

    @Override
    public void checkWrite(final FileDescriptor fd) {
        throw new SecurityException("Program cannot cannot write to file");
    }

    @Override
    public void checkWrite(final String file) {
        throw new SecurityException("Program cannot cannot write to file: " + file);
    }

    @Override
    public void checkDelete(final String file) {
        throw new SecurityException("Program cannot cannot delete file: " + file);
    }

    @Override
    public void checkConnect(final String host, final int port) {
        throw new SecurityException("Program cannot connect to " + host + ":" + port);
    }

    @Override
    public void checkConnect(final String host, final int port, final Object context) {
        throw new SecurityException("Program cannot connect to " + host + ":" + port);
    }

    @Override
    public void checkListen(final int port) {
        throw new SecurityException("Program cannot listen on port " + port);
    }

    @Override
    public void checkAccept(final String host, final int port) {
        throw new SecurityException("Program cannot accept connections");
    }

    @Override
    public void checkMulticast(final InetAddress maddr) {
        throw new SecurityException("Program cannot multicast");
    }

    @Override
    public void checkPropertiesAccess() {
        throw new SecurityException("Program cannot access system properties");
    }

    @Override
    public void checkPropertyAccess(final String key) {
        if (key.contains("os") || key.contains("user")) {
            throw new SecurityException("Program cannot access system property: "+key);
        }
    }

    @Override
    public void checkPrintJobAccess() {
        throw new SecurityException("Program cannot print");
    }


    @Override
    public void checkPackageAccess(final String pkg) {
        super.checkPackageAccess(pkg);
    }

    @Override
    public void checkPackageDefinition(final String pkg) {
        super.checkPackageDefinition(pkg);
    }

    @Override
    public void checkSetFactory() {
        throw new SecurityException("Program cannot set socket factory");
    }

    @Override
    public void checkSecurityAccess(final String target) {
        super.checkSecurityAccess(target);
    }

    @Override
    public ThreadGroup getThreadGroup() {
        return super.getThreadGroup();
    }
}
