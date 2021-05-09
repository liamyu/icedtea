package com.liam.algorithms.sort;

import edu.princeton.cs.algs4.StdRandom;

/**
 * 在不完全排序的情况下快速找出第k小的元素.
 */
public class QuickSelect {

    private static int partition(Comparable[] a, int lo, int hi) {
        Comparable v = a[lo];
        int i = lo;
        int j = hi + 1;
        while (true) {
            while (less(a[++i], v)) if (i == hi) break;
            while (less(v, a[--j])) {}
            if (j <= i) break;
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    public static Comparable select(Comparable[] a, int k) {
        StdRandom.shuffle(a);
        int lo = 0;
        int hi = a.length - 1;
        while (hi > lo) {
            int j = partition(a, lo, hi);
            if (j == k) break;
            if (j < k) lo = j + 1;
            else hi = j - 1;
        }
        return a[k];
    }

    public static void main(String[] args) {
        Comparable[] a = new Comparable[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int k = 9;
        Comparable w = QuickSelect.select(a, k);
        System.out.println("第" + k + "小的元素是" + w);
    }
}
