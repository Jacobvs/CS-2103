import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {

	private String[] dataArr = new String[]{"a", "b", "c", "d", "e", "f", "g"}; // capacity 7 (0-6)

	private DataProvider<Integer,String> provider = key -> { // Initialize dataprovider for testing
		return (0 <= key && key <= dataArr.length-1) ? dataArr[key] : null; // return an element from the dataArr or null if the index (key) provided is too large
	};

	/**
	 * Test that LRU Cache initializes correctly
	 */
	@Test
	public void leastRecentlyUsedIsCorrect () {
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
	}

	/**
	 * Test that the Cache retrieves data properly
	 */
	@Test
	public void retrieveData() {
		Cache<Integer,String> cache = new LRUCache<>(provider, 5);
		assertEquals(cache.get(0), "a");
	}

	/**
	 * Tests if get can handle null as an input, and if misses are counted properly
	 */
	@Test
	public void testRetrieveNull(){
		Cache<Integer,String> cache = new LRUCache<>(provider, 5);
		String[] verify = new String[10];
		for(int i = 0; i<10; i++){
			String data = cache.get(i);
			verify[i] = data;
		}
		assertArrayEquals(verify, new String[]{"a", "b", "c", "d", "e", "f", "g", null, null, null});
		assertEquals(10, cache.getNumMisses());
	}

	/**
	 * Ensures that cache pulls from data provider properly, and that get method pulls from cache properly
	 * Testing against a normal sized set of data, in this case capacity 5
	 */
	@Test
	public void testCaching() {
		Cache<Integer,String> cache = new LRUCache<>(provider, 5);
		for (int i = 0; i < 4; i++) {
			cache.get(i);
		}
		for (int i = 0; i < 7; i++) {
			cache.get(i);
		}
		assertEquals(7, cache.getNumMisses());
	}

	/**
	 * Ensures that nodes are being moved and evicted properly
	 * - Ensures that a previously not most recent node is being marked as most recent if it is called and hit
	 * - Ensures that a node is evicted if it is least recently used and an extra node is added to full list
	 * - Testing against a somewhat specialized set of data, as there is only one node that is not head or tail
	 */
	@Test
	public void testLRUEviction(){
		Cache<Integer,String> cache = new LRUCache<>(provider, 3);
		int[] misses = new int[5];
		cache.get(0); // get a
		cache.get(1); // get b
		cache.get(2); // get c | Cache should look like {c, b, a} at this point && == 3 misses
		misses[0] = cache.getNumMisses();
		cache.get(1); // get b again -> {b, c, a} == 3 misses
		misses[1] = cache.getNumMisses();
		cache.get(3); // get d, evict a -> {d, b, c} == 4 misses
		misses[2] = cache.getNumMisses();
		cache.get(0); // get a again, evict c -> {a, d, b} == 5 misses
		misses[3] = cache.getNumMisses();
		cache.get(1); // get b again -> {b, a, d} == 5 misses (ensures b was moved to front)
		misses[4] = cache.getNumMisses();

		assertArrayEquals(new int[]{3,3,4,5,5}, misses);
	}

	/**
	 * Ensure that the LRUCache works properly with a maximum capacity of 2.
	 * - Ensures that the LRUCache is able to properly set head and tail when there are only 2 elements
	 * - Testing against special case, as there is no node between head and tail
	 */
	@Test
	public void testCapacity2(){
		Cache<Integer,String> cache = new LRUCache<>(provider, 2);
		int[] misses = new int[6];
		cache.get(0);
		misses[0] = cache.getNumMisses();
		cache.get(1);
		misses[1] = cache.getNumMisses();
		cache.get(0);
		misses[2] = cache.getNumMisses();
		cache.get(2);
		misses[3] = cache.getNumMisses();
		cache.get(1);
		misses[4] = cache.getNumMisses();
		cache.get(2);
		misses[5] = cache.getNumMisses();

		assertArrayEquals(new int[]{1,2,2,3,4,4}, misses);
	}

	/**
	 * Tests that LRUCache works with a maximum capacity of 1
	 * - This tests a special case, as the head and tail will be the same.
	 */
	@Test
	public void testCapacity1(){
		Cache<Integer,String> cache = new LRUCache<>(provider, 1);
		int[] misses = new int[6];
		cache.get(0);
		misses[0] = cache.getNumMisses();
		cache.get(1);
		misses[1] = cache.getNumMisses();
		cache.get(1);
		misses[2] = cache.getNumMisses();
		cache.get(2);
		misses[3] = cache.getNumMisses();
		cache.get(0);
		misses[4] = cache.getNumMisses();
		cache.get(0);
		misses[5] = cache.getNumMisses();

		assertArrayEquals(new int[]{1,2,2,3,4,4}, misses);
	}

	/**
	 * Tests that LRUCache does not break when assigned a capacity of zero
	 * - All elements will be misses
	 */
	@Test
	public void testCapacity0(){
		Cache<Integer,String> cache = new LRUCache<>(provider, 0);
		cache.get(0);
		cache.get(1);
		cache.get(2);
		cache.get(0);
		cache.get(1);
		assertEquals(5, cache.getNumMisses());
	}

	/**
	 * Tests LRUCache with a relatively large set of data
	 * - Should work the same as any general case (cache maximum capacity > 3)
	 */
	@Test
	public void testLargeCapacity(){
		DataProvider<Integer, Integer> largeDP = key -> key;
		Cache<Integer, Integer> cache = new LRUCache<>(largeDP, 5000);
		for (int i = 0; i < 10000; i++) {
			cache.get(i);
		}
		for (int i = 5000; i < 10000; i++) {
			cache.get(i);
		}
		assertEquals(10000, cache.getNumMisses());
	}

	/**
	 * Tests that the getNumMisses from cache is the same as the get calls from the DataProvider
	 */
	@Test
	public void testNumMisses(){
        final AtomicInteger requests = new AtomicInteger(); // Use Atomic Integer to log get requests from inner class
		DataProvider<Integer,String> loggingProvider = key -> { // Initialize dataprovider for testing
			requests.set(requests.get()+1); // Increment requests
			return (0 <= key && key <= dataArr.length-1) ? dataArr[key] : null; // return an element from the dataArr or null if the index (key) provided is too large
		};
		Cache<Integer, String> cache = new LRUCache<>(loggingProvider, 5);

        for (int i = 0; i < 7 ; i++) {
            cache.get(i);
        }
        for (int i = 2; i < 7; i++) {
            cache.get(i);
        }
        assertEquals(new AtomicInteger(7).get(), requests.get());
	}
}
