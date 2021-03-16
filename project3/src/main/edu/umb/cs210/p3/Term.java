package edu.umb.cs210.p3;

import stdlib.In;
import stdlib.StdOut;

import java.util.Arrays;
import java.util.Comparator;

// An immutable data type that represents an autocomplete term: a query string 
// and an associated real-valued weight.
public class Term implements Comparable<Term> {
    private String query; // Query string
    private long weight; // Real-valued weight

    // Construct a term given the associated query string, having weight 0.
    public Term(String query) {
        if (query == null) throw new NullPointerException();
        this.query = query;
        this.weight = 0;
    }

    // Construct a term given the associated query string and weight.
    public Term(String query, long weight) {
        // Corner cases: query is null, weight is negative
        if (query == null) throw new NullPointerException();
        if (weight < 0) throw new IllegalArgumentException();
        this.query = query;
        this.weight = weight;
    }

    // Compare this term to that in lexicographic order by query and 
    // return a negative, zero, or positive integer based on whether this 
    // term is smaller, equal to, or larger than that term.
    public int compareTo(Term that) {
        // Lexicographically compares two strings returning -, 0, or +
        // if this query comes before, equal to, after that.query
        return this.query.compareTo(that.query);
    }

    // A reverse-weight comparator.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightOrder();
    }

    // Helper reverse-weight comparator.
    private static class ReverseWeightOrder implements Comparator<Term> {
        public int compare(Term v, Term w) {
            // Returns a -1, 0, or 1 if v.weight is larger, equals to
            // or smaller than w as to reverse weight, descending order
            if (v.weight > w.weight) return -1;
            if (v.weight == w.weight) return 0;
            if (v.weight < w.weight) return 1;
            // Return statement to allow compilation
            return 0;
        }
    }

    // A prefix-order comparator.
    public static Comparator<Term> byPrefixOrder(int r) {
        return new PrefixOrder(r);
    }

    // Helper prefix-order comparator.
    private static class PrefixOrder implements Comparator<Term> {
        private int r; // Prefix length

        // Construct a new PrefixOrder object
        PrefixOrder(int r) {
            // Corner case: negative r
            if (r < 0) throw new IllegalArgumentException();
            this.r = r;
        }

        public int compare(Term v, Term w) {
            // Substring a, b of length of smallest between r and query length
            String a, b;
            a = v.query.substring(0, Math.min(this.r, v.query.length()));
            b = w.query.substring(0, Math.min(this.r, w.query.length()));
            // Lexicographically compares two strings returning -, 0, or +
            // if prefix v comes before, equal to, or after prefix w
            return a.compareTo(b);
        }
    }

    // A string representation of this term.
    public String toString() {
        // Returns weight and query separated by a tab
        return String.format("%d\t%s", this.weight, this.query);
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        int k = Integer.parseInt(args[1]);
        In in = new In(filename);
        int N = in.readInt();
        Term[] terms = new Term[N];
        for (int i = 0; i < N; i++) {
            long weight = in.readLong(); 
            in.readChar(); 
            String query = in.readLine(); 
            terms[i] = new Term(query.trim(), weight); 
        }
        StdOut.printf("Top %d by lexicographic order:\n", k);
        Arrays.sort(terms);
        for (int i = 0; i < k; i++) {
            StdOut.println(terms[i]);
        }
        StdOut.printf("Top %d by reverse-weight order:\n", k);
        Arrays.sort(terms, Term.byReverseWeightOrder());
        for (int i = 0; i < k; i++) {
            StdOut.println(terms[i]);
        }
    }
}
