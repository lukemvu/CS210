package edu.umb.cs210.p6;

import dsa.BreadthFirstPaths;
import dsa.Graph;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;
// import stdlib.StdStats;

public class GraphProperties {
    private int[] eccentricities;
    private int diameter;
    private int radius;
    private LinkedQueue<Integer> centers;

    // Calculate graph properties for the graph G.
    public GraphProperties(Graph G) {
// *******YOU DO NOT NEED TO CHECK THIS CORNER CASE:
//      throw new IllegalArgumentException("G is not connected");
// ****** Ignore the corner case requirement for this problem ***************

        eccentricities = new int[G.V()];
        diameter = Integer.MIN_VALUE;
        radius = Integer.MAX_VALUE;

        // Calculate eccentricities of each vertex
        for (int i = 0; i < eccentricities.length; i++) {
            int maxDis = Integer.MIN_VALUE;
            BreadthFirstPaths bfs = new BreadthFirstPaths(G, i);
            for (int j = 0; j < eccentricities.length; j++) {
                if (i != j && bfs.hasPathTo(j)) {
                    int dis = bfs.distTo(j);
                    if (dis > maxDis)
                        maxDis = dis;
                }
            }
            eccentricities[i] = maxDis;
        }

        // Calculate diameter as max of eccentricities
        for (int i : eccentricities) {
            if (i > diameter) diameter = i;
        }

        // Calculate radius as min of eccentricities
        for (int i : eccentricities) {
            if (i < radius) radius = i;
        }
    }
    
    // Eccentricity of v.
    public int eccentricity(int v) {
        return eccentricities[v];
    }

    // Diameter of G.
    public int diameter() {
        return diameter;
    }

    // Radius of G.
    public int radius() {
        return radius;
    }

    // Centers of G.
    public Iterable<Integer> centers() {
        LinkedQueue<Integer> c = new LinkedQueue<>();
        for (int i = 0; i < eccentricities.length; i++) {
            if (eccentricities[i] == radius()) c.enqueue(i);
        }
        return c;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        GraphProperties gp = new GraphProperties(G);
        StdOut.println("Diameter = " + gp.diameter());
        StdOut.println("Radius   = " + gp.radius());
        StringBuilder centers = new StringBuilder();
        for (int v : gp.centers()) centers.append(v).append(" ");
        StdOut.println("Centers  = " + centers.toString());
    }
}
