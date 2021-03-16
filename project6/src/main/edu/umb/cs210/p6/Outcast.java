package edu.umb.cs210.p6;

import stdlib.In;
import stdlib.StdOut;

// An immutable data type for outcast detection.
public class Outcast {
    WordNet wordnet; // Wordnet object

    // Construct an Outcast object given a WordNet object.
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // The outcast noun from nouns.
    public String outcast(String[] nouns) {
        String noun = null;
        int maxDis = Integer.MIN_VALUE;

        // Sum distance between each noun with all the other nouns.
        // Return noun with largest distance between all other nouns.
        for (String noun1 : nouns) {
            int dis = 0;
            for (String noun2 : nouns) {
                dis += wordnet.distance(noun1, noun2);
            }
            if (dis > maxDis) {
                maxDis = dis;
                noun = noun1;
            }
        }
        return noun;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println("outcast(" + args[t] + ") = "
                           + outcast.outcast(nouns));
        }
    }
}
