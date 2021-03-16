package edu.umb.cs210.p5;

import dsa.LinkedQueue;
import stdlib.StdIn;
import stdlib.StdOut;

public class ArrayST<Key, Value> {
    private static final int INIT_CAPACITY = 2;
    private Key[] keys;
    private Value[] values;
    private int N;

    // Create a symbol table with INIT_CAPACITY.
    public ArrayST() {
        keys = (Key[]) new Object[INIT_CAPACITY];
        values = (Value[]) new Object[INIT_CAPACITY];
        N = 0;
    }

    // Create a symbol table with given capacity.
    public ArrayST(int capacity) {
        keys = (Key[]) new Object[capacity];
        values = (Value[]) new Object[capacity];
        N = 0;
    }

    // Return the number of key-value pairs in the table.
    public int size() {
        return N;
    }

    // Return true if the table is empty and false otherwise.
    public boolean isEmpty() {
        return N == 0;
    }

    // Return true if the table contains key and false otherwise.
    public boolean contains(Key key) {
        for (int i = 0; i < N; i++) {
            if (key.equals(keys[i])) {
                return true;
            }
        }
        return false;
    }

    // Return the value associated with key, or null.
    public Value get(Key key) {
        for (int i = 0; i < N; i++) {
            if (key.equals(keys[i]))
                return values[i];
        }
        return null;
    }

    // Put the key-value pair into the table; remove key from table 
    // if value is null.
    public void put(Key key, Value value) {
        if (value == null) {
            delete(key);
        } else if (this.contains(key)) {
            for (int i = 0; i < N; i++) {
                if (key.equals(keys[i])) {
                    values[i] = value;
                    break;
                }
            }
        } else {
            if (N >= keys.length) resize(N*2);
            keys[N] = key;
            values[N] = value;
            N++;
        }
    }

    // Remove key (and its value) from table.
    public void delete(Key key) {
        if (this.contains(key)) {
            for (int i = 0; i < N; i++) {
                if (key.equals(keys[i])) {
                    keys[i] = keys[N-1];
                    keys[N-1] = null;
                    values[i] = values[N-1];
                    values[N-1] = null;
                    N--;
                    if (N > 0 && N == keys.length/4) resize(keys.length/2);
                    return;
                }
            }
        }
    }

    // Return all the keys in the table.
    public Iterable<Key> keys() {
        LinkedQueue<Key> keysQ = new LinkedQueue<Key>();
        for (int i = 0; i < N; i++) {
            keysQ.enqueue(keys[i]);
        }
        return keysQ;
    }

    // Resize the internal arrays to capacity.
    private void resize(int capacity) {
        Key[] tempk = (Key[]) new Object[capacity];
        Value[] tempv = (Value[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            tempk[i] = keys[i];
            tempv[i] = values[i];
        }
        values = tempv;
        keys = tempk;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        ArrayST<String, Integer> st = new ArrayST<String, Integer>();
        int count = 0;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            st.put(s, ++count);
        }
        for (String s : args) {
            st.delete(s);
        }
        for (String s : st.keys()) {
            StdOut.println(s + " " + st.get(s));
        }
    }
}
