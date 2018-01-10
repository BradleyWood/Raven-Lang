package org.toylang.core.wrappers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TDict extends TObject implements Map<TObject, TObject> {

    public static final TType TYPE = new TType(TType.class);

    private final HashMap<TObject, TObject> map = new HashMap<>();

    public TDict() {
    }

    @Override
    public Object coerce(Class clazz) {
        if (Map.class.isAssignableFrom(clazz)) {
            return map;
        }
        return super.coerce(clazz);
    }

    @Override
    public int coerceRating(Class clazz) {
        if (clazz.isAssignableFrom(Map.class)) {
            return COERCE_IDEAL;
        }
        return super.coerceRating(clazz);
    }

    @Override
    public TObject getType() {
        return TYPE;
    }

    @Override
    public TObject set(TObject index, TObject obj) {
        return put(index, obj);
    }

    @Override
    public TObject get(TObject obj) {
        return map.get(obj);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public TObject get(Object key) {
        return get(toToyLang(key));
    }

    @Override
    public TObject put(TObject key, TObject value) {
        map.put(toToyLang(key), toToyLang(value));
        return this;
    }

    @Override
    public TObject remove(Object key) {
        return map.remove(toToyLang(key));
    }

    @Override
    public void putAll(Map<? extends TObject, ? extends TObject> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<TObject> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<TObject> values() {
        return map.values();
    }

    @Override
    public Set<Entry<TObject, TObject>> entrySet() {
        return map.entrySet();
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
