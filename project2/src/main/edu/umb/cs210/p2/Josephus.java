package edu.umb.cs210.p2;

import dsa.LinkedQueue;
import stdlib.StdOut;

public class Josephus {
    protected static LinkedQueue<Integer> calculateJosephus(String[] args) {
        // Get N and M from command line as ints.
        int N = Integer.parseInt(args[0]);
        int M = Integer.parseInt(args[1]);

        // Create a queue q and enqueue integers
        // 1, 2, ... N.
        LinkedQueue<Integer> q = new LinkedQueue<Integer>();
        for (int i = 1; i <= N; i++) {
            q.enqueue(i);
        }

        // Create a queue outQ to store the ordering
        LinkedQueue<Integer> outQ = new LinkedQueue<Integer>();

        int i = 0;
        // As long as q is not empty: increment i;
        // dequeue an element pos; if M divides i,
        // enqueue pos to outQ, otherwise enqueue pos to q.
        while (!q.isEmpty()) {
            i++;
            int pos = q.peek();
            q.dequeue();
            if (i % M == 0) {
                outQ.enqueue(pos);
            } else {
                q.enqueue(pos);
            }
        }

        // Return outQ
        return outQ;
    }

    // Entry point. [DO NOT EDIT]
    public static void main(String[] args) {
        for (int i : calculateJosephus(args)) {
            StdOut.println(i);
        }
    }
}
