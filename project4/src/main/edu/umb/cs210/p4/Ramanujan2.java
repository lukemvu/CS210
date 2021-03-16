package edu.umb.cs210.p4;

import dsa.MinPQ;
import stdlib.StdOut;

public class Ramanujan2 {
    // A data type that encapsulates a pair of numbers (i, j) 
    // and the sum of their cubes, ie, i^3 + j^3.
    private static class Pair implements Comparable<Pair> {
        private int i;          // first element of the pair
        private int j;          // second element of the pair
        private int sumOfCubes; // i^3 + j^3

        // Construct a pair (i, j).
        Pair(int i, int j) {
            this.i = i;
            this.j = j;
            sumOfCubes = i * i * i + j * j * j;
        }

        // Compare this pair to the other by sumOfCubes.
        public int compareTo(Pair other) {
            return sumOfCubes - other.sumOfCubes;
        }
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        MinPQ<Pair> pq = new MinPQ<Pair>();
        for (int i = 1; i*i*i < N; i++) {
            pq.insert(new Pair(i, i+1));
        }
        while (!pq.isEmpty()) {
            Pair temp = pq.delMin();
            if (pq.isEmpty()) break;
            if (temp.sumOfCubes == pq.min().sumOfCubes &&
                    temp.sumOfCubes <= N) {
                StdOut.printf("%d = %d^3 + %d^3 = %d^3 + %d^3\n",
                        temp.sumOfCubes, temp.i, temp.j,
                        pq.min().i, pq.min().j);
            }
            if (temp.j * temp.j * temp.j < N) pq.insert(new Pair(
                    temp.i, temp.j+1));
        }
    }
}
