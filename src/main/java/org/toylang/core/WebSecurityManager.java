package org.toylang.core;

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
    public void checkPermission(Permission perm) {
        super.checkPermission(perm);
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        super.checkPermission(perm, context);
    }

    @Override
    public void checkCreateClassLoader() {
        throw new SecurityException("Program cannot cannot create class loader");
    }

    @Override
    public void checkAccess(Thread t) {
        super.checkAccess(t);
    }

    @Override
    public void checkAccess(ThreadGroup g) {
        super.checkAccess(g);
    }

    @Override
    public void checkExit(int status) {
        super.checkExit(status);
    }

    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("Program cannot call exec()");
    }

    @Override
    public void checkLink(String lib) {
        throw new SecurityException("Program cannot load dynamic link library");
    }

    @Override
    public void checkRead(FileDescriptor fd) {
        throw new SecurityException("Program cannot cannot read from file");
    }

    @Override
    public void checkRead(String file) {
        throw new SecurityException("Program cannot cannot read from file");
    }

    @Override
    public void checkRead(String file, Object context) {
        throw new SecurityException("Program cannot cannot read from file");
    }

    @Override
    public void checkWrite(FileDescriptor fd) {
        throw new SecurityException("Program cannot cannot write to file");
    }

    @Override
    public void checkWrite(String file) {
        throw new SecurityException("Program cannot cannot write to file: " + file);
    }

    @Override
    public void checkDelete(String file) {
        throw new SecurityException("Program cannot cannot delete file: " + file);
    }

    @Override
    public void checkConnect(String host, int port) {
        throw new SecurityException("Program cannot connect to " + host + ":" + port);
    }

    @Override
    public void checkConnect(String host, int port, Object context) {
        throw new SecurityException("Program cannot connect to " + host + ":" + port);
    }

    @Override
    public void checkListen(int port) {
        throw new SecurityException("Program cannot listen on port " + port);
    }

    @Override
    public void checkAccept(String host, int port) {
        throw new SecurityException("Program cannot accept connections");
    }

    @Override
    public void checkMulticast(InetAddress maddr) {
        throw new SecurityException("Program cannot multicast");
    }

    @Override
    public void checkPropertiesAccess() {
        throw new SecurityException("Program cannot access system properties");
    }

    @Override
    public void checkPropertyAccess(String key) {
        throw new SecurityException("Program cannot access system properties");
    }

    @Override
    public void checkPrintJobAccess() {
        throw new SecurityException("Program cannot print");
    }


    @Override
    public void checkPackageAccess(String pkg) {
        super.checkPackageAccess(pkg);
    }

    @Override
    public void checkPackageDefinition(String pkg) {
        super.checkPackageDefinition(pkg);
    }

    @Override
    public void checkSetFactory() {
        throw new SecurityException("Program cannot set socket factory");
    }

    @Override
    public void checkSecurityAccess(String target) {
        super.checkSecurityAccess(target);
    }

    @Override
    public ThreadGroup getThreadGroup() {
        return super.getThreadGroup();
    }
}
