package edu.umb.cs210.p6;

import dsa.DiGraph;
import dsa.LinkedQueue;
import dsa.SeparateChainingHashST;
import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

// An immutable data type for computing shortest common ancestors.
public class ShortestCommonAncestor {

    // Rooted directional acyclic graph
    DiGraph G;

    // Construct a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(DiGraph G) {
        if (G == null) {
            throw new NullPointerException("G is null");
        }
        Degrees degrees = new Degrees(G);
        if (!degrees.sinks().iterator().hasNext()) {
            throw new IllegalArgumentException("G not a rooted DAG");
        }

        // Initialize a defensive copy of he digraph
        this.G = defensiveCopy(G);
    }

    // Length of the shortest ancestral path between v and w.
    public int length(int v, int w) {
        if ((v < 0 || v > G.V()) && (w < 0 || w > G.V())) {
            throw new IndexOutOfBoundsException(
                    "\"v is out of bounds\" <OR> \"w is out of bounds\"");
        } else if (v < 0 || v > G.V()) {
            throw new IndexOutOfBoundsException("v is out of bounds");
        } else if (w < 0 || w > G.V()) {
            throw new IndexOutOfBoundsException("w is out of bounds");
        }

        // hst contains distances to reachable points
        SeparateChainingHashST<Integer, Integer> vHST = distFrom(v);
        SeparateChainingHashST<Integer, Integer> wHST = distFrom(w);

        int ancestor = ancestor(v, w);

        // Return sum of distance to shortest common ancestor
        return vHST.get(ancestor) + wHST.get(ancestor);
    }

    // Shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) {
        if ((v < 0 || v > G.V()) && (w < 0 || w > G.V())) {
            throw new IndexOutOfBoundsException(
                    "\"v is out of bounds\" <OR> \"w is out of bounds\"");
        } else if (v < 0 || v > G.V()) {
            throw new IndexOutOfBoundsException("v is out of bounds");
        } else if (w < 0 || w > G.V()) {
            throw new IndexOutOfBoundsException("w is out of bounds");
        }

        SeparateChainingHashST<Integer, Integer> vHST = distFrom(v);
        SeparateChainingHashST<Integer, Integer> wHST = distFrom(w);

        int ancestor = 0;
        int minDis = Integer.MAX_VALUE;

        // Find matching ancestors from vertexes v, w
        // Vertex containing shortest distance stored into ancestor
        for (int x : vHST.keys()) {
            if (wHST.contains(x)) {
                int dis = vHST.get(x) + wHST.get(x);
                if (minDis > dis) {
                    minDis = dis;
                    ancestor = x;
                }
            }
        }
        return ancestor;
    }

    // Length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> setA, Iterable<Integer> setB) {
        if (setA == null && setB == null) {
            throw new NullPointerException(
                    "\"A is null\" <OR> \"B is null\"");
        } else if (setA == null) {
            throw new NullPointerException("A is null");
        } else if (setB == null) {
            throw new NullPointerException("B is null");
        } else if (!setA.iterator().hasNext() && !setB.iterator().hasNext()) {
            throw new IllegalArgumentException(
                    "\"A is empty\" <OR> \"B is empty\"");
        } else if (!setA.iterator().hasNext()) {
            throw new IllegalArgumentException("A is empty");
        } else if (!setB.iterator().hasNext()) {
            throw new IllegalArgumentException("B is empty");
        }

        int[] triad = triad(setA, setB);

        SeparateChainingHashST<Integer, Integer> vHST = distFrom(triad[1]);
        SeparateChainingHashST<Integer, Integer> wHST = distFrom(triad[2]);

        return vHST.get(triad[0]) + wHST.get(triad[0]);
    }

    // A shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> setA, Iterable<Integer> setB) {
        if (setA == null && setB == null) {
            throw new NullPointerException(
                    "\"A is null\" <OR> \"B is null\"");
        } else if (setA == null) {
            throw new NullPointerException("A is null");
        } else if (setB == null) {
            throw new NullPointerException("B is null");
        } else if (!setA.iterator().hasNext() && !setB.iterator().hasNext()) {
            throw new IllegalArgumentException(
                    "\"A is empty\" <OR> \"B is empty\"");
        } else if (!setA.iterator().hasNext()) {
            throw new IllegalArgumentException("A is empty");
        } else if (!setB.iterator().hasNext()) {
            throw new IllegalArgumentException("B is empty");
        }

        int[] triad = triad(setA, setB);

        return triad[0];
    }

    // Helper: Return a map of vertices reachable from v and their 
    // respective shortest distances from v.
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {

        // hst <vertex syn_id, distance from v to vertex containing syn_id>
        SeparateChainingHashST<Integer, Integer> hst =
                new SeparateChainingHashST<>();
        LinkedQueue<Integer> toVisit = new LinkedQueue<Integer>();

        // Enqueue origin vertex & insert distance to self as 0
        toVisit.enqueue(v);
        hst.put(v, 0);

        // BFS from origin vertex, inserting vertex & distance into hst
        // at each reachable vertex, keeping only the shortest path
        while (!toVisit.isEmpty()) {
            int current = toVisit.dequeue();
            Iterable<Integer> neighbors = this.G.adj(current);

            // For each neighboring vertex, check if it has been previously
            // reached, else, enqueue that neighbor to visit and
            // insert that vertex into hst
            for (int neighbor : neighbors) {
                if (!hst.contains(neighbor)) {
                    toVisit.enqueue(neighbor);
                    // Get distance for new vertex from parent vertex
                    // and increment.
                    hst.put(neighbor, hst.get(current)+1);
                }
            }
        }
        return hst;
    }

    // Helper: Return an array consisting of a shortest common ancestor a 
    // of vertex subsets A and B, and vertex v from A and vertex w from B 
    // such that the path v-a-w is the shortest ancestral path of A and B.
    private int[] triad(Iterable<Integer> setA, Iterable<Integer> setB) {

        // triad[0] = sca of vertices v, w
        // triad[1] = v
        // triad[2] = w
        int[] triad = new int[3];
        int minDis = Integer.MAX_VALUE;

        // Compare all vertices of A with B
        // Store SCA between the two sets
        for (int a : setA) {
            for (int b : setB) {
                int dis = length(a, b);
                if (minDis > dis) {
                    minDis = dis;
                    triad[0] = ancestor(a, b);
                    triad[1] = a;
                    triad[2] = b;
                }
            }
        }
        return triad;
    }

    // helper method that produces a defensive copy of G
    private DiGraph defensiveCopy(DiGraph g) {

        DiGraph copy = new DiGraph(g.V());
        for (int from = 0; from < g.V(); from++) {
            for (int to : g.adj(from)) {
                copy.addEdge(from, to);
            }
        }
        return copy;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        DiGraph G = new DiGraph(in);
        in.close();
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
