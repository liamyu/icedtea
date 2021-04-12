package com.liam.algorithms.sort;

import java.util.Arrays;

public class Selection {

  public static void sort(Comparable[] a) {
    for (int i = 0; i < a.length; i++) {
      int min = i;
      for (int j = i + 1; j < a.length; j++) {
        if (less(a[j], a[min])) {
          min = j;
        }
      }
      exch(a, i, min);
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
    Selection.sort(a);
    System.out.println(Arrays.toString(a));
  }
}
