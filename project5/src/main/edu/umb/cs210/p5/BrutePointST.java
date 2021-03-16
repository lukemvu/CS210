package edu.umb.cs210.p5;

import dsa.LinkedQueue;
import dsa.MinPQ;
import dsa.Point2D;
import dsa.RectHV;
import dsa.RedBlackBinarySearchTreeST;
import stdlib.StdIn;
import stdlib.StdOut;

public class BrutePointST<Value> implements PointST<Value> {
    // Declare bst of Point2D and Value
    RedBlackBinarySearchTreeST<Point2D, Value> bst;

    // Construct an empty symbol table of points.
    public BrutePointST() {
        bst = new RedBlackBinarySearchTreeST<Point2D, Value>();
    }

    // Is the symbol table empty?
    public boolean isEmpty() { 
        return bst.isEmpty();
    }

    // Number of points in the symbol table.
    public int size() {
        return bst.size();
    }

    // Associate the value val with point p.
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new NullPointerException("null argument");
        bst.put(p, val);
    }

    // Value associated with point p.
    public Value get(Point2D p) {
        if (p == null) throw new NullPointerException("null argument");
        return bst.get(p);
    }

    // Does the symbol table contain the point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("null argument");
        return bst.contains(p);
    }

    // All points in the symbol table.
    public Iterable<Point2D> points() {
        return bst.keys();
    }

    // All points in the symbol table that are inside the rectangle rect.
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("null argument");
        LinkedQueue<Point2D> pts = new LinkedQueue<>();
        // Iterate through all points
        // If point is contained in rect, enqueue into pt
        for (Point2D pt : this.points()) {
            if (rect.contains(pt)) {
                pts.enqueue(pt);
            }
        }
        return pts;
    }

    // A nearest neighbor to point p; null if the symbol table is empty.
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("null argument");
        if (this.isEmpty()) return null;
        Point2D smallestPt = null;
        double smallestDis = Double.POSITIVE_INFINITY;
        for (Point2D pt : this.points()) {
            double dis = p.distanceSquaredTo(pt);
            if (dis != 0 && dis < smallestDis) {
                smallestDis = dis;
                smallestPt = pt;
            }
        }
        return smallestPt;
    }

    // k points that are closest to point p.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        if (p == null) throw new NullPointerException("null argument");
        MinPQ<Point2D> pts = new MinPQ<Point2D>(p.distanceToOrder());
        for (Point2D pt : this.points()) {
            // Insert all points of tree into a MinPQ with distance
            // to target point as the comparator as long as
            // the target point wasn't the same point as the node points
            if (!p.equals(pt))
                pts.insert(pt);
        }
        MinPQ<Point2D> nearestPts = new MinPQ<Point2D>(p.distanceToOrder());
        for (int i = 0; i < k; i++) {
            // Insert k number of points onto another MinPQ
            // This MinPQ will contain the k nearest points to target point
            nearestPts.insert(pts.delMin());
        }
        return nearestPts;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        BrutePointST<Integer> st = new BrutePointST<Integer>();
        double qx = Double.parseDouble(args[0]);
        double qy = Double.parseDouble(args[1]);
        double rx1 = Double.parseDouble(args[2]);
        double rx2 = Double.parseDouble(args[3]);
        double ry1 = Double.parseDouble(args[4]);
        double ry2 = Double.parseDouble(args[5]);
        int k = Integer.parseInt(args[6]);
        Point2D query = new Point2D(qx, qy);
        RectHV rect = new RectHV(rx1, ry1, rx2, ry2);
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            st.put(p, i++);
        }
        StdOut.println("st.empty()? " + st.isEmpty());
        StdOut.println("st.size() = " + st.size());
        StdOut.println("First " + k + " values:");
        i = 0;
        for (Point2D p : st.points()) {
            StdOut.println("  " + st.get(p));
            if (i++ == k) {
                break;
            }
        }
        StdOut.println("st.contains(" + query + ")? " + st.contains(query));
        StdOut.println("st.range(" + rect + "):");
        for (Point2D p : st.range(rect)) {
            StdOut.println("  " + p);
        }
        StdOut.println("st.nearest(" + query + ") = " + st.nearest(query));
        StdOut.println("st.nearest(" + query + ", " + k + "):");
        for (Point2D p : st.nearest(query, k)) {
            StdOut.println("  " + p);
        }
    }
}
