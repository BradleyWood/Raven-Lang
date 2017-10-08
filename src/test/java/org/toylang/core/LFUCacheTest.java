package org.toylang.core;


import org.junit.Test;


import static org.junit.Assert.*;

public class LFUCacheTest {

    @Test
    public void test1() {
        LFUCache<Integer> cache = new LFUCache<>();

        for(int i = 0; i < 1002; i++) {
            cache.put((Integer)i, (Integer) i);
        }
        assertTrue(cache.size() == 1000);
        assertTrue(cache.isFull());
        assertTrue(cache.get(999) == 999);
        assertTrue(cache.get(2) == 2);
        assertTrue(cache.get(1) == null);
    }
}
