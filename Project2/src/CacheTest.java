import org.junit.Test;

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


	@Test
	public void leastRecentlyUsedIsCorrect () {
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
	}

	@Test
	public void retrieveData() {
		Cache<Integer,String> cache = new LRUCache<>(provider, 5);
		assertEquals(cache.get(0), "a");
	}

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






}
