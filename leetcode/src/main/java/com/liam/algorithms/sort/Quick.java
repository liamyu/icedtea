package com.liam.algorithms.sort;

import java.util.Arrays;

import edu.princeton.cs.algs4.StdRandom;

/**
 * 快速排序.
 */
public class Quick {

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
        assert isSorted(a);
    }

    public static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        int j = partition(a, lo, hi);
        sort(a, lo, j - 1);
        sort(a, j + 1, hi);
        assert isSorted(a, lo, hi);
    }

    public static int partition(Comparable[] a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];
        while (true) {
            while (less(a[++i], v)) {
                if (i == hi) break;
            }
            while (less(v, a[--j])) {}
            if (j <= i) break;
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }

    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i < hi; i++) {
            if (less(a[i], a[i - 1])) return false;
        }
        return true;
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void exch(Comparable[] a, int one, int another) {
        Comparable temp = a[one];
        a[one] = a[another];
        a[another] = temp;
    }

    public static void main(String[] args) {
        Comparable[] a = {3, 32, 24, 4356, 45, 224, 546, 45, 224, 43, 754, 65};
//        Comparable[] a = {0, 1};
        Quick.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
