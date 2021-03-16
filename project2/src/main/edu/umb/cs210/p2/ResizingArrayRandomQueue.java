package edu.umb.cs210.p2;

import stdlib.StdIn;
import stdlib.StdOut;
import stdlib.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

// Random queue implementation using a resizing array.
public class ResizingArrayRandomQueue<Item> implements Iterable<Item> {
    private static final String UNCHECKED = "unchecked";
    // Array of items of queue q, Size of queue N
    private Item[] q;
    private int N;

    // Construct an empty queue.
    public ResizingArrayRandomQueue() {
    //@SuppressWarnings(UNCHECKED) // Use to suppress unchecked cast warning
        // Initialize array q of Item type size
        q = (Item[]) new Object[2];
    }

    // Is the queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // The number of items on the queue.
    public int size() {
        return N;
    }

    // Add item to the queue.
    // Double array size if full, insert item at N, increment array size N by 1
    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();
        if (N == q.length) resize(2*q.length);
        q[N++] = item;
    }

    // Remove and return a random item from the queue.
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int r = StdRandom.uniform(0, N);
        Item item = q[r];
        q[r] = q[N - 1];
        q[N - 1] = null;
        if (N > 0 && N == q.length/4) resize(q.length/2);
        N--;
        return item;
    }

    // Return a random item from the queue, but do not remove it.
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return q[StdRandom.uniform(0, N)];
    }

    // An independent iterator over items in the queue in random order.
    @Override
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }
    
    // An iterator, doesn't implement remove() since it's optional.
    private class RandomQueueIterator implements Iterator<Item> {
        private Item[] items;
        private int current;

        RandomQueueIterator() {
          //@SuppressWarnings(UNCHECKED) // to suppress unchecked cast warning
            // Copy array q to array items
            items = (Item[]) new Object[N];
            for (int i = 0; i < N; i++) {
                items[i] = q[i];
            }
            // Iterate through array items randomly swapping items for each index
            for (int i = 0; i < items.length; i++) {
                int randomIndex = StdRandom.uniform(i, N);
                Item tempItem = items[i];
                items[i] = items[randomIndex];
                items[randomIndex] = tempItem;
            }
            // current pointed to front of queue
            current = 0;
        }
        
        public boolean hasNext() {
            return current < N;
        }

        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            // no more items to return
            if (current == N) throw new NoSuchElementException();
            // returns item at current then increments
            return items[current++];
        }
    }

    // A string representation of the queue.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item).append(" ");
        }
        if (s.length() < 1) return "[Empty]";
        else return s.toString().substring(0, s.length() - 1);
    }

    // Helper method for resizing the underlying array.
    private void resize(int max) {
        @SuppressWarnings(UNCHECKED) // Else compiler will warn about cast
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < N; i++) {
            if (q[i] != null) {
                temp[i] = q[i];
            }
        }
        q = temp;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        ResizingArrayRandomQueue<Integer> q = 
            new ResizingArrayRandomQueue<Integer>();
        while (!StdIn.isEmpty()) {
            q.enqueue(StdIn.readInt());
        }
        int sum1 = 0;
        for (int x : q) {
            sum1 += x;
        }
        int sum2 = sum1;
        for (int x : q) {
            sum2 -= x;
        }
        int sum3 = 0;
        while (q.size() > 0) {
            sum3 += q.dequeue();
        }
        StdOut.println(sum1);
        StdOut.println(sum2);
        StdOut.println(sum3);
        StdOut.println(q.isEmpty());
    }
}
