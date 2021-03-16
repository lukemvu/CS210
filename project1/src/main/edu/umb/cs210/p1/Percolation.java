package edu.umb.cs210.p1;

import dsa.WeightedQuickUnionUF;
import stdlib.In;
import stdlib.StdOut;

// Models an N-by-N percolation system.
public class Percolation {
    // Matrix size N
    public int N;
    // N-N 2D Grid
    public boolean[][] open;
    // Tracking # of openSites
    public int openSites;
    // Tracking connection from source to drain
    public WeightedQuickUnionUF uf;
    // Tracking connection to source only
    public WeightedQuickUnionUF uf2;

    // Creates an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
        if (N <= 0) throw new IllegalArgumentException();
        this.N = N;
        open = new boolean[N][N];
        openSites = 0;
        // Connection from source to drain
        uf = new WeightedQuickUnionUF(N*N+2);
        // Connection from source
        uf2 = new WeightedQuickUnionUF(N*N+1);
        // Top row sites connected to source
        for (int p = 1; p <= N; p++) {
            uf.union(0, p);
            uf2.union(0, p);
        }
        // Bottom row sites connected to drain
        for (int q = N*(N-1)+1; q <= N*N; q++) {
            uf.union(N*N+1, q);
        }
    }

    // Opens site (i, j) if it is not open already.
    public void open(int i, int j) {
        if (i < 0 || i > this.N-1 || j < 0 || j > this.N-1) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(i, j)) {
            open[i][j] = true;
            openSites++;
        }
        // North
        if  (i > 0 && isOpen(i-1, j)) {
            uf.union(encode(i, j), encode(i-1, j));
            uf2.union(encode(i, j), encode(i-1, j));
        }
        // East
        if (j < this.N-1 && isOpen(i, j+1)) {
            uf.union(encode(i, j), encode(i, j+1));
            uf2.union(encode(i, j), encode(i, j+1));
        }
        // West
        if (j > 0 && isOpen(i, j-1)) {
            uf.union(encode(i, j), encode(i, j-1));
            uf2.union(encode(i, j), encode(i, j-1));
        }
        // South
        if (i < this.N-1 && isOpen(i+1, j)) {
            uf.union(encode(i, j), encode(i+1, j));
            uf2.union(encode(i, j), encode(i+1, j));
        }
    }

    // Checks if site (i, j) is open.
    public boolean isOpen(int i, int j) {
        if (i < 0 || i > this.N-1 || j < 0 || j > this.N-1) {
            throw new IndexOutOfBoundsException();
        }
        return open[i][j];
    }

    // Checks if site (i, j) is full.
    public boolean isFull(int i, int j) {
        if (i < 0 || i > this.N-1 || j < 0 || j > this.N-1) {
            throw new IndexOutOfBoundsException();
        }
        return (isOpen(i, j) &&
                uf.connected(0, encode(i, j)) &&
                uf2.connected(0, encode(i, j)));
    }

    // Returns the number of open sites.
    public int numberOfOpenSites() {
        return openSites;
    }

    // Checks if the system percolates.
    public boolean percolates() {
        return uf.connected(0, this.N*this.N+1);
    }

    // Returns an integer ID (1...N) for site (i, j).
    protected int encode(int i, int j) {
        return this.N*i + j + 1;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        }
        else {
            StdOut.println("does not percolate");
        }

        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}