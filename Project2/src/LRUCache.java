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
    private Node<T, U> first = null;
    private Node<T, U> last = null;

    /**
     * @param provider the data provider to consult for a cache miss
     * @param capacity the exact number of (key,value) pairs to store in the cache
     */
    public LRUCache(DataProvider<T, U> provider, int capacity) {
        this.misses = 0;
        this.capacity = capacity;
        this.dataProvider = provider;
        map = new HashMap<>();
    }

    /**
     * Returns the value associated with the specified key.
     *
     * @param key the key
     * @return the value associated with the key
     */
    public U get(T key) {
        Node<T, U> request = map.get(key); // Request data from hashmap

        if (request != null) { // Cache hit
            moveNodeToFront(request); // Move requested node to front of nodes in O(1) time by removing specified node, then pushing to front
            return request.getValue();
        }
        else { // Cache miss
            misses++;
            U data = dataProvider.get(key); // Grab requested data from dataprovider
            Node<T, U> n = new Node<>(key, data, last, first); // create new node with data

            if (first == null) { // if this is the first piece of data, initialize tail and head of list
                first = n;
                last = n;
            }
            if (capacity <= 0) // if capacity of the queue is 0 don't attempt to move anything around
                return data;
            else if (map.size() >= capacity) { // queue is full, evict least recently used data
                removeNode(last, true); // remove node from list and map
            }

            map.put(key, n); // add new data to map
            push(n); // add node to front of list
            return data;
        }
    }

    /**
     * Moves a node from it's current position to the front of the list in O(1) time
     *
     * @param n Node to be moved to the front of the list
     */
    private void moveNodeToFront(Node<T, U> n) {
        removeNode(n, false);
        push(n);
    }

    /**
     * Adds a node to the front of the list
     *
     * @param n Node to be added
     */
    private void push(Node<T, U> n) {
        this.last.setNext(n);
        this.first.setPrevious(n);
        this.first = n;
    }

	/*
	O(1) Operation:
	Doubly linked list A <--> B <--> C <--> D <--> E. To remove D:
		Find the previous (C) - O(1)
		Find the next (E) - O(1)
		Change the previous's "next" to D's next (E) - O(1)
		Change the next's "previous" to D's previous (C) - O(1)
	 */

    /**
     * Removes a node from the list & optionally from the map
     *
     * @param n             Node to be removed
     * @param removeFromMap Boolean argument to also remove the node from the map
     */
    private void removeNode(Node<T, U> n, boolean removeFromMap) {
        n.getPrevious().setNext(n.getNext()); // See note above for O(1) explanation
        n.getNext().setPrevious(n.getPrevious());
        if (n == last) last = last.getPrevious(); // ensure last gets reset properly
        if (removeFromMap) map.remove(n.getKey()); // if evicting, remove data from map
    }


    /**
     * Returns the number of cache misses since the object's instantiation.
     *
     * @return the number of cache misses since the object's instantiation.
     */
    public int getNumMisses() {
        return misses;
    }
}
