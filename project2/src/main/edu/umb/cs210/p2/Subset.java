package edu.umb.cs210.p2;

import stdlib.In;
import stdlib.StdOut;

public class Subset {
    protected static ResizingArrayRandomQueue<String> subset(String[] args) {
        ResizingArrayRandomQueue<String> q = new ResizingArrayRandomQueue<>();
        // args[1] carrying filename stored into filename
        String filename = args[1];
        // In objects allows for reading input from a file input
        In in = new In(filename);
        // enqueue all strings until file empty
        while (!in.isEmpty()) {
            q.enqueue(in.readString());
        }
        return q;
    }

    // Entry point. [DO NOT EDIT]
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        ResizingArrayRandomQueue<String> q = subset(args);
        for (int i = 0; i < k; i++) {
            StdOut.println(q.dequeue());
        }
    }
}
