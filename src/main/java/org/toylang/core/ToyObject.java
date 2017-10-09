package org.toylang.core;

import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ToyObject implements Comparable<ToyObject> {

    private static final HashMap<Integer, JavaMethod> methodCache = new HashMap<>();

    @Hidden
    private ToyType type;
    @Hidden
    private Object obj = null;

    @Hidden
    protected ToyObject() {
    }
    @Hidden
    public ToyObject(Object obj) {
        this.obj = obj;
        this.type = new ToyType(obj.getClass());
    }
    @Hidden
    public ToyObject(ToyType type) {
        this.type = type;
    }
    @Hidden
    public void setType(ToyType type) {
        this.type = type;
    }
    public ToyObject getType() {
        return type;
    }
    public boolean isTrue() {
        return false;
    }
    public ToyObject set(ToyObject index, ToyObject obj) {
        return null;
    }
    public ToyObject get(ToyObject obj) {
        return null;
    }
    public ToyObject add(ToyObject obj) {
        return null;
    }
    public ToyObject sub(ToyObject obj) {
        return null;
    }
    public ToyObject mul(ToyObject obj) {
        return null;
    }
    public ToyObject div(ToyObject obj) {
        return null;
    }
    public ToyObject mod(ToyObject obj) {
        return null;
    }
    public ToyObject pow(ToyObject obj) {
        return null;
    }
    public ToyObject GT(ToyObject obj) {
        return null;
    }
    public ToyObject LT(ToyObject obj) {
        return null;
    }
    public ToyObject GTE(ToyObject obj) {
        return null;
    }
    public ToyObject LTE(ToyObject obj) {
        return null;
    }
    public ToyObject EQ(ToyObject obj) {
        return null;
    }
    public ToyObject NE(ToyObject obj) {
        return null;
    }
    public ToyObject put(ToyObject key, ToyObject value) {
        return null;
    }
    public ToyObject not() {
        return null;
    }
    @Hidden
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
        throw new RuntimeException(this+" cannot be converted to boolean");
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
        throw new RuntimeException(this+" cannot be converted to array");
    }
    @Hidden
    public static ToyObject newObj(Class clazz, ToyObject params) {
        try {
            if(clazz.getAnnotationsByType(Hidden.class).length == 0) {
                for (Constructor<?> constructor : clazz.getConstructors()) {
                    if(constructor.getAnnotationsByType(Hidden.class).length > 0)
                        continue;
                    if (constructor.getParameterCount() == params.size()) {
                        Class<?>[] types = constructor.getParameterTypes();
                        Object[] pa = getParams(params, types);
                        if (pa == null)
                            continue;
                        for (int i = 0; i < pa.length; i++) {
                            //System.out.println(pa[i] + " " + pa[i].getClass() + " " + " : " + types[i] + " : " + params.get(new ToyInt(i)));
                        }
                        Object o = constructor.newInstance(pa);
                        if (ToyObject.class.isAssignableFrom(o.getClass()))
                            return (ToyObject) o;
                        return new ToyObject(o);
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Constructor " + clazz.getName() + " not found.");
    }
    @Hidden
    public void setField(String name, ToyObject value) {
        try {
            String[] names = name.split("\\.");
            Object o = obj;
            for(int i = 0; i < names.length - 1; i ++) {
                o = getField(o.getClass(), names[i]);
            }
            Field f = o.getClass().getField(name);
            if(f.getType().isAssignableFrom(ToyObject.class)) {
                f.setAccessible(true);
                f.set(o, value);
                return;
            } else {
                Object[] javaValue = getParams(new ToyList().add(value), new Class<?>[] {f.getType()});
                if(javaValue != null) {
                    f.set(o, javaValue[0]);
                    return;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(obj + " has no attribute " + name);
    }
    @Hidden
    public ToyObject getField(String name) {
        try {
            String[] names = name.split("\\.");
            if(names.length == 0)
                names = new String[] { name };


            for (String s : names) {
                System.out.println(name + " : "+s);
            }
            Object o = obj;
            for(int i = 0; i < names.length; i++) {
                Field f = o.getClass().getField(names[i]);
                if(f.getAnnotationsByType(Hidden.class) != null) {
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
        throw new RuntimeException(obj+" has no attribute "+name);
    }
    @Hidden
    public static ToyObject getField(Class clazz, String name) {
        try {
            String[] names = name.split("\\.");
            if(names.length == 0)
                names = new String[] {name};

            Field f = clazz.getField(names[0]);
            f.setAccessible(true);
            if(f.getAnnotationsByType(Hidden.class) == null || !Modifier.isStatic(f.getModifiers()))
                throw new RuntimeException("Cannot find field");
            Object o = f.get(null);
            if(names.length == 1)
                return toToyLang(o);
            return new ToyObject(o).getField(name.substring(names[0].length()));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Attribute is not accessible: "+clazz.getName()+":"+name);
    }
    @Hidden
    public final ToyObject invoke(String name, ToyObject params) {
        if(name.equals("getType"))
            return getType();
        if(obj == null)
            return invoke(this.getClass(), this,  name, params);
        return invoke(obj.getClass(), obj,  name, params);
    }
    @Hidden
    public static ToyObject invoke(Class clazz, String name, ToyObject params) {
        return invoke(clazz, null, name, params);
    }
    @Hidden
    public ToyObject invokeV(int hash, ToyObject params) {
        JavaMethod method = findMethod(hash);

        if(method != null) {
            Object[] pa = getParams(params, method.parameterTypes);
            try {
                method.mh.invoke(this, pa);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return new ToyError("Error at call to "+this.getClass().getName()+"."+method.mh.getName()+"("+params+")");
            }
        }
        throw new RuntimeException("Method not found: "+hash+":"+params);
    }
    public static ToyObject invoke(int hash, ToyObject params) {
        JavaMethod jm = findMethod(hash);
        if(jm != null) {
            Object[] pa = getParams(params, jm.parameterTypes);
            try {
                return toToyLang(jm.mh.invoke(null, pa));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return new ToyError(e.getMessage());
            }
        }
        throw new RuntimeException("Method not found: "+hash);
    }
    @Hidden
    public static ToyObject invoke(Class clazz, Object obj, String name, ToyObject params) {
        JavaMethod method = findMethod(clazz, name, params);
        if(method != null) {
            try {
                Object[] pa = getParams(params, method.parameterTypes);
                Object ret = method.mh.invoke(obj, pa);

                return toToyLang(ret);
            } catch (Throwable e) {
                e.printStackTrace();
                return new ToyError(e.getMessage());
            }
        }
        throw new RuntimeException("Method " + clazz.getName() + ":" + name + " not found.");
    }
    @Hidden
    public static void registerMethod(Class clazz, String name, int paramCount) {
        int found = 0;
        Method m = null;
        for (Method method : clazz.getDeclaredMethods()) {
            if(method.getAnnotationsByType(Hidden.class).length > 0 || method.getParameterCount() != paramCount)
                continue;
            if(method.getName().equals(name)) {
                found++;
                m = method;
            }
        }
        if(found != 1) {
            if(found > 1)
                throw new RuntimeException("Cannot register overloaded method: "+name);
            else
                throw new RuntimeException("Method not found: "+name);
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
    private static JavaMethod findMethod(Class clazz, String name, ToyObject params) {
        int hash = Objects.hash(clazz, name, params.size());
        JavaMethod m = methodCache.get(hash);
        if(m != null) {
            return m;
        }
        LinkedList<Method> methods = new LinkedList<>();
        Method found = null;
        for (Method method : getAllMethods(clazz, false, false)) {
            if(method.getAnnotationsByType(Hidden.class).length > 0 || method.getParameterCount() != params.size())
                continue;
            if(method.getName().equals(name)) {
                methods.add(method);
                Class<?>[] types = method.getParameterTypes();
                Object[] pa = getParams(params, types);
                if (pa == null)
                    continue;
                found = method;
            }
        }
        if(found == null)
            return null;

        JavaMethod jm = new JavaMethod(found, found.getParameterTypes());
        // MethodHandle methodHandle = mhLookup.unreflect(found);
        // dont put overloaded functions into the map!
        if(methods.size() == 1) {
            methodCache.put(hash, jm);
        }
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

        final int access=Modifier.PUBLIC|Modifier.PROTECTED|Modifier.PRIVATE;

        Package p = clazz.getPackage();
        if(!includeAllPackageAndPrivateMethodsOfSuperclasses) {
            int pass = includeOverridenAndHidden?
                    Modifier.PUBLIC|Modifier.PROTECTED: Modifier.PROTECTED;
            include = include.and(m -> { int mod = m.getModifiers();
                return (mod&pass)!=0
                        || (mod&access)==0 && m.getDeclaringClass().getPackage()==p;
            });
        }
        if(!includeOverridenAndHidden) {
            Map<Object,Set<Package>> types = new HashMap<>();
            final Set<Package> pkgIndependent = Collections.emptySet();
            for(Method m: methods) {
                int acc=m.getModifiers()&access;
                if(acc==Modifier.PRIVATE) continue;
                if(acc!=0) types.put(methodKey(m), pkgIndependent);
                else types.computeIfAbsent(methodKey(m),x->new HashSet<>()).add(p);
            }
            include = include.and(m -> { int acc = m.getModifiers()&access;
                return acc!=0? acc==Modifier.PRIVATE
                        || types.putIfAbsent(methodKey(m), pkgIndependent)==null:
                        noPkgOverride(m, types, pkgIndependent);
            });
        }
        for(clazz=clazz.getSuperclass(); clazz!=null; clazz=clazz.getSuperclass())
            Stream.of(clazz.getDeclaredMethods()).filter(include).forEach(methods::add);
        return methods;
    }
    static boolean noPkgOverride(
            Method m, Map<Object,Set<Package>> types, Set<Package> pkgIndependent) {
        Set<Package> pkg = types.computeIfAbsent(methodKey(m), key -> new HashSet<>());
        return pkg!=pkgIndependent && pkg.add(m.getDeclaringClass().getPackage());
    }
    private static Object methodKey(Method m) {
        return Arrays.asList(m.getName(),
                MethodType.methodType(m.getReturnType(), m.getParameterTypes()));
    }
    @Hidden
    private static Object[] getParams(ToyObject params, Class<?>[] types) {
        if(!(params instanceof ToyList))
            return null;

        List<ToyObject> lst = ((ToyList)params).getList();

        Object[] pa = new Object[params.size()];
        for(int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            if(lst.get(i) instanceof ToyNull) {
                pa[i] = null;
                continue;
            }
            try {
                if (type.isAssignableFrom(ToyObject.class)) {
                    pa[i] = toToyLang(lst.get(i));
                } else if (type.getName().equals(ToyObject.class.getName())) {
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
                } else {
                    pa[i] = lst.get(i).toObject();
                    if (!type.isAssignableFrom(pa[i].getClass())) {
                        return null;
                    }
                }
            } catch (Exception e) {
                return null;
            }
            if(pa[i] == null)
                return null;
        }
        return pa;
    }
    @Hidden
    public static ToyObject toToyLang(Object o) {
        if(o instanceof ToyObject)
            return (ToyObject) o;

        if(o instanceof Integer || o instanceof Long || o instanceof Short || o instanceof Byte) {
            return new ToyInt((int) o);
        }
        if(o instanceof Float || o instanceof Double) {
            return new ToyReal((double) o);
        }
        if(o instanceof Boolean) {
            return ((boolean)o) ? ToyBoolean.TRUE : ToyBoolean.FALSE;
        }
        if(o instanceof String) {
            return new ToyString((String) o);
        }
        if(o == null) {
            return ToyNull.NULL;
        }
        if(o instanceof List) {
            return new ToyList((List)o);
        }
        if(o.getClass().isArray()) {
            ToyList lst = new ToyList();
            Object[] objA = (Object[])o;
            lst.addAll(Arrays.asList(objA));
            return lst;
        }
        if(ToyObject.class.isAssignableFrom(o.getClass()))
            return (ToyObject) o;
        return new ToyObject(o);
    }

    @Override
    public String toString() {
        return (obj != null ? obj.toString() : "[INVALID-OBJECT]");
    }
    @Override
    public int compareTo(ToyObject o) {
        if(obj instanceof Comparable && o.obj instanceof Comparable) {
            return ((Comparable)obj).compareTo(o.obj);
        }
        throw new RuntimeException("Attempted to compare non comparable objects");
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToyObject toyObject = (ToyObject) o;

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
