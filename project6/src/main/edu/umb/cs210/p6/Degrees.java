package edu.umb.cs210.p6;

import dsa.DiGraph;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;

public class Degrees {
    private DiGraph G;              // Graph
    private int[] outdegree;        // Array of outdegrees
    private int[] indegree;         // Array of indegrees

    // Construct a Degrees object from a digraph G.
    public Degrees(DiGraph G) {
        if (G == null) throw new NullPointerException("null argument");

        setDegrees(G);
        this.G = G;
    }
    
    // Sources of G.
    public Iterable<Integer> sources() {
        LinkedQueue<Integer> sourcesq = new LinkedQueue<Integer>();
        for (int i = 0; i < indegree.length; i++) {
            if (indegree[i] == 0) {
                sourcesq.enqueue(i);
            }
        }
        return sourcesq;
    }

    // Sinks of G.
    public Iterable<Integer> sinks() {
        LinkedQueue<Integer> sinksq = new LinkedQueue<Integer>();
        for (int i = 0; i < outdegree.length; i++) {
            if (outdegree[i] == 0) {
                sinksq.enqueue(i);
            }
        }
        return sinksq;
    }

    // Is G a map?
    public boolean isMap() {
        for (int degree : outdegree) {
            if (degree != 1) return false;
        }
        return true;
    }

    // helper method calculates the in and out degrees of each vertex
    private void setDegrees(DiGraph g) {
        outdegree = new int[g.V()];
        indegree = new int[g.V()];
        for (int from = 0; from < g.V(); from++) {
            for (int to : g.adj(from)) {
                outdegree[from]++;
                indegree[to]++;
            }
        }
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        Degrees degrees = new Degrees(G);

        StringBuilder sources = new StringBuilder();
        StringBuilder   sinks = new StringBuilder();
        for (int v : degrees.sources()) sources.append(v).append(" ");
        for (int v : degrees.sinks())     sinks.append(v).append(" ");

        StdOut.println("Sources = " + sources.toString());
        StdOut.println("Sinks   = " + sinks.toString());
        StdOut.println("Is Map  = " + degrees.isMap());
    }
}
