package edu.umb.cs210.p1;

import stdlib.StdOut;
import stdlib.StdRandom;
import stdlib.StdStats;

// Estimates percolation threshold for an N-by-N percolation system.
public class PercolationStats {
    // # of independent experiments
    int T;
    // array of ratio of open sites/total sites to percolate
    double[] p;

    // Performs T independent experiments (Monte Carlo simulations) on an
    // N-by-N grid.
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) throw new IllegalArgumentException();
        this.T = T;
        p = new double[T];
        for (int k = 0; k < T; k++) {
            Percolation perc = new Percolation(N);
            do {
                int i = StdRandom.uniform(N);
                int j = StdRandom.uniform(N);
                if (!perc.isOpen(i, j)) perc.open(i, j);
            } while (!perc.percolates());
            double d = N*N;
            double u = perc.numberOfOpenSites()/d;
            p[k] = u;
        }
    }

    // Returns sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(p);
    }

    // Returns sample standard deviation of percolation threshold.
    public double stddev() {
        return StdStats.stddev(p);
    }

    // Returns low endpoint of the 95% confidence interval.
    public double confidenceLow() {
        return mean() - (1.96*stddev())/(Math.sqrt(this.T));
    }

    // Returns high endpoint of the 95% confidence interval.
    public double confidenceHigh() {
        return mean() + (1.96*stddev())/(Math.sqrt(this.T));
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);

        StdOut.printf("mean           = %f\n", stats.mean());
        StdOut.printf("stddev         = %f\n", stats.stddev());
        StdOut.printf("confidenceLow  = %f\n", stats.confidenceLow());
        StdOut.printf("confidenceHigh = %f\n", stats.confidenceHigh());

    }
}

