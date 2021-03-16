package edu.umb.cs210.p1;

import stdlib.StdArrayIO;
import stdlib.StdOut;

public class Distance {
    // Returns the Euclidean distance between the position vectors x and y.
    protected static double distance(double[] x, double[] y) {
        // For each 0 <= i < x.length, add the square of
        // (x[i] - y[i]) to distance. At the end return
        // sqrt(distance).
        double dis = 0;
        for (int i = 0; i < x.length; i++)
        {
            dis = dis + (x[i] - y[i]) * (x[i] - y[i]);
        }
        return Math.sqrt(dis);
    }

    // Entry point. [DO NOT EDIT]
    public static void main(String[] args) {
        double[] x = StdArrayIO.readDouble1D();
        double[] y = StdArrayIO.readDouble1D();
        StdOut.println(distance(x, y));
    }
}
