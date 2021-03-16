package edu.umb.cs210.p5;

import dsa.LinkedQueue;
import dsa.MaxPQ;
import dsa.Point2D;
import dsa.RectHV;
import stdlib.StdIn;
import stdlib.StdOut;

public class KdTreePointST<Value> implements PointST<Value> {
    Node root;              // ref to root of 2d-tree
    int N;                  // nodes (key/val) pairs in tree
    
    // 2d-tree (generalization of a BST in 2d) representation.
    private class Node {
        private Point2D p;   // the point
        private Value val;   // the symbol table maps the point to this value
        private RectHV rect; // the axis-aligned rectangle corresponding to 
                             // this node
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree

        // Construct a node given the point, the associated value, and the 
        // axis-aligned rectangle corresponding to the node.
        Node(Point2D p, Value val, RectHV rect) {
            this.p = p;
            this.val = val;
            this.rect = rect;
        }
    }

    // Construct an empty symbol table of points.
    public KdTreePointST() {
        root = null;
        N = 0;
    }

    // Is the symbol table empty?
    public boolean isEmpty() { 
        return N == 0;
    }

    // Number of points in the symbol table.
    public int size() {
        return N;
    }

    // Associate the value val with point p.
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new NullPointerException("null argument");
        RectHV rtRect = new RectHV(
                Double.NEGATIVE_INFINITY,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY
        );
        // lr == true for root node
        root = put(root, p, val, rtRect, true);
    }

    // Helper for put(Point2D p, Value val).
    private Node put(Node x, Point2D p, Value val, RectHV rect, boolean lr) {
        // Empty spot, insert new node
        if (x == null) {
            N++;
            return new Node(p, val, rect);
        }
        // Update value if point exists
        if (p.equals(x.p)) {
            x.val = val;
        } else {
            // Recursive call to put() to traverse down ST
            if (lr) {
                // x-aligned node, lr = true
                if (p.x() < x.p.x()) {
                    // New point x-coordinate less than current node's
                    // x-coordinate. Traverse to left node. Pass new x-max
                    // coordinate to new rectangle from current node. Invert
                    // lr (node alignment)
                    RectHV newRect = new RectHV(
                            rect.xmin(),
                            rect.ymin(),
                            x.p.x(),
                            rect.ymax()
                    );
                    x.lb = put(x.lb, p, val, newRect, !lr);
                } else {
                    // New point x-coordinate greater than current node's
                    // x-coordinate. Traverse to right node. Pass new x-min
                    // coordinate to new rectangle from current node. Invert
                    // lr
                    RectHV newRect = new RectHV(
                            x.p.x(),
                            rect.ymin(),
                            rect.xmax(),
                            rect.ymax()
                    );
                    x.rt = put(x.rt, p, val, newRect, !lr);
                }
            } else {
                // y-aligned node, lr = false
                if (p.y() < x.p.y()) {
                    // New point y-coordinate less than current node's
                    // y-coordinate. Traverse to left node. Pass new y-max
                    // coordinate to new rectangle from current node. Invert
                    // lr
                    RectHV newRect = new RectHV(
                            rect.xmin(),
                            rect.ymin(),
                            rect.xmax(),
                            x.p.y()
                    );
                    x.lb = put(x.lb, p, val, newRect, !lr);
                } else {
                    // New point y-coordinate greater than current node's
                    // y-coordinate. Traverse to right node. Pass new y-min
                    // coordinate to new rectangle from current node. Invert
                    // lr
                    RectHV newRect = new RectHV(
                            rect.xmin(),
                            x.p.y(),
                            rect.xmax(),
                            rect.ymax()
                    );
                    x.rt = put(x.rt, p, val, newRect, !lr);
                }
            }
        }
        return x;
    }

    // Value associated with point p.
    public Value get(Point2D p) {
        if (p == null) throw new NullPointerException("null argument");
        return get(root, p, true);
    }

    // Helper for get(Point2D p).
    private Value get(Node x, Point2D p, boolean lr) {
        if (x == null) return null;
        if (p.equals(x.p)) return x.val;
        if (lr) {
            // x-aligned node, lr = true
            if (p.x() < x.p.x()) {
                // Traverse left comparing x
                return get(x.lb, p, !lr);
            } else {
                // Traverse right comparing x
                return get(x.rt, p, !lr);
            }
        } else {
            // y-aligned node, lr = false
            if (p.y() < x.p.y()) {
                // Traverse left comparing y
                return get(x.lb, p, !lr);
            } else {
                // Traverse right comparing y
                return get(x.rt, p, !lr);
            }
        }
    }

    // Does the symbol table contain the point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("null argument");
        return get(p) != null;
    }

    // All points in the symbol table, in level order.
    public Iterable<Point2D> points() {
        LinkedQueue<Node> nQ = new LinkedQueue<>();     // Tracks nodes
        LinkedQueue<Point2D> pQ = new LinkedQueue<>();  // Tracks points
        nQ.enqueue(root);                               // Start with root
        while (!nQ.isEmpty()) {
            Node temp = nQ.dequeue();                   // Remove root
            pQ.enqueue(temp.p);                         // Add point from root
            if (temp.lb != null) nQ.enqueue(temp.lb);   // Add left node
            if (temp.rt != null) nQ.enqueue(temp.rt);   // Add right node
        }
        return pQ;
    }

    // All points in the symbol table that are inside the rectangle rect.
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("null argument");
        // rQ will contain points that intersect rect
        LinkedQueue<Point2D> rQ = new LinkedQueue<>();
        range(root, rect, rQ);
        return rQ;
    }

    // Helper for public range(RectHV rect).
    private void range(Node x, RectHV rect, LinkedQueue<Point2D> q) {
        if (x == null) return;
        if (rect.intersects(x.rect)) {
            if (rect.contains(x.p)) q.enqueue(x.p);
            // Inserts point if it is contained within the rect
            // Recursive call to check left and right nodes.
            // If point not in range, return null, no need to check further
            // down that path.
            range(x.lb, rect, q);
            range(x.rt, rect, q);
        }
    }

    // A nearest neighbor to point p; null if the symbol table is empty.
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("null argument");
        Point2D nPt = null; // initialize nearest point
        double nDis = Double.POSITIVE_INFINITY; // initialize nearest distance
        nPt = nearest(root, p, nPt, nDis, true);
        return nPt;
    }
    
    // Helper for public nearest(Point2D p).
    private Point2D nearest(Node x, Point2D p, Point2D nearest, 
                            double nearestDistance, boolean lr) {

        if (x == null) return nearest;

        // Distance to query point p from x
        double dqp = x.p.distanceSquaredTo(p);

        // Distance to query point p from x rect
        double dqr = x.rect.distanceSquaredTo(p);

        if (!x.p.equals(p) && dqp < nearestDistance) {
            // key in x is diff from given key &&
            // squared dist between the two is smaller than nearestDistance
            nearestDistance = dqp;
            nearest = x.p; // nearest point is now point in node x
        }
        if (dqr < dqp) {
            // If distance to rectangle at the node is smaller
            // than nearest distance, possible point with small dist to target
            // 4 different cases
            if (lr) {
                // x-aligned
                if (p.x() < x.p.x()) {
                    nearest = nearest(x.lb, p, nearest, nearestDistance, !lr);
                    nearestDistance = nearest.distanceSquaredTo(p);
                    // If nearest distance is closer than distance to rectangle
                    // no need to check the other side of tree.
                    if (nearestDistance > dqr) {
                        nearest = nearest(
                                x.rt, p, nearest, nearestDistance, !lr);
                    }
                } else {
                    nearest = nearest(x.rt, p, nearest, nearestDistance, !lr);
                    nearestDistance = nearest.distanceSquaredTo(p);
                    if (nearestDistance > dqr) {
                        nearest = nearest(
                                x.lb, p, nearest, nearestDistance, !lr);
                    }
                }
            } else {
                // y-aligned
                if (p.y() < x.p.y()) {
                    nearest = nearest(x.lb, p, nearest, nearestDistance, !lr);
                    nearestDistance = nearest.distanceSquaredTo(p);
                    if (nearestDistance > dqr) {
                        nearest = nearest(
                                x.rt, p, nearest, nearestDistance, !lr);
                    }
                } else {
                    nearest = nearest(x.rt, p, nearest, nearestDistance, !lr);
                    nearestDistance = nearest.distanceSquaredTo(p);
                    if (nearestDistance > dqr) {
                        nearest = nearest(
                                x.lb, p, nearest, nearestDistance, !lr);
                    }
                }
            }
        }

        return nearest;
    }

    // k points that are closest to point p.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        if (p == null) throw new NullPointerException("null argument");
        MaxPQ<Point2D> mpq = new MaxPQ<Point2D>(p.distanceToOrder());
        nearest(root, p, k, mpq, true);
        return mpq;
    }

    // Helper for public nearest(Point2D p, int k).
    private void nearest(Node x, Point2D p, int k, MaxPQ<Point2D> pq, 
                         boolean lr) {
        if (x == null || pq.size() > k) return;
        if (!x.p.equals(p)) pq.insert(x.p);
        if (pq.size() > k) pq.delMax();
        nearest(x.lb, p, k, pq, !lr);
        nearest(x.rt, p, k, pq, !lr);
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        KdTreePointST<Integer> st = new KdTreePointST<Integer>();
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
