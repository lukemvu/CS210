package edu.umb.cs210.p2;

import stdlib.StdOut;
import stdlib.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

// Deque implementation using a linked list.
public class LinkedDeque<Item> implements Iterable<Item> {
    private int N; // Size of dequeue
    private Node first; // Pointer to front of dequeue
    private Node last; // Pointer to rear of dequeue

    // Helper doubly-linked list class.
    private class Node {
        private Item item;  // This Node's value
        private Node next;  // The next Node
        private Node prev;  // The previous Node
    }

    // Construct an empty deque.
    public LinkedDeque() {
        // Initializing first and last nodes to 0, size to 0, empty dequeue
        first = null;
        last = null;
        N = 0;
    }

    // Is the deque empty?
    public boolean isEmpty() {
        return (first == null && last == null);
    }

    // The number of items on the deque.
    public int size() {
        return N;
    }

    // Add item to the front of the deque.
    public void addFirst(Item item) {
        // Throw NullPointerException if attempt made to add an empty item
        if (item == null) throw new NullPointerException();
        Node newNode = new Node();
        newNode.item = item;
        // Point newly created node back to first
        newNode.next = first;
        // If dequeue empty, point last to newNode
        // Else point old first to newNode
        if (first == null) {
            last = newNode;
        } else {
            first.prev = newNode;
        }
        // Assign newNode to first
        first = newNode;
        // Increment size of dequeue
        N++;
    }

    // Add item to the end of the deque.
    public void addLast(Item item) {
        // Throw NullPointerException if attempt made to add an empty item
        if (item == null) throw new NullPointerException();
        Node newNode = new Node();
        newNode.item = item;
        // Point newly created node to last
        newNode.prev = last;
        // If dequeue empty, point first to newNode
        // Else point old last to newNode
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        // Assign newNode to last
        last = newNode;
        N++;
    }

    // Remove and return item from the front of the deque.
    public Item removeFirst() {
        // Throw NoSuchElementException if trying to remove from empty dequeue
        if (first == null) throw new NoSuchElementException();
        // Copy first node into temp
        Node tempNode = first;
        // Move first pointer to the right
        first = first.next;
        if (first == null)
            last = null;
        else
            // Remove link from the left
            first.prev = null;

        // Decrement dequeue size
        N--;
        // Returns value of removed node
        return tempNode.item;
    }

    // Remove and return item from the end of the deque.
    public Item removeLast() {
        // Throw NoSuchElementException if trying to remove from empty dequeue
        if (last == null) throw new NoSuchElementException();
        Node tempNode = last;
        last = last.prev;
        if (last == null)
            first = null;
        else
            // Remove link from the right
            last.next = null;
        // Decrement dequeue size
        N--;
        // Returns value of removed node
        return tempNode.item;
    }

    // An iterator over items in the queue in order from front to end.
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    // An iterator, doesn't implement remove() since it's optional.
    private class DequeIterator implements Iterator<Item> {
        // Pointer to current node
        private Node current;

        // DequeIterator constructor
        DequeIterator() {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() { throw new UnsupportedOperationException(); }

        public Item next() {
            if (current == null) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // A string representation of the deque.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item + " ");
        }
        if (s.length() < 1) return "[Empty]";
        else return s.toString().substring(0, s.length() - 1);
    }
    
    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        LinkedDeque<Character> deque = new LinkedDeque<Character>();
        String quote = "There is grandeur in this view of life, with its " 
            + "several powers, having been originally breathed into a few " 
            + "forms or into one; and that, whilst this planet has gone " 
            + "cycling on according to the fixed law of gravity, from so " 
            + "simple a beginning endless forms most beautiful and most " 
            + "wonderful have been, and are being, evolved. ~ " 
            + "Charles Darwin, The Origin of Species";
        int r = StdRandom.uniform(0, quote.length());
        for (int i = quote.substring(0, r).length() - 1; i >= 0; i--) {
            deque.addFirst(quote.charAt(i));
        }
        for (int i = 0; i < quote.substring(r).length(); i++) {
            deque.addLast(quote.charAt(r + i));
        }
        StdOut.println(deque.isEmpty());
        StdOut.printf("(%d characters) ", deque.size());
        for (char c : deque) {
            StdOut.print(c);
        }
        StdOut.println();
        double s = StdRandom.uniform();
        for (int i = 0; i < quote.length(); i++) {
            if (StdRandom.bernoulli(s)) {
                deque.removeFirst();
            }
            else {
                deque.removeLast();
            }
        }
        StdOut.println(deque.isEmpty());
    }
}
