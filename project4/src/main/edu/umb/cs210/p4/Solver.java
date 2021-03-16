package edu.umb.cs210.p4;

import dsa.LinkedStack;
import dsa.MinPQ;
import stdlib.In;
import stdlib.StdOut;

import java.util.Comparator;

// A solver based on the A* algorithm for the 8-puzzle and its generalizations.
public class Solver {
    LinkedStack<Board> solution;    // Sequence of boards in shortest solution
    int moves;                      // Min # of moves to solve initial board
    
    // Helper search node class.
    private class SearchNode {
        Board board;                // Board represented by this node
        int moves;                  // # of moves from initial node
        int hamming;                // Hamming order of board
        int manhattan;              // Manhattan order of board
        SearchNode previous;        // Pointer to previous node

        // Construct a new SearchNode
        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            hamming = board.hamming();
            manhattan = board.manhattan();
            this.previous = previous;

        }
    }
     
    // Find a solution to the initial board (using the A* algorithm).
    public Solver(Board initial) {
        // Corner case: null initial board
        if (initial == null) throw new NullPointerException("null board");
        // Corner case: Unsolvable board
        if (!initial.isSolvable()) {
            throw new IllegalArgumentException("Unsolvable board");
        }
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>(new ManhattanOrder());
        solution = new LinkedStack<Board>();
        // Initial node with 0 moves and a null previous node
        pq.insert(new SearchNode(initial, 0, null));
        while (!pq.isEmpty()) {
            // Remove min priority node from pq
            SearchNode node = pq.delMin();
            // Check if goal board
            if (node.board.isGoal()) {
                // Store moves
                moves = node.moves;
                // Traverse queue pushing solution path board
                // onto solution stack, [goal board, initial board)
                while (node.previous != null) {
                    solution.push(node.board);
                    node = node.previous;
                }
                break;
            }
            // Insert new SearchNodes from neighbor boards incrementing
            // each node's moves and linking the previous node
            for (Board neighbor : node.board.neighbors()) {
                // Skip insertion of a previous board
                if (node.previous == null ||
                    !neighbor.equals(node.previous.board)) {
                    pq.insert(new SearchNode(neighbor, node.moves+1, node));
                }
            }
        }
    }

    // The minimum number of moves to solve the initial board.
    public int moves() {
        return this.moves;
    }

    // Sequence of boards in a shortest solution.
    public Iterable<Board> solution() {
        return solution;
    }

    // Helper hamming priority function comparator.
    private static class HammingOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int aBoard = a.hamming + a.moves;
            int bBoard = b.hamming + b.moves;
            // Compares hamming order + # of moves
            return aBoard-bBoard;
        }
    }
       
    // Helper manhattan priority function comparator.
    private static class ManhattanOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int aBoard = a.manhattan + a.moves;
            int bBoard = b.manhattan + b.moves;
            // Compares manhattan order + # of moves
            return aBoard-bBoard;
        }
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
        else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
