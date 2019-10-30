import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Code to test <tt>WordSearch3D</tt>.
 */
public class WordSearchTester {
	private WordSearch3D _wordSearch;

	@Test
	/**
	 *  Verifies that search works correctly in a tiny grid that is effectively 2D.
	 */
	public void testSearchSimple () {
		// Note: this grid is 1x2x2 in size
		final char[][][] grid = new char[][][] { { { 'a', 'b', 'c' },
				{ 'd', 'f', 'e' } } };
		final int[][] location = _wordSearch.search(grid, "be");
		assertNotNull(location);
		assertEquals(location[0][0], 0);
		assertEquals(location[0][1], 0);
		assertEquals(location[0][2], 1);
		assertEquals(location[1][0], 0);
		assertEquals(location[1][1], 1);
		assertEquals(location[1][2], 2);
	}

	@Test
	/**
	 *  Verifies that search works correctly in a 3d array.
	 */
	public void testSearch3dWord () {
		// Note: this grid is 1x2x2 in size
		final char[][][] grid = new char[][][] {{{'c','a','t'},{'z','a','b'},{'e','z','c'}},
				{{'d','e','f'},{'z','e','g'},{'h','i','j'}},
				{{'k','l','b'},{'z','m','n'},{'o','p','q'}}};
		final int[][] location = _wordSearch.search(grid, "cat");
		assertNotNull(location);
		assertArrayEquals(location, new int[][]{{0,0,0},{0,0,1},{0,0,2}});
	}

	@Test
	/**
	 *  Verifies that search finds a word with repeating letters in a 3d array.
	 */
	public void testSearch3dWordRepeating () {
		// Note: this grid is 1x2x2 in size
		final char[][][] grid = new char[][][] {{{'c','a','t'},{'z','a','b'},{'e','z','c'}},
				{{'d','e','f'},{'z','e','g'},{'h','i','j'}},
				{{'k','l','b'},{'z','m','n'},{'o','p','q'}}};
		final int[][] location = _wordSearch.search(grid, "bee");
		assertNotNull(location);
		assertArrayEquals(location, new int[][]{{2,0,2},{1,1,1},{0,2,0}});
	}

	@Test
	/**
	 *  Verifies that search finds a word with all of the same letters in a 3d array.
	 */
	public void testSearch3dSameLetters () {
		// Note: this grid is 1x2x2 in size
		final char[][][] grid = new char[][][] {{{'c','a','t'},{'z','a','b'},{'e','z','c'}},
				{{'d','e','f'},{'z','e','g'},{'h','i','j'}},
				{{'k','l','b'},{'z','m','n'},{'o','p','q'}}};
		final int[][] location = _wordSearch.search(grid, "zzz");
		assertNotNull(location);
		assertArrayEquals(location, new int[][]{{0,1,0},{1,1,0},{2,1,0}});
	}

	@Test
	/**
	 *  Verifies that search returns null on a word that's larger than any direction of the 3d array.
	 */
	public void testSearch3dLongWord () {
		// Note: this grid is 1x2x2 in size
		final char[][][] grid = new char[][][] {{{'c','a','t'},{'z','a','b'},{'e','z','c'}},
				{{'d','e','f'},{'z','e','g'},{'h','i','j'}},
				{{'k','l','b'},{'z','m','n'},{'o','p','q'}}};
		final int[][] location = _wordSearch.search(grid, "cats");
		assertNull(location);
	}

	@Test
	/**
	 *  Verifies that search returns null on an empty word in a 3d array.
	 */
	public void testSearch3dEmptyWord () {
		// Note: this grid is 1x2x2 in size
		final char[][][] grid = new char[][][] {{{'c','a','t'},{'z','a','b'},{'e','z','c'}},
				{{'d','e','f'},{'z','e','g'},{'h','i','j'}},
				{{'k','l','b'},{'z','m','n'},{'o','p','q'}}};
		final int[][] location = _wordSearch.search(grid, "");
		assertNull(location);
	}

	@Test
	/**
	 *  Verifies that search returns the proper coordinates for a one-letter word in a 3d array.
	 */
	public void testSearch3dOneLetterWord () {
		// Note: this grid is 1x2x2 in size
		final char[][][] grid = new char[][][] {{{'c','a','t'},{'z','a','b'},{'e','z','c'}},
				{{'d','e','f'},{'z','e','g'},{'h','i','j'}},
				{{'k','l','b'},{'z','m','n'},{'o','p','q'}}};
		final int[][] location = _wordSearch.search(grid, "j");
		assertNotNull(location);
		assertArrayEquals(location, new int[][]{{1,2,2}});
	}

	@Test
	/**
	 * Verifies that make can generate a very simple puzzle that is effectively 1d.
	 */
	public void testMake1D () {
		final String[] words = new String[] { "java" };
		// Solution is either java or avaj
		final char[][][] grid = _wordSearch.make(words, 1, 1, 4);
		final char[] row = grid[0][0];
        System.out.println(Arrays.deepToString(grid));
		assertTrue((row[0] == 'j' && row[1] == 'a' && row[2] == 'v' && row[3] == 'a') ||
		           (row[3] == 'j' && row[2] == 'a' && row[1] == 'v' && row[0] == 'a'));
	}

	@Test
	/**
	 * Verifies that make returns null when it's impossible to construct a puzzle.
	 */
	public void testMakeImpossible () {
		final String[] words = new String[] {"aa", "bb", "cc", "dd", "ee"};
		final char[][][] grid = _wordSearch.make(words, 2, 2, 2);
		assertNull(grid);
	}


	@Test
	/**
	 * Verifies that make can generate a grid when it's *necessary* for words to share
	 * some common letter locations.
	 */
	public void testMakeWithIntersection () {
		final String[] words = new String[] { "amc", "dmf", "gmi", "jml", "nmo", "pmr", "smu", "vmx", "yma", "zmq" };
		final char[][][] grid = _wordSearch.make(words, 3, 3, 3);
		assertNotNull(grid);
	}

	@Test
	/**
	 * Verifies that make returns a grid of the appropriate size.
	 */
	public void testMakeGridSize () {
		final String[] words = new String[] { "at", "it", "ix", "ax" };
		final char[][][] grid = _wordSearch.make(words, 17, 11, 13);
		assertEquals(grid.length, 17);
		for (int x = 0; x < 2; x++) {
			assertEquals(grid[x].length, 11);
			for (int y = 0; y < 2; y++) {
				assertEquals(grid[x][y].length, 13);
			}
		}
	}

	/* TODO: write more methods for make*/

	@Before
	public void setUp () {
		_wordSearch = new WordSearch3D();
	}
}
