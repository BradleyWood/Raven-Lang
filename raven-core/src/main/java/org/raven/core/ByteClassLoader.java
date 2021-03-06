package org.raven.core;

import java.util.HashMap;
import java.util.Map;

public class ByteClassLoader extends ClassLoader {

    private final Map<String, byte[]> extraClassDefs;

    public ByteClassLoader(final ClassLoader parent, final Map<String, byte[]> extraClassDefs) {
        super(parent);
        this.extraClassDefs = new HashMap<>(extraClassDefs);
    }

    public void addDef(final String name, final byte[] def) {
        extraClassDefs.put(name, def);
    }

    @Override
    protected Class<?> findClass(final String name) {
        byte[] classBytes = this.extraClassDefs.remove(name);
        if (classBytes != null) {
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        return null;
    }

}