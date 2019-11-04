import java.util.LinkedList;

class Node<T, U> extends LinkedList {
    private T key;
    private U value;
    private Node<T, U> previous = this;
    private Node<T, U> next = this;

    Node(T key, U value){
        this.key = key;
        this.value = value;
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
