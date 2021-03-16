package edu.umb.cs210.p6;

import dsa.DiGraph;
import dsa.RedBlackBinarySearchTreeST;
import dsa.Set;
import stdlib.In;
import stdlib.StdOut;

// An immutable WordNet data type.
public class WordNet {

    // Nouns and their set of Synset IDs
    RedBlackBinarySearchTreeST<String, Set<Integer>> st;

    // Synset IDs and their synset string
    RedBlackBinarySearchTreeST<Integer, String> rst;

    // ShortestCommonAncestor object
    ShortestCommonAncestor sca;
    
    // Construct a WordNet object given the names of the input (synset and
    // hypernym) files.
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null && hypernyms == null) {
            throw new NullPointerException(
                    "\"synsets is null\" <OR> \"hypernyms is null\"");
        } else if (synsets == null) {
            throw new NullPointerException("synsets is null");
        } else if (hypernyms == null) {
            throw new NullPointerException("hypernyms is null");
        }

        // Initialize search trees
        st = new RedBlackBinarySearchTreeST<String, Set<Integer>>();
        rst = new RedBlackBinarySearchTreeST<Integer, String>();

        // Initialize input files to read from
        In inSynsets = new In(synsets);
        In inHypernyms = new In(hypernyms);

        // Read from synsets input line by line until empty
        while (!inSynsets.isEmpty()) {

            // tokens are [synId],[noun noun noun],[gloss]
            String[] tokenSynsets = inSynsets.readLine().split(",");
            int synId = Integer.parseInt(tokenSynsets[0]);

            // Split synsets into individual nouns
            String[] nouns = tokenSynsets[1].split(" ");

            // New set for each each unique noun
            Set<Integer> set = new Set<Integer>();
            set.add(synId);

            for (String word : nouns) {
                // If noun already exists in tree, add synId to its set
                // Else insert noun with Set containing synId
                if (st.contains(word))
                    st.get(word).add(synId);
                else
                    st.put(word, set);
            }

            // Insert each synId with its corresponding synset string
            rst.put(synId, tokenSynsets[1]);
        }

        // Digraph G size of # of unique synset IDs
        DiGraph G = new DiGraph(rst.size());

        // Read from synsets input line by line until empty
        while (!inHypernyms.isEmpty()) {
            String[] token = inHypernyms.readLine().split(",");
            int synId = Integer.parseInt(token[0]);
            for (int i = 1; i < token.length; i++) {

                // Add an edge from synset ID to its hypernym ID
                G.addEdge(synId, Integer.parseInt(token[i]));
            }
        }
        sca = new ShortestCommonAncestor(G);
    }

    // All WordNet nouns.
    public Iterable<String> nouns() {
        return st.keys();
    }

    // Is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException("word is null");
        }
        return st.contains(word);
    }

    // A synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
        if (noun1 == null && noun2 == null) {
            throw new NullPointerException(
                    "\"noun1 is null\" <OR> \"noun2 is null\"");
        } else if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        } else if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        } else if (!isNoun(noun1) && !isNoun(noun2)) {
            throw new IllegalArgumentException(
                    "\"noun1 is not a noun\" <OR> \"noun2 is not a noun\"");
        } else if (!isNoun(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        } else if (!isNoun(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }

        return rst.get(sca.ancestor(st.get(noun1), st.get(noun2)));
    }

    // Distance between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        if (noun1 == null && noun2 == null) {
            throw new NullPointerException(
                    "\"noun1 is null\" <OR> \"noun2 is null\"");
        } else if (noun1 == null) {
            throw new NullPointerException("noun1 is null");
        } else if (noun2 == null) {
            throw new NullPointerException("noun2 is null");
        } else if (!isNoun(noun1) && !isNoun(noun2)) {
            throw new IllegalArgumentException(
                    "\"noun1 is not a noun\" <OR> \"noun2 is not a noun\"");
        } else if (!isNoun(noun1)) {
            throw new IllegalArgumentException("noun1 is not a noun");
        } else if (!isNoun(noun2)) {
            throw new IllegalArgumentException("noun2 is not a noun");
        }

        return sca.length(st.get(noun1), st.get(noun2));
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];        
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.println("# of nouns = " + nouns);
        StdOut.println("isNoun(" + word1 + ") = " + wordnet.isNoun(word1));
        StdOut.println("isNoun(" + word2 + ") = " + wordnet.isNoun(word2));
        StdOut.println("isNoun(" + (word1 + " " + word2) + ") = "
                       + wordnet.isNoun(word1 + " " + word2));
        StdOut.println("sca(" + word1 + ", " + word2 + ") = "
                       + wordnet.sca(word1, word2));
        StdOut.println("distance(" + word1 + ", " + word2 + ") = "
                       + wordnet.distance(word1, word2));
    }
}
