package org.toylang.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class LFUCache<T> {

    private final LinkedHashMap<Integer, Entry<T>> map;
    private final int capacity;

    public LFUCache() {
        this(1000);
    }

    public LFUCache(int capacity) {
        map = new LinkedHashMap<>(capacity);
        this.capacity = capacity;
    }

    public T get(Object key) {
        Entry<T> entry = map.get(key);
        if (entry != null)
            return entry.getData();
        return null;
    }

    public void put(Object key, T data) {
        if (!isFull()) {
            Entry<T> entry = new Entry<>(data);
            map.put(key.hashCode(), entry);
        } else {
            int minFreq = Integer.MAX_VALUE;
            Integer minKey = 0;
            for (Map.Entry<Integer, Entry<T>> entry : map.entrySet()) {
                if (entry.getValue().getFreq() < minFreq) {
                    minFreq = entry.getValue().getFreq();
                    minKey = entry.getKey();
                }
            }
            map.remove(minKey);
            put(key, data);
        }
    }

    public int size() {
        return map.size();
    }

    public boolean isFull() {
        return size() == getCapacity();
    }

    public int getCapacity() {
        return capacity;
    }

    private class Entry<T> {

        private T data;
        private int freq;

        private Entry(T data) {
            this.data = data;
            this.freq = 0;
        }

        public T getData() {
            return data;
        }

        public int getFreq() {
            return freq;
        }

        public void setData(T data) {
            this.data = data;
        }

        public void setFreq(int freq) {
            this.freq = freq;
        }
    }
}
