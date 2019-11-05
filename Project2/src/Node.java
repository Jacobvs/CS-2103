class Node<T, U> {
    private T key;
    private U value;
    private Node<T, U> previous;
    private Node<T, U> next;

    Node(T key, U value, Node prev, Node next){
        this.key = key;
        this.value = value;
        this.previous = prev;
        this.next = next;
    }

    void setPrevious(Node<T, U> n){
        this.previous = n;
    }

    void setNext(Node<T, U> n){
        this.next = n;
    }

    Node<T, U> getPrevious(){
        return this.previous;
    }

    Node<T, U> getNext(){
        return this.next;
    }

    T getKey(){
        return this.key;
    }

    U getValue(){
        return this.value;
    }
}
