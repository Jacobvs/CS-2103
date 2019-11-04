import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {

	private int capacity, misses;
	private DataProvider<T, U> dataProvider;
	private Map<T, Node<T, U>> map;
	private Node<T, U> first;
	private Node<T, U> last;

	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		this.misses = 0;
		this.capacity = capacity;
		this.dataProvider = provider;
		map = new HashMap<>();

	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		/* NOTES/Thinking :
		1. check map for contains --> returns null or linkedlist object/ reference?
		2. if map contains, keep map same, push element of ll to front of ll (ru = front)
		3. if map == null & cache not full, add to map & front of ll && update missed num
		4. if map == null & cache full:
		-  0. increment misses
		-  1. retrieve new data
		-  2. remove last element from ll and map
		-  3. add new data to map and front of ll
		-  4. return new data??


		O(1) operations:
		adding to ll
		accessing first/last element in ll
		removing from ll with index
		accessing hashmap with key

		O(n) operations:
		finding element in ll without index
		finding element in hm without key/hash
		removing from ll without index
		updating indices


		O(1) Operation:
		Consider a doubly linked list A <--> B <--> C <--> D <--> E. Suppose I have a pointer/reference to D. To remove it, I do:
			Find the predecessor (C) - O(1)
			Find the successor (D) - O(1)
			Change the predecessor's "next" to the successor (E) - O(1)
			Change the successor "previous" to the predecessor's (C) - O(1)
		 */

		Node<T, U> request = map.get(key);

		if(request != null){ // Cache hit
			moveNodeToFront(request);
			return request.getValue();
		}
		else{ // Cache miss
			misses++;
			U data = dataProvider.get(key);
			if(map.size() >= capacity){ // queue is full, evict least recently used data
				removeNode(last, true);
				last = last.getPrevious();
			}
			map.put(key, new Node<>(key, data));
			push(new Node<>(key, data));
			return data;
		}
	}

	/**
	 * Moves a node from it's current position to the front of the list
	 * @param n Node to be moved to the front of the list
	 */
	private void moveNodeToFront(Node<T, U> n){
		removeNode(n, false);
		push(n);
	}

	/**
	 * Adds a node to the front of the list
	 * @param n Node to be added
	 */
	private void push(Node<T, U> n){
		this.last.setNext(n);
		this.first.setPrevious(n);
		n.setPrevious(last);
		n.setNext(first);
		this.first = n;
	}

	/**
	 * Removes a node from the list & optionally from the map
	 * @param n Node to be removed
	 * @param removeFromMap Boolean argument to also remove the node from the map
	 */
	private void removeNode(Node<T, U> n, boolean removeFromMap){
		n.getPrevious().setNext(n.getNext());
		if(removeFromMap) map.remove(n.getKey());
	}


	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return misses;
	}
}
