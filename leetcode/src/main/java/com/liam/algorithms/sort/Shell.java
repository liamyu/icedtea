package com.liam.algorithms.sort;

import java.util.Arrays;

/**
 * 希尔排序.
 */
public class Shell {

    public static void sort(Comparable[] a) {
        int n = a.length;
        int h = 1;
        while (h < n / 3) h = h * 3 + 1;
        while (h >= 1) {
            for (int i = h; i < n; i++) {
                for (int j = i; j >= h && less(a[j], a[j - h]); j-=h) {
                    exch(a, j, j - h);
                }
            }
            h = h / 3;
        }
    }

    private static boolean less(Comparable one, Comparable another) {
        return one.compareTo(another) < 0;
    }

    private static void exch(Comparable[] a, int one, int another) {
        Comparable temp = a[one];
        a[one] = a[another];
        a[another] = temp;
    }

    public static void main(String[] args) {
        Comparable[] a = {1, 5, 3, 9, 13, 9, 8};
        Shell.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
