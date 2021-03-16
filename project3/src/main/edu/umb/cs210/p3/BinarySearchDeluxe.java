package edu.umb.cs210.p3;

import stdlib.In;
import stdlib.StdOut;

import java.util.Arrays;
import java.util.Comparator;

// Implements binary search for clients that may want to know the index of 
// either the first or last key in a (sorted) collection of keys.
public class BinarySearchDeluxe {
    // The index of the first key in a[] that equals the search key, 
    // or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> c) {
        if (a == null || key == null || c == null) {
            throw new NullPointerException();
        }

        int low = 0; // Lower bound
        int high = a.length-1; // Upper bound
        int mid; // Middle index
        int index = -1; // Key index, -1 if key not found

        // Binary Search implementation
        // Exits when lower bound exceeds higher bound, key not found
        while (low <= high) {
            mid = (high - low)/2 + low;

            // Instead of returning index of key when found, sets index = mid
            // key == a[mid]
            if (c.compare(key, a[mid]) == 0) {
                index = mid;

                // Checks left of index for key
                // If not found, index is firstIndex
                // key != a[index-1]
                if (index == 0) return index;
                if (c.compare(key, a[index-1]) != 0) return index;
                // New upper bound at key index, firstIndex somewhere to left
                high = mid;

                // key < a[mid]
            } else if (c.compare(key, a[mid]) < 0) {
                high = mid - 1;

            } else {
                low = mid + 1;

            }
        }
        // return -1 when key isn't found
        return index;
    }

    // The index of the last key in a[] that equals the search key, 
    // or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> c) {
        if (a == null || key == null || c == null) {
            throw new NullPointerException();
        }
        int low = 0;
        int high = a.length-1;
        int mid;
        int index = -1;

        while (low <= high) {
            mid = (high - low)/2 + low;

            if (c.compare(key, a[mid]) == 0) {
                index = mid;

                // Checks right of index for key
                // If not found, index is lastIndex
                // key != a[index+1]
                if (index == a.length-1) return index;
                if (c.compare(key, a[index+1]) != 0) return index;
                // New lower bound at key index, lastIndex somewhere to right
                // Because integer division drops the decimal, mid may
                // get stuck in an infinite loop, +1 lets it increment
                // properly
                low = mid+1;

            } else if (c.compare(key, a[mid]) < 0) {
                high = mid - 1;

            } else {
                low = mid + 1;

            }
        }
        // return -1 when key isn't found
        return index;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        String prefix = args[1];
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong(); 
            in.readChar(); 
            String query = in.readLine(); 
            terms[i] = new Term(query.trim(), weight); 
        }
        Arrays.sort(terms);
        Term term = new Term(prefix);
        Comparator<Term> prefixOrder = Term.byPrefixOrder(prefix.length());
        int i = BinarySearchDeluxe.firstIndexOf(terms, term, prefixOrder);
        int j = BinarySearchDeluxe.lastIndexOf(terms, term, prefixOrder);
        int count = i == -1 && j == -1 ? 0 : j - i + 1;
        StdOut.println(count);
    }
}
