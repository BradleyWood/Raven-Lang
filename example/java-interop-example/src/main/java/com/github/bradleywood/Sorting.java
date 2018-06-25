package com.github.bradleywood;

public class Sorting {

    public static int[] selectionSort(final int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < data.length; j++) {
                if (data[j] < data[min_idx]) {
                    min_idx = j;
                }
            }
            final int temp = data[min_idx];
            data[min_idx] = data[i];
            data[i] = temp;
        }
        // must return the data because we are working on a copy
        return data;
    }
}
