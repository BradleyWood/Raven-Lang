package org.toylang.core.wrappers;

import org.toylang.core.Hidden;

import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TObject implements Comparable<TObject> {

    private static final HashMap<Integer, JavaMethod> methodCache = new HashMap<>();

    @Hidden
    private TType type;
    @Hidden
    private Object obj = null;

    @Hidden
    protected TObject() {
    }

    @Hidden
    public TObject(Object obj) {
        this.obj = obj;
        this.type = new TType(obj.getClass());
    }

    @Hidden
    public TObject(TType type) {
        this.type = type;
    }

    @Hidden
    public void setType(TType type) {
        this.type = type;
    }

    public TObject getType() {
        return type;
    }

    @Hidden
    public boolean isTrue() {
        if (obj instanceof Boolean) {
            return (boolean) obj;
        }
        return false;
    }

    @Hidden
    public TObject set(TObject index, TObject obj) {
        throw new RuntimeException("Cannot set element in non list type: " + getType().toString());
    }

    @Hidden
    public TObject get(TObject obj) {
        throw new RuntimeException("Cannot get element from non list type: " + getType().toString());
    }

    @Hidden
    public TObject add(TObject obj) {
        throw new RuntimeException("Cannot add types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject sub(TObject obj) {
        throw new RuntimeException("Cannot subtract types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject mul(TObject obj) {
        throw new RuntimeException("Cannot multiply types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject div(TObject obj) {
        throw new RuntimeException("Cannot divide types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject mod(TObject obj) {
        throw new RuntimeException("Cannot mod types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject pow(TObject obj) {
        throw new RuntimeException("Cannot divide types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject GT(TObject obj) {
        throw new RuntimeException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject LT(TObject obj) {
        throw new RuntimeException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject GTE(TObject obj) {
        throw new RuntimeException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject LTE(TObject obj) {
        throw new RuntimeException("Cannot compare types '" + getType().toString() + "' and '" + obj.getType().toString() + "'");
    }

    @Hidden
    public TObject EQ(TObject obj) {
        if (this.obj != null && obj.obj != null) {
            return this.obj.equals(obj.obj) ? TBoolean.TRUE : TBoolean.FALSE;
        }
        return null;
    }

    @Hidden
    public TObject NE(TObject obj) {
        return EQ(obj).not();
    }

    public TObject put(TObject key, TObject value) {
        throw new RuntimeException("Cannot put " + key + ", " + value + " in non-map");
    }

    @Hidden
    public TObject not() {
        throw new RuntimeException("Cannot invert " + getType().toString());
    }

    @Hidden
    public TObject and(TObject b) {
        return (isTrue() && b.isTrue()) ? TBoolean.TRUE : TBoolean.FALSE;
    }

    @Hidden
    public TObject or(TObject b) {
        return (isTrue() || b.isTrue()) ? TBoolean.TRUE : TBoolean.FALSE;
    }

    public Integer toInt() {
        return null;
    }

    @Hidden
    public Byte toByte() {
        return null;
    }

    @Hidden
    public Short toShort() {
        return null;
    }

    @Hidden
    public Long toLong() {
        return null;
    }

    @Hidden
    public Float toFloat() {
        return null;
    }

    @Hidden
    public Double toDouble() {
        return null;
    }

    @Hidden
    public Boolean toBoolean() {
        throw new RuntimeException(this + " cannot be converted to boolean");
    }

    @Hidden
    public BigInteger toBigInt() {
        return null;
    }

    public int size() {
        return 0;
    }

    @Hidden
    public Object toObject() {
        return obj;
    }

    @Hidden
    public Object[] toArray() {
        throw new RuntimeException(this + " cannot be converted to array");
    }

    @Hidden
    public static TObject newObj(Class clazz, TObject params) {
        try {
            if (clazz.getAnnotationsByType(Hidden.class).length == 0) {
                for (Constructor<?> constructor : clazz.getConstructors()) {
                    if (constructor.getAnnotationsByType(Hidden.class).length > 0)
                        continue;
                    if (constructor.getParameterCount() == params.size()) {
                        Class<?>[] types = constructor.getParameterTypes();
                        Object[] pa = getParams(params, types);
                        if (pa == null)
                            continue;
                        for (int i = 0; i < pa.length; i++) {
                            //System.out.println(pa[i] + " " + pa[i].getClass() + " " + " : " + types[i] + " : " + params.get(new TInt(i)));
                        }
                        Object o = constructor.newInstance(pa);
                        if (TObject.class.isAssignableFrom(o.getClass()))
                            return (TObject) o;
                        return new TObject(o);
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Constructor " + clazz.getName() + " not found.");
    }

    @Hidden
    public void setField(String name, TObject value) {
        try {
            String[] names = name.split("\\.");
            Object o = obj;
            for (int i = 0; i < names.length - 1; i++) {
                o = getField(o.getClass(), names[i]);
            }
            Field f = o.getClass().getField(name);
            if (setField(value, o, f)) return;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(obj + " has no attribute " + name);
    }

    @Hidden
    public void setField(Class clazz, String name, TObject value) {
        try {
            String[] names = name.split("\\.");
            Object o = obj;
            for (int i = 0; i < names.length - 1; i++) {
                o = getField(o.getClass(), names[i]);
            }
            Field f = clazz.getField(name);
            if (setField(value, o, f)) return;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(obj + " has no attribute " + name);
    }

    @Hidden
    private boolean setField(TObject value, Object o, Field f) throws IllegalAccessException {
        if (f.getType().isAssignableFrom(TObject.class)) {
            f.setAccessible(true);
            f.set(o, value);
            return true;
        } else {
            Object[] javaValue = getParams(new TList().add(value), new Class<?>[]{f.getType()});
            if (javaValue != null) {
                f.set(o, javaValue[0]);
                return true;
            }
        }
        return false;
    }

    @Hidden
    public TObject getField(String name) {
        try {
            String[] names = name.split("\\.");
            if (names.length == 0)
                names = new String[]{name};


            for (String s : names) {
                System.out.println(name + " : " + s);
            }
            Object o = obj;
            for (int i = 0; i < names.length; i++) {
                Field f = o.getClass().getField(names[i]);
                if (f.getAnnotationsByType(Hidden.class) != null) {
                    f.setAccessible(true);
                    o = f.get(o);
                } else {
                    throw new RuntimeException("Field " + name + " is not accessible");
                }
            }
            return toToyLang(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(obj + " has no attribute " + name);
    }

    @Hidden
    public static TObject getField(Class clazz, String name) {
        try {
            String[] names = name.split("\\.");
            if (names.length == 0)
                names = new String[]{name};

            Field f = clazz.getField(names[0]);
            f.setAccessible(true);
            if (f.getAnnotationsByType(Hidden.class) == null || !Modifier.isStatic(f.getModifiers()))
                throw new RuntimeException("Cannot find field");
            Object o = f.get(null);
            if (names.length == 1)
                return toToyLang(o);
            return new TObject(o).getField(name.substring(names[0].length()));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Attribute is not accessible: " + clazz.getName() + ":" + name);
    }

    @Hidden
    public final TObject invoke(String name, TObject params) {
        if (name.equals("getType"))
            return getType();
        if (obj == null)
            return invoke(this.getClass(), this, name, params);
        return invoke(obj.getClass(), obj, name, params);
    }

    @Hidden
    public static TObject invoke(Class clazz, String name, TObject params) {
        return invoke(clazz, null, name, params);
    }

    @Hidden
    public TObject invokeV(int hash, TObject params) {
        JavaMethod method = findMethod(hash);

        if (method != null) {
            Object[] pa = getParams(params, method.parameterTypes);
            try {
                return toToyLang(method.mh.invoke(this, pa));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return new TError("Error at call to " + this.getClass().getName() + "." + method.mh.getName() + "(" + params + ")");
            }
        }
        throw new RuntimeException("Method not found: " + hash + ":" + params);
    }

    @Hidden
    public static TObject invoke(int hash, TObject params) {
        JavaMethod jm = findMethod(hash);
        if (jm != null) {
            Object[] pa = getParams(params, jm.parameterTypes);
            try {
                return toToyLang(jm.mh.invoke(null, pa));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.getCause().printStackTrace();
                return new TError(e.getMessage());
            }
        }
        throw new RuntimeException("Method not found: " + hash);
    }

    @Hidden
    public static TObject invoke(Class clazz, Object obj, String name, TObject params) {
        JavaMethod method = findMethod(clazz, name, params);
        if (method != null) {
            try {
                Object[] pa = getParams(params, method.parameterTypes);
                Object ret = method.mh.invoke(obj, pa);

                return toToyLang(ret);
            } catch (Throwable e) {
                e.getCause().printStackTrace();
                return new TError(e.getMessage());
            }
        }
        throw new RuntimeException("Method " + clazz.getName() + ":" + name + " not found.");
    }

    @Hidden
    public static void registerMethod(Class clazz, String name, int paramCount) {
        int found = 0;
        Method m = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotationsByType(Hidden.class).length > 0 || method.getParameterCount() != paramCount)
                continue;
            if (method.getName().equals(name)) {
                found++;
                m = method;
            }
        }
        if (found != 1) {
            if (found > 1)
                throw new RuntimeException("Cannot register overloaded method: " + name);
            else
                throw new RuntimeException("Method not found: " + name);
        } else {
            int hash = Objects.hash(clazz.getName(), name, paramCount);
            methodCache.put(hash, new JavaMethod(m, m.getParameterTypes()));
        }
    }

    @Hidden
    private static JavaMethod findMethod(int hash) {
        return methodCache.get(hash);
    }

    @Hidden
    private static JavaMethod findMethod(Class clazz, String name, TObject params) {
        int hash = Objects.hash(clazz, name, params.size());
        JavaMethod m = methodCache.get(hash);
        if (m != null) {
            return m;
        }
        LinkedList<Method> methods = new LinkedList<>();
        Method found = null;
        for (Method method : getAllMethods(clazz, false, false)) {
            if (method.getAnnotationsByType(Hidden.class).length > 0 || method.getParameterCount() != params.size())
                continue;
            if (method.getName().equals(name)) {
                methods.add(method);
                Class<?>[] types = method.getParameterTypes();
                Object[] pa = getParams(params, types);
                if (pa == null)
                    continue;
                found = method;
            }
        }
        if (found == null)
            return null;

        JavaMethod jm = new JavaMethod(found, found.getParameterTypes());
        // MethodHandle methodHandle = mhLookup.unreflect(found);
        // dont put overloaded functions into the map!
        if (methods.size() == 1) {
            methodCache.put(hash, jm);
        }
        found.setAccessible(true);
        return jm;
    }

    public static Collection<Method> getAllMethods(Class clazz,
                                                   boolean includeAllPackageAndPrivateMethodsOfSuperclasses,
                                                   boolean includeOverridenAndHidden) {

        Predicate<Method> include = m -> !m.isBridge() && !m.isSynthetic() &&
                Character.isJavaIdentifierStart(m.getName().charAt(0))
                && m.getName().chars().skip(1).allMatch(Character::isJavaIdentifierPart);

        Set<Method> methods = new LinkedHashSet<>();
        Collections.addAll(methods, clazz.getMethods());
        methods.removeIf(include.negate());
        Stream.of(clazz.getDeclaredMethods()).filter(include).forEach(methods::add);

        final int access = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

        Package p = clazz.getPackage();
        if (!includeAllPackageAndPrivateMethodsOfSuperclasses) {
            int pass = includeOverridenAndHidden ?
                    Modifier.PUBLIC | Modifier.PROTECTED : Modifier.PROTECTED;
            include = include.and(m -> {
                int mod = m.getModifiers();
                return (mod & pass) != 0
                        || (mod & access) == 0 && m.getDeclaringClass().getPackage() == p;
            });
        }
        if (!includeOverridenAndHidden) {
            Map<Object, Set<Package>> types = new HashMap<>();
            final Set<Package> pkgIndependent = Collections.emptySet();
            for (Method m : methods) {
                int acc = m.getModifiers() & access;
                if (acc == Modifier.PRIVATE) continue;
                if (acc != 0) types.put(methodKey(m), pkgIndependent);
                else types.computeIfAbsent(methodKey(m), x -> new HashSet<>()).add(p);
            }
            include = include.and(m -> {
                int acc = m.getModifiers() & access;
                return acc != 0 ? acc == Modifier.PRIVATE
                        || types.putIfAbsent(methodKey(m), pkgIndependent) == null :
                        noPkgOverride(m, types, pkgIndependent);
            });
        }
        for (clazz = clazz.getSuperclass(); clazz != null; clazz = clazz.getSuperclass())
            Stream.of(clazz.getDeclaredMethods()).filter(include).forEach(methods::add);
        return methods;
    }

    static boolean noPkgOverride(
            Method m, Map<Object, Set<Package>> types, Set<Package> pkgIndependent) {
        Set<Package> pkg = types.computeIfAbsent(methodKey(m), key -> new HashSet<>());
        return pkg != pkgIndependent && pkg.add(m.getDeclaringClass().getPackage());
    }

    private static Object methodKey(Method m) {
        return Arrays.asList(m.getName(),
                MethodType.methodType(m.getReturnType(), m.getParameterTypes()));
    }

    @Hidden
    private static Object[] getParams(TObject params, Class<?>[] types) {
        if (!(params instanceof TList))
            return null;

        List<TObject> lst = ((TList) params).getList();

        Object[] pa = new Object[params.size()];
        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            if (lst.get(i) instanceof TNull) {
                pa[i] = null;
                continue;
            }
            try {
                if (type.isAssignableFrom(TObject.class)) {
                    pa[i] = toToyLang(lst.get(i));
                } else if (type.getName().equals(TObject.class.getName())) {
                    pa[i] = lst.get(i);
                } else if (type == int.class) {
                    pa[i] = lst.get(i).toInt();
                } else if (type == long.class) {
                    pa[i] = lst.get(i).toLong();
                } else if (type == short.class) {
                    pa[i] = lst.get(i).toShort();
                } else if (type == byte.class) {
                    pa[i] = lst.get(i).toByte();
                } else if (type == float.class) {
                    pa[i] = lst.get(i).toFloat();
                } else if (type == double.class) {
                    pa[i] = lst.get(i).toDouble();
                } else if (type == boolean.class) {
                    pa[i] = lst.get(i).toBoolean();
                } else if (type == BigInteger.class) {
                    pa[i] = lst.get(i).toBigInt();
                } else if (type == Object[].class) {
                    pa[i] = lst.toArray();
                } else {
                    pa[i] = lst.get(i).toObject();
                    if (!type.isAssignableFrom(pa[i].getClass())) {
                        return null;
                    }
                }
            } catch (Exception e) {
                return null;
            }
            if (pa[i] == null)
                return null;
        }
        return pa;
    }

    @Hidden
    public static TObject toToyLang(Object o) {
        if (o instanceof TObject)
            return (TObject) o;
        if (o instanceof BigInteger) {
            return new TBigInt((BigInteger) o);
        }
        if (o instanceof Integer || o instanceof Long || o instanceof Short || o instanceof Byte) {
            return new TInt((int) o);
        }
        if (o instanceof Float || o instanceof Double) {
            return new TReal((double) o);
        }
        if (o instanceof Boolean) {
            return ((boolean) o) ? TBoolean.TRUE : TBoolean.FALSE;
        }
        if (o instanceof String) {
            return new TString((String) o);
        }
        if (o == null) {
            return TNull.NULL;
        }
        if (o instanceof List) {
            return new TList((List) o);
        }
        if (o.getClass().isArray()) {
            TList lst = new TList();
            Object[] objA = (Object[]) o;
            lst.addAll(Arrays.asList(objA));
            return lst;
        }
        if (TObject.class.isAssignableFrom(o.getClass()))
            return (TObject) o;
        return new TObject(o);
    }

    @Override
    public String toString() {
        return (obj != null ? obj.toString() : "[INVALID-OBJECT]");
    }

    @Override
    public int compareTo(TObject o) {
        if (obj instanceof Comparable && o.obj instanceof Comparable) {
            return ((Comparable) obj).compareTo(o.obj);
        }
        throw new RuntimeException("Attempted to compare non comparable objects");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TObject toyObject = (TObject) o;

        if (type != null ? !type.equals(toyObject.type) : toyObject.type != null) return false;
        return obj != null ? obj.equals(toyObject.obj) : toyObject.obj == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (obj != null ? obj.hashCode() : 0);
        return result;
    }

    private static class JavaMethod {
        Method mh;
        Class<?>[] parameterTypes;

        public JavaMethod(Method mh, Class<?>[] parameterTypes) {
            this.mh = mh;
            this.parameterTypes = parameterTypes;
        }
    }
}
