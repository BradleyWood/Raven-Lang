package org.toylang.core;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ByteClassLoader extends ClassLoader {

    private final Map<String, byte[]> extraClassDefs;

    public ByteClassLoader(URL[] urls, ClassLoader parent, Map<String, byte[]> extraClassDefs) {
        super(parent);
        this.extraClassDefs = new HashMap<>(extraClassDefs);
    }

    public void addDef(String name, byte[] def) {
        extraClassDefs.put(name, def);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        byte[] classBytes = this.extraClassDefs.remove(name);
        if (classBytes != null) {
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        return super.findClass(name);
    }

}