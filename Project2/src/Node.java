public class Node<T, U> {
    private T key;
    private U value;
    private Node<T, U> previous;
    private Node<T, U> next;

    /**
     * Node holds a key and a value and is a reimplementation of a bi-directional linked list
     * @param key key of type T to find the node
     * @param value value of type U
     * @param prev previous node in list
     * @param next next node in list
     */
    Node(T key, U value, Node<T, U> prev, Node<T, U> next){
        this.key = key;
        this.value = value;
        this.previous = prev;
        this.next = next;
    }

    /**
     * Sets the previous node
     * @param n previous node
     */
    void setPrevious(Node<T, U> n){
        this.previous = n;
    }

    /**
     * Sets the next node
     * @param n next node
     */
    void setNext(Node<T, U> n){
        this.next = n;
    }

    /**
     * Retrieves the previous node
     * @return previous node
     */
    Node<T, U> getPrevious(){
        return this.previous;
    }

    /**
     * Retrieves the next node
     * @return next node
     */
    Node<T, U> getNext(){
        return this.next;
    }

    /**
     * Retrieves the stored key
     * @return key of type T
     */
    T getKey(){
        return this.key;
    }

    /**
     * Retrieves the stored value
     * @return value of type U
     */
    U getValue(){
        return this.value;
    }
}
