package org.toylang.core;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

public class ToyObject implements Comparable<ToyObject> {

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
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
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
    public static ToyObject invoke(Class clazz, Object obj, String name, ToyObject params) {
        if(clazz != null && params instanceof ToyList) {
            for (Method method : clazz.getMethods()) {
                if(method.getAnnotationsByType(Hidden.class).length > 0)
                    continue;
                if(method.getName().equals(name) && method.getParameterCount() == params.size()) {
                    Class<?>[] types = method.getParameterTypes();
                    Object[]pa = getParams(params, types);
                    if(pa == null)
                        continue;
                    // invoke
                    try {
                        Object ret = method.invoke(obj, pa);
                        return toToyLang(ret);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new RuntimeException("Method " + clazz.getName() + ":" + name + " not found.");
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
            if(type.isAssignableFrom(ToyObject.class)) {
                pa[i] = toToyLang(lst.get(i));
            } else if(type.getName().equals(ToyObject.class.getName())) {
                pa[i] = lst.get(i);
            } else if(type == int.class) {
                pa[i] = lst.get(i).toInt();
            } else if(type == long.class) {
                pa[i] = lst.get(i).toLong();
            } else if(type == short.class) {
                pa[i] = lst.get(i).toShort();
            } else if(type == byte.class) {
                pa[i] = lst.get(i).toByte();
            } else if(type == float.class) {
                pa[i] = lst.get(i).toFloat();
            } else if(type == double.class) {
                pa[i] = lst.get(i).toDouble();
            } else if(type == boolean.class) {
                pa[i] = lst.get(i).toBoolean();
            } else {
                pa[i] = lst.get(i).toObject();
                if(!type.isAssignableFrom(pa[i].getClass())) {
                    return null;
                }
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
            return new ToyBoolean((boolean)o);
        }
        if(o instanceof String) {
            return new ToyString((String) o);
        }
        if(o == null) {
            return new ToyNull();
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
}
