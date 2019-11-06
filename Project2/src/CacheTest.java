import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {

	String[] dataArr = new String[]{"a", "b", "c", "d", "e", "f", "g"}; // capacity 7 (0-6)
	DataProvider<Integer,String> provider = new DataProvider<Integer, String>() {
		@Override
		public String get(Integer key) {
			return (0 <= key && key <= dataArr.length-1) ? dataArr[key] : null; // return an element from the dataArr or null if the index (key) provided is too large
		}
	}; // Need to instantiate an actual DataProvider


	@Test
	public void leastRecentlyUsedIsCorrect () {
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
	}

	@Test
	public void retrieveData() {
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
		String[] verify = new String[10];
		for(int i = 0; i<10; i++){
			String data = cache.get(i);
			verify[i] = data;
		}
		assertArrayEquals(verify, new String[]{"a", "b", "c", "d", "e", "f", "g", null, null, null});
		assertEquals(cache.getNumMisses(), 10);
	}

	@Test
	public void testCaching() {
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
		for (int i = 0; i < 4; i++) {
			cache.get(i);
		}
		for (int i = 0; i < 7; i++) {
			cache.get(i);
		}
		assertEquals(7, cache.getNumMisses());
	}

	//TODO: implement additional tests


}
