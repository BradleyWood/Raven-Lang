package org.toylang.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ToyDict extends ToyObject implements Map<ToyObject, ToyObject> {

    public static final ToyType TYPE = new ToyType(ToyType.class);

    private final HashMap<ToyObject, ToyObject> map = new HashMap<>();

    public ToyDict() {
    }
    @Override
    public ToyObject getType() {
        return super.getType();
    }
    @Override
    public ToyObject get(ToyObject obj) {
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
        return containsValue(value);
    }
    @Override
    public ToyObject get(Object key) {
        return get(toToyLang(key));
    }
    @Override
    public ToyObject put(ToyObject key, ToyObject value) {
        map.put(toToyLang(key), toToyLang(value));
        return this;
    }
    @Override
    public ToyObject remove(Object key) {
        return map.remove(toToyLang(key));
    }
    @Override
    public void putAll(Map<? extends ToyObject, ? extends ToyObject> m) {
        map.putAll(m);
    }
    @Override
    public void clear() {
        map.clear();
    }
    @Override
    public Set<ToyObject> keySet() {
        return map.keySet();
    }
    @Override
    public Collection<ToyObject> values() {
        return map.values();
    }
    @Override
    public Set<Entry<ToyObject, ToyObject>> entrySet() {
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
