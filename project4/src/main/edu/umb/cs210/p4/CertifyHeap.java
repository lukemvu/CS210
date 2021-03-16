
package edu.umb.cs210.p4;

import stdlib.StdIn;
import stdlib.StdOut;

public class CertifyHeap {
    // Return true of v is less than w and false otherwise.
    private static <T extends Comparable<T>> boolean less(T v, T w) {
        return (v.compareTo(w) < 0);
    }

    // Return true if a[] represents a maximum-ordered heap
    // and false otherwise.
    protected static <T extends Comparable<T>> boolean maxOrderedHeap(T[] a) {
        int N = a.length;

        // For each node 1 <= i <= N / 2, if i is less than
        // either of its children, return false, meaning a[]
        // does not represent a maximum-ordered heap.
        // Otherwise, return true.
        for (int i = 1; i <= (N-1)/2; i++) {
            if (less(a[i], a[i * 2])) return false;
            if (i*2+1 >= N) return true;
            if (less(a[i], a[i * 2 + 1])) return false;
        }
        return true;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        StdOut.println(maxOrderedHeap(a));
    }
}