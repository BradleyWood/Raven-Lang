package org.toylang.core;

public class Timer {

    public long start;

    public Timer() {
        start = System.currentTimeMillis();
    }

    public int elapsed() {
        return (int) (System.currentTimeMillis() - start);
    }

    public void reset() {
        start = System.currentTimeMillis();
    }
}
