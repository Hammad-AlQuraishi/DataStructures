import java.util.NoSuchElementException;


public class LinkedQueue<T> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private LinkedNode<T> head;
    private LinkedNode<T> tail;
    private int size;

    /*
     * Do not add a constructor.
     */

    /**
     * Adds the data to the back of the queue.
     *
     * Must be O(1).
     *
     * @param data the data to add to the back of the queue
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void enqueue(T data) {
        //Enqueues at tail
        if (data == null) {
            throw new IllegalArgumentException("data passed in can't be null");
        }

        LinkedNode<T> toAdd = new LinkedNode<>(data);
        if (this.size == 0) {
            this.head = toAdd;
            this.tail = toAdd;
            size++;
        } else {
            this.tail.setNext(toAdd);
            tail = this.tail.getNext();
            size++;
        }
    }

    /**
     * Removes and returns the data from the front of the queue.
     *
     * Must be O(1).
     *
     * @return the data formerly located at the front of the queue
     * @throws java.util.NoSuchElementException if the queue is empty
     */
    public T dequeue() {
        if (this.size == 0) {
            throw new NoSuchElementException("The queue is already empty");
        } else if (this.size == 1) {
            T toRet = head.getData();
            this.head = null;
            this.tail = null;
            size--;
            return toRet;
        } else {
            T toRet = head.getData();
            this.head = this.head.getNext();
            size--;
            return toRet;
        }
    }

    /**
     * Returns the data from the front of the queue without removing it.
     *
     * Must be O(1).
     *
     * @return the data located at the front of the queue
     * @throws java.util.NoSuchElementException if the queue is empty
     */
    public T peek() {
        if (this.size == 0) {
            throw new NoSuchElementException("The queue is completely empty");
        }

        return this.head.getData();
    }

    /**
     * Returns the head node of the queue.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the head of the queue
     */
    public LinkedNode<T> getHead() {
        // DO NOT MODIFY THIS METHOD!
        return head;
    }

    /**
     * Returns the tail node of the queue.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the node at the tail of the queue
     */
    public LinkedNode<T> getTail() {
        // DO NOT MODIFY THIS METHOD!
        return tail;
    }

    /**
     * Returns the size of the queue.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the queue
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
