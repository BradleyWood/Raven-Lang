package org.raven.core.wrappers;

import org.raven.core.Intrinsics;

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
    public Object coerce(final Class clazz) {
        if (Map.class.isAssignableFrom(clazz)) {
            final HashMap<Object, Object> ret = new HashMap<>();
            map.forEach((key, value) -> ret.put(key.toObject(), value.toObject()));
            return ret;
        }
        return super.coerce(clazz);
    }

    @Override
    public int coerceRating(final Class<?> clazz) {
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
    public TObject set(final TObject index, final TObject obj) {
        return put(index, obj);
    }

    @Override
    public TObject get(final TObject obj) {
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
    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return map.containsValue(value);
    }

    @Override
    public TObject get(final Object key) {
        return get(Intrinsics.wrap(key));
    }

    @Override
    public TObject put(final TObject key, final TObject value) {
        map.put(Intrinsics.wrap(key), Intrinsics.wrap(value));
        return this;
    }

    @Override
    public TObject remove(final Object key) {
        return map.remove(Intrinsics.wrap(key));
    }

    @Override
    public void putAll(final Map<? extends TObject, ? extends TObject> m) {
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
    public boolean equals(final Object o) {
        if (o == this)
            return true;
        return o instanceof TDict && ((TDict) o).map.equals(map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
