import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Implements a 3-d word search puzzle program.
 */
public class WordSearch3D {
    public WordSearch3D() {
    }

    /**
     * Searches for all the words in the specified list in the specified grid.
     * You should not need to modify this method.
     *
     * @param grid  the grid of characters comprising the word search puzzle
     * @param words the words to search for
     * @return a list of lists of locations of the letters in the words
     */
    public int[][][] searchForAll(char[][][] grid, String[] words) {
        final int[][][] locations = new int[words.length][][];
        for (int i = 0; i < words.length; i++) {
            locations[i] = search(grid, words[i]);
        }
        return locations;
    }

    /**
     * Searches for the specified word in the specified grid.
     *
     * @param grid the grid of characters comprising the word search puzzle
     * @param word the word to search for
     * @return If the grid contains the
     * word, then the method returns a (2D Array) of the (3-d) locations of its letters; if not, returns null
     */
    public int[][] search(char[][][] grid, String word) {
        if(word.length() == 0)
            return null;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                for (int k = 0; k < grid[i][j].length; k++) { // Tri-nested loop to traverse the 3d Array
                    if (grid[i][j][k] == word.charAt(0)) { // If current loc = first letter of word,
                        if(word.length() == 1)
                            return new int[][]{{i,j,k}};
                        int[][] points = checkSurroundingChars(grid, i, j, k, word); // check to see if
                        if (points != null)
                            return points;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Searches surrounding 3-d space 1 move away from a position for the next letter in a word
     * @param grid the grid of characters comprising the word search puzzle
     * @param x x-coordinate of starting letter
     * @param y y-coordinate
     * @param z z-coordinate
     * @param word the word to search for
     * @return Null if the second letter cannot be found, otherwise a (2D) Array of the (3D) locations of the word's letters
     */
    private int[][] checkSurroundingChars(char[][][] grid, int x, int y, int z, String word) {
        int[] bounds = checkBounds(grid, x, y, z);
        for (int i = bounds[0]; i <= bounds[1]; i++) {
            for (int j = bounds[2]; j <= bounds[3]; j++) {
                for (int k = bounds[4]; k <= bounds[5]; k++) {
                    try {
                        if (!(i == 0 & j == 0 && k == 0)) {
                            char c = grid[x + i][y + j][z + k];
                            if (word.charAt(1) == c) {
                                int[][] points = checkForWord(grid, x, y, z, i, j, k, word);
                                if (points != null)
                                    return points;
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }
        return null;
    }

    /**
     * Given a starting coordinate and a direction (in the form of coordinate delta's), search the 3d space letter by letter
     * in the given direction until the word is completed, or an edge is reached.
     * @param grid the grid of characters comprising the word search puzzle
     * @param x x-coordinate of starting letter
     * @param y y-coordinate
     * @param z z-coordinate
     * @param deltaX change per step in the x direction
     * @param deltaY change per step in the y direction
     * @param deltaZ change per step in the z direction
     * @param word the word to search for
     * @return Null if the whole word cannot be completed, otherwise a (2D) Array of the (3D) locations of the word's letters
     */
    private int[][] checkForWord(char[][][] grid, int x, int y, int z, int deltaX, int deltaY, int deltaZ, String word) {
        int[][] points = new int[word.length()][3];
        for (int i = 0; i < word.length(); i++) {
            try {
                char c = grid[x + (i * deltaX)][y + (i * deltaY)][z + (i * deltaZ)];
                if (c != word.charAt(i)) {
                    return null;
                } else {
                    points[i] = new int[]{x + (i * deltaX), y + (i * deltaY), z + (i * deltaZ)};
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;
            }
        }
        return points;
    }

    //Method will set 6 variables used to control for loops in checkSurroundingPairs method
    // xLeftBound: -1 or 0, xRightBound: 0 or 1 **both cannot be 0
    // yUpperBound: -1 or 0, yLowerBound 0 or 1 **both cannot be 0
    // zLeftBound: -1 or 0, zRightBound 0 or 1 **both cannot be 0
    public int[] checkBounds(char[][][] grid, int xPos, int yPos, int zPos) {
        int xLeftBound, xRightBound, yUpperBound, yLowerBound, zLeftBound, zRightBound;

        xLeftBound = (xPos == 0) ? 0 : -1;
        xRightBound = (xPos == grid.length) ? 0 : 1;
        yUpperBound = (yPos == 0) ? 0 : -1;
        yLowerBound = (yPos == grid[0].length) ? 0 : 1;
        zLeftBound = (zPos == 0) ? 0 : -1;
        zRightBound = (zPos == grid[0][0].length) ? 0 : 1;

        return new int[]{xLeftBound, xRightBound, yUpperBound, yLowerBound, zLeftBound, zRightBound};
    }

    /**
     * Tries to create a word search puzzle of the specified size with the specified
     * list of words.
     *
     * @param words the list of words to embed in the grid
     * @param sizeX size of the grid along first dimension
     * @param sizeY size of the grid along second dimension
     * @param sizeZ size of the grid along third dimension
     * @return a 3-d char array if successful that contains all the words, or <tt>null</tt> if
     * no satisfying grid could be found.
     */
    public char[][][] make(String[] words, int sizeX, int sizeY, int sizeZ) {
        char[][][] board = new char[sizeX][sizeY][sizeZ]; //make new board
        ArrayList<String> wordsToPlace = new ArrayList<>(Arrays.asList(words));

        for (int i = 1; i < 1000; i++) {
            while(true){
                int w = new Random().nextInt(wordsToPlace.size());
                String word = wordsToPlace.get(w);

                board = placeWord(board, word);
                if(board != null){
                    if(wordsToPlace.size() == 1) {
                        System.out.println("Completed board:");
                        System.out.println(Arrays.deepToString(board));
                        System.out.println("Filled board:");
                        board = fillBlankSpace(board);
                        System.out.println(Arrays.deepToString(board));
                        return board;
                    }
                    else
                        wordsToPlace.remove(w);
                }
                else
                    break;
            }

            System.out.println("Reset Board");
            System.out.println("Board attempts: " + i);
            board = new char[sizeX][sizeY][sizeZ];
            wordsToPlace = new ArrayList<>(Arrays.asList(words));

        }

        return null;
    }

    public char[][][] placeWord(char[][][] board, String word) {

        for (int i = 0; i < 1000; i++) {
            int x = 0, y = 0, z = 0;
            while (board[x][y][z] != '\0') {
                x = new Random().nextInt(board.length);
                y = new Random().nextInt(board[0].length);
                z = new Random().nextInt(board[0][0].length);
            }
            int dX = 0, dY = 0, dZ = 0;
            while (dX == 0 && dY == 0 && dZ == 0) {
                dX = new Random().nextInt(3) - 1;
                dY = new Random().nextInt(3) - 1;
                dZ = new Random().nextInt(3) - 1;
            }

            for (int j = 0; j < word.length(); j++) { // Check if there's space for the word
                try {
                    char c = board[x + (j * dX)][y + (j * dY)][z + (j * dZ)];
                    if (c != '\0' && c != word.charAt(j)) {
                        break;
                    } else if (word.length() - 1 == j) { // If there's space for the word, place it.
                        for (int k = 0; k < word.length(); k++) {
                            board[x + (k * dX)][y + (k * dY)][z + (k * dZ)] = word.charAt(k);
                        }
                        return board;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        return null;
    }

    public char[][][] fillBlankSpace(char[][][] board){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                for (int k = 0; k < board[i][j].length; k++) {
                    if(board[i][j][k] == '\0'){
                        board[i][j][k] = (char) (new Random().nextInt(26) + 'a');
                    }
                }
            }
        }
        return board;
    }

    /**
     * Exports to a file the list of lists of 3-d coordinates.
     * You should not need to modify this method.
     *
     * @param locations a list (for all the words) of lists (for the letters of each word) of 3-d coordinates.
     * @param filename  what to name the exported file.
     */
    public static void exportLocations(int[][][] locations, String filename) {
        // First determine how many non-null locations we have
        int numLocations = 0;
        for (int i = 0; i < locations.length; i++) {
            if (locations[i] != null) {
                numLocations++;
            }
        }

        try (final PrintWriter pw = new PrintWriter(filename)) {
            pw.print(numLocations);  // number of words
            pw.print('\n');
            for (int i = 0; i < locations.length; i++) {
                if (locations[i] != null) {
                    pw.print(locations[i].length);  // number of characters in the word
                    pw.print('\n');
                    for (int j = 0; j < locations[i].length; j++) {
                        for (int k = 0; k < 3; k++) {  // 3-d coordinates
                            pw.print(locations[i][j][k]);
                            pw.print(' ');
                        }
                    }
                    pw.print('\n');
                }
            }
            pw.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    /**
     * Exports to a file the contents of a 3-d grid.
     * You should not need to modify this method.
     *
     * @param grid     a 3-d grid of characters
     * @param filename what to name the exported file.
     */
    public static void exportGrid(char[][][] grid, String filename) {
        try (final PrintWriter pw = new PrintWriter(filename)) {
            pw.print(grid.length);  // height
            pw.print(' ');
            pw.print(grid[0].length);  // width
            pw.print(' ');
            pw.print(grid[0][0].length);  // depth
            pw.print('\n');
            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[0].length; y++) {
                    for (int z = 0; z < grid[0][0].length; z++) {
                        pw.print(grid[x][y][z]);
                        pw.print(' ');
                    }
                }
                pw.print('\n');
            }
            pw.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    /**
     * Creates a 3-d word search puzzle with some nicely chosen fruits and vegetables,
     * and then exports the resulting puzzle and its solution to grid.txt and locations.txt
     * files.
     */
    public static void main(String[] args) {
        final WordSearch3D wordSearch = new WordSearch3D();
        final String[] words = new String[]{"apple", "orange", "pear", "peach", "durian", "lemon", "lime", "jackfruit", "plum", "grape", "apricot", "blueberry", "tangerine", "coconut", "mango", "lychee", "guava", "strawberry", "kiwi", "kumquat", "persimmon", "papaya", "longan", "eggplant", "cucumber", "tomato", "zucchini", "olive", "pea", "pumpkin", "cherry", "date", "nectarine", "breadfruit", "sapodilla", "rowan", "quince", "toyon", "sorb", "medlar"};
        final int xSize = 10, ySize = 10, zSize = 10;
        final char[][][] grid = wordSearch.make(words, xSize, ySize, zSize);
        exportGrid(grid, "grid.txt");

        final int[][][] locations = wordSearch.searchForAll(grid, words);
        exportLocations(locations, "locations.txt");
    }
}
