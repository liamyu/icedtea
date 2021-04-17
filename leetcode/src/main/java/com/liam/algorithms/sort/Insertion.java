package com.liam.algorithms.sort;

import java.util.Arrays;

/**
 * 插入排序.
 */
public class Insertion {

    public static void sort(Comparable[] a) {
        int n = a.length;
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0 && less(a[j], a[j - 1]); j--) {
                exch(a, j, j - 1);
            }
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
        Insertion.sort(a);
        System.out.println(Arrays.toString(a));
    }
}
