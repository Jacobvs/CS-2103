import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Implements a 3-d word search puzzle program.
 */
public class WordSearch3D {
    public WordSearch3D () {
    }

    /**
     * Searches for all the words in the specified list in the specified grid.
     * You should not need to modify this method.
     * @param grid the grid of characters comprising the word search puzzle
     * @param words the words to search for
     * @return a list of lists of locations of the letters in the words
     */
    public int[][][] searchForAll (char[][][] grid, String[] words) {
        final int[][][] locations = new int[words.length][][];
        for (int i = 0; i < words.length; i++) {
            locations[i] = search(grid, words[i]);
        }
        return locations;
    }

    /**
     * Searches for the specified word in the specified grid.
     * @param grid the grid of characters comprising the word search puzzle
     * @param word the word to search for
     * @return If the grid contains the
     * word, then the method returns a list of the (3-d) locations of its letters; if not,
     */
    public int[][] search (char[][][] grid, String word) {
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid[i].length; j++){
                for(int k=0; k<grid[j].length; k++){ // Tri-nested loop to traverse the 3d Array
                    if(grid[i][j][k] == word.charAt(0)) { // If current loc = first letter of word,
                        int[][] points = checkSurroundingChars(grid, i, j, k, word); // check to see if
                        if(points != null)
                            return points;
                    }
                }
            }
        }
        return null;
    }

    //TODO: COMMENT UP
    private int[][] checkSurroundingChars(char[][][] grid, int x, int y, int z, String word) {
        int[] bounds = checkBounds(grid, x, y, z);
        for (int i = bounds[0]; i <= bounds[1]; i++) {
            for (int j = bounds[2]; j <= bounds[3]; j++) {
                for (int k = bounds[4]; k <= bounds[5]; k++) {
                    try {
                        char c = grid[x + i][y + j][z + k];
                        if (word.charAt(1) == c) {
                            int[][] points = checkForWord(grid, x, y, z, i, j, k, word);
                            if (points != null)
                                return points;
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        System.out.println("Out of bounds: " + e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    private int[][] checkForWord(char[][][] grid, int x, int y, int z, int deltaX, int deltaY, int deltaZ, String word){
        int[][] points = new int[word.length()][3];
        for(int i = 0; i<word.length(); i++){
            try {
                char c = grid[x + (i * deltaX)][y + (i * deltaY)][z + (i * deltaZ)];
                if (c != word.charAt(i)) {
                    return null;
                } else {
                    points[i] = new int[]{x + (i * deltaX), y + (i * deltaY), z + (i * deltaZ)};
                }
            }
            catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Out of bounds: " + e.getMessage());
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

        int xLeftBound = -1, xRightBound = 1, yUpperBound = -1, yLowerBound = 1, zLeftBound = -1, zRightBound = 1;

        if (xPos == 0) {
            xLeftBound = 0;
        } else {
            if (xPos == grid.length) {
                xRightBound = 0;
            }
        }

        if (yPos == 0) {
            yUpperBound = 0;
        } else {
            if (yPos == grid[0].length) {
                yLowerBound = 0;
            }
        }

        if (zPos == 0) {
            zLeftBound = 0;
        } else {
            if (zPos == grid[0][0].length) {
                zRightBound = 0;
            }
        }

        return new int[] {xLeftBound, xRightBound, yUpperBound, yLowerBound, zLeftBound, zRightBound};
    }

    /**
     * Tries to create a word search puzzle of the specified size with the specified
     * list of words.
     * @param words the list of words to embed in the grid
     * @param sizeX size of the grid along first dimension
     * @param sizeY size of the grid along second dimension
     * @param sizeZ size of the grid along third dimension
     * @return a 3-d char array if successful that contains all the words, or <tt>null</tt> if
     * no satisfying grid could be found.
     */
    public char[][][] make (String[] words, int sizeX, int sizeY, int sizeZ) {
        // TODO: implement me
        return null;
    }

    /**
     * Exports to a file the list of lists of 3-d coordinates.
     * You should not need to modify this method.
     * @param locations a list (for all the words) of lists (for the letters of each word) of 3-d coordinates.
     * @param filename what to name the exported file.
     */
    public static void exportLocations (int[][][] locations, String filename) {
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
     * @param grid a 3-d grid of characters
     * @param filename what to name the exported file.
     */
    public static void exportGrid (char[][][] grid, String filename) {
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
    public static void main (String[] args) {
        final WordSearch3D wordSearch = new WordSearch3D();
        final String[] words = new String[] { "apple", "orange", "pear", "peach", "durian", "lemon", "lime", "jackfruit", "plum", "grape", "apricot", "blueberry", "tangerine", "coconut", "mango", "lychee", "guava", "strawberry", "kiwi", "kumquat", "persimmon", "papaya", "longan", "eggplant", "cucumber", "tomato", "zucchini", "olive", "pea", "pumpkin", "cherry", "date", "nectarine", "breadfruit", "sapodilla", "rowan", "quince", "toyon", "sorb", "medlar" };
        final int xSize = 10, ySize = 10, zSize = 10;
        final char[][][] grid = wordSearch.make(words, xSize, ySize, zSize);
        exportGrid(grid, "grid.txt");

        final int[][][] locations = wordSearch.searchForAll(grid, words);
        exportLocations(locations, "locations.txt");
    }
}
