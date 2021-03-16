package edu.umb.cs210.p4;

import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private final int[][] tiles;    // tiles in the board
    private int N;                  // board size
    private int hamming;            // Hamming distance
    private int manhattan;          // Manhattan distance
    
    // Construct a board from an N-by-N array of tiles, where 
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank 
    // square.
    public Board(int[][] tiles) {
        this.tiles = tiles;
        this.N = tiles.length;
        this.hamming = hamming();
        this.manhattan = manhattan();
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        return this.tiles[i][j];
    }
    
    // Size of this board.
    public int size() {
        return this.N;
    }

    // Number of tiles out of place.
    public int hamming() {
        int hcount = 0;
        // Iterate through matrix
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                // Increment hcount when tile at index i, j
                // does not match goal board tile
                // calculated by N*i + j + 1
                if (tileAt(i, j) != this.N*i + j + 1) hcount++;
            }
        }
        // Decrement 1, blank tile is in correct spot at last index
        return hcount-1;
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        int mcount = 0;
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                // Ignore blank tile
                // Check for goal board tile
                if (tileAt(i, j) != 0 && tileAt(i, j) != this.N*i + j + 1) {
                    int val = tileAt(i, j);
                    // Find index of goal board tile k, l of current index i, j
                    int k = (val-1) / this.N;
                    int l = (val-1) % this.N;
                    // Sum differences of current tile index
                    // and goal board tile index
                    mcount += Math.abs(i-k) + Math.abs(j-l);
                }
            }
        }
        return mcount;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        // Hamming() returning 0 means goal board is met
        return this.hamming == 0;
    }

    // Is this board solvable?
    public boolean isSolvable() {
        // N odd board
        if (this.N % 2 == 1) {
            // Solvable if # of inversions even
            return inversions() % 2 == 0;
        } else {
            // N even board
            int blankRow = (blankPos()-1)/this.N;
            int sum = blankRow + inversions();
            // Solvable if sum of index of row of blankPos and # of inversions
            // is odd
            return sum % 2 == 1;
        }
    }

    // Does this board equal that?
    public boolean equals(Object that) {
        // Compare pointers
        if (that == this) return true;
        // Check if that is null
        if (that == null) return false;
        // Compare classes of each object
        if (that.getClass() != this.getClass()) return false;
        Board other = (Board) that;
        // Compare board sizes
        if (this.N != other.N) return false;
        // Compare each tile value
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.tileAt(i, j) != other.tileAt(i, j)) return false;
            }
        }
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        // New linked queue q of Board objects
        LinkedQueue<Board> q = new LinkedQueue<Board>();
        // Find index i, j of blankPos
        int i = (blankPos() - 1) / this.N;
        int j = (blankPos() - 1) % this.N;
        // UP - check for valid position in matrix for swap
        if (i-1 >= 0) {
            // Clone this.tiles 2D array
            int[][] tilesClone = cloneTiles();
            // Swap tiles
            int temp = tilesClone[i-1][j];
            tilesClone[i-1][j] = 0;
            tilesClone[i][j] = temp;
            // Enqueue into linked queue
            q.enqueue(new Board(tilesClone));
        }
        // Right
        if (j+1 < this.N) {
            int[][] tilesClone = cloneTiles();
            int temp = tilesClone[i][j+1];
            tilesClone[i][j+1] = 0;
            tilesClone[i][j] = temp;
            q.enqueue(new Board(tilesClone));
        }
        // Down
        if (i+1 < this.N) {
            int[][] tilesClone = cloneTiles();
            int temp = tilesClone[i+1][j];
            tilesClone[i+1][j] = 0;
            tilesClone[i][j] = temp;
            q.enqueue(new Board(tilesClone));
        }
        // Left
        if (j-1 >= 0) {
            int[][] tilesClone = cloneTiles();
            int temp = tilesClone[i][j-1];
            tilesClone[i][j-1] = 0;
            tilesClone[i][j] = temp;
            q.enqueue(new Board(tilesClone));
        }
        // Return linked queue q of Board objects
        return q;
    }

    // String representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d", tiles[i][j]));
                if (j < N - 1) {
                    s.append(" ");
                }
            }
            if (i < N - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Helper method that returns the position (in row-major order) of the 
    // blank (zero) tile.
    // Search array for tile, return position N*i + j + 1
    private int blankPos() {
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.tiles[i][j] == 0) {
                    return this.N*i + j + 1;
                }
            }
        }
        return 0;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        int inv = 0;
        // Helper 1D array, copies 2D matrix into 1D array for easy iteration
        int[] rowmaj = new int[this.N * this.N];
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                rowmaj[this.N*i + j] = tiles[i][j];
            }
        }
        // Search for inversions, ignore blank 0, increment inv
        for (int i = 0; i < rowmaj.length; i++) {
            for (int j = i; j < rowmaj.length; j++) {
                if (rowmaj[i] != 0 && rowmaj[j] != 0) {
                    if (rowmaj[i] > rowmaj[j]) inv++;
                }
            }
        }
        return inv;
    }

    // Helper method that clones the tiles[][] array in this board and 
    // returns it.
    private int[][] cloneTiles() {
        // New 2D array tilesCopy
        int[][] tilesCopy = new int[this.N][];
        for (int i = 0; i < tilesCopy.length; i++) {
            // Clone each array in tiles[i] into tilesCopy[i]
            tilesCopy[i] = this.tiles[i].clone();
        }
        return tilesCopy;
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
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
