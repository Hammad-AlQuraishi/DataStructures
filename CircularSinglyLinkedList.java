import java.util.NoSuchElementException;

public class CircularSinglyLinkedList<T> {


    private CircularSinglyLinkedListNode<T> head;
    private int size;

	public CircularSinglyLinkedList<T>() {
		this.head = null;
		this.size = 0;
    }

    /**
     * Adds the data to the specified index.
     *
     * Must be O(1) for indices 0 and size and O(n) for all other cases.
     *
     * @param index the index at which to add the new data
     * @param data  the data to add at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index > size
     * @throws java.lang.IllegalArgumentException  if data is null
     */
    public void addAtIndex(int index, T data) {
        //Error Checking
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index can't be negative or bigger than size");
        } else if (data == null) {
            throw new IllegalArgumentException("Data can't be null");
        }

        CircularSinglyLinkedListNode<T> toBeSet = new CircularSinglyLinkedListNode<>(data, null);
        //If data is to be added at head
        if (this.size == 0) {
            this.head = toBeSet;
            this.head.setNext(this.head);
            size++;
        } else if (index == 0) {
            toBeSet.setNext(this.head.getNext());
            this.head.setNext(toBeSet);
            toBeSet.setData(this.head.getData());
            this.head.setData(data);
            size++;
        } else if (index == this.size) {
            toBeSet.setNext(this.head.getNext());
            this.head.setNext(toBeSet);
            toBeSet.setData(this.head.getData());
            this.head.setData(data);
            this.head = toBeSet;
            size++;
        } else {
            CircularSinglyLinkedListNode<T> current = this.head;
            for (int i = 1; i < index; i++) {
                current = current.getNext();
            }
            toBeSet.setNext(current.getNext());
            current.setNext(toBeSet);
            size++;
        }
    }

    /**
     * Adds the data to the front of the list.
     *
     * O(1).
     *
     * @param data the data to add to the front of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null");
        }
        CircularSinglyLinkedListNode<T> toBeSet = new CircularSinglyLinkedListNode<>(data, null);
        if (this.size == 0) {
            this.head = toBeSet;
            this.head.setNext(this.head);
            size++;
        } else {
            //Insert a new node at index 1
            toBeSet.setNext(this.head.getNext());
            this.head.setNext(toBeSet);
            //Set the data of new node to be that of the current head
            toBeSet.setData(this.head.getData());
            //Set the data of the head to be the new data so that the new data's node becomes the head
            this.head.setData(data);
            size++;
        }
    }

    /**
     * Adds the data to the back of the list.
     *
     * O(1).
     *
     * @param data the data to add to the back of the list
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null");
        }

        CircularSinglyLinkedListNode<T> toBeSet = new CircularSinglyLinkedListNode<>(data, null);

        if (this.size == 0) {
            this.head = toBeSet;
            this.head.setNext(this.head);
            size++;
        } else {
            //Insert a new node at index 1
            toBeSet.setNext(this.head.getNext());
            this.head.setNext(toBeSet);
            //Set new node's data from head's data
            toBeSet.setData(this.head.getData());
            //Set head's data to the new data
            this.head.setData(data);
            //Move the head to point to the new node, resulting in old head being the new end node of the list
            this.head = toBeSet;
            size++;
        }
    }

    /**
     * Removes and returns the data at the specified index.
     *
     * Is O(1) for index 0 and O(n) for all other cases.
     *
     * @param index the index of the data to remove
     * @return the data formerly located at the specified index
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index can't be negative or bigger than size");
        }
        CircularSinglyLinkedListNode<T> current = this.head;
        if (index == 0) {
            if (this.size == 1) {
                T toRet = this.head.getData();
                this.head = null;
                size--;
                return toRet;
            } else {
                T toRet = this.head.getData();
                this.head.setData(this.head.getNext().getData());
                this.head.setNext(this.head.getNext().getNext());
                size--;
                return toRet;
            }
        }

        for (int i = 1; i < index; i++) {
            current = current.getNext();
        }
        CircularSinglyLinkedListNode<T> toRet = current.getNext();
        current.setNext(toRet.getNext());
        size--;
        return toRet.getData();
    }

    /**
     * Removes and returns the first data of the list.
     *
     * O(1).
     *
     * @return the data formerly located at the front of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromFront() {
        if (this.size == 0) {
            throw new NoSuchElementException("The list is already empty");
        } else if (this.size == 1) {
            T toRet = this.head.getData();
            this.head = null;
            size--;
            return toRet;
        } else if (this.size == 2) {
            T toRet = this.head.getData();
            this.head = this.head.getNext(); //Move the head ahead
            this.head.setNext(this.head); //Make the new head point to itself
            size--;
            return toRet;
        } else {
            T toRet = this.head.getData();
            T newHead = this.head.getNext().getData();
            this.head.setData(newHead);
            this.head.setNext(this.head.getNext().getNext());
            size--;
            return toRet;
        }
    }

    /**
     * Removes and returns the last data of the list.
     *
     * O(n).
     *
     * @return the data formerly located at the back of the list
     * @throws java.util.NoSuchElementException if the list is empty
     */
    public T removeFromBack() {
        if (this.size == 0) {
            throw new NoSuchElementException("The list is already empty");
        } else if (this.size == 1) {
            T toRet = this.head.getData();
            this.head = null;
            size--;
            return toRet;
        }
        CircularSinglyLinkedListNode<T> current = this.head;
        for (int i = 1; i < this.size - 1; i++) {
            current = current.getNext();
        }
        T toRet = current.getNext().getData();
        current.setNext(this.head);
        size--;
        return toRet;
    }

    /**
     * Returns the data at the specified index.
     *
     * Is O(1) for index 0 and O(n) for all other cases.
     *
     * @param index the index of the data to get
     * @return the data stored at the index in the list
     * @throws java.lang.IndexOutOfBoundsException if index < 0 or index >= size
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index can't be less than 0 or greater than size - 1");
        }
        if (index == 0) {
            return this.head.getData();
        } else {
            CircularSinglyLinkedListNode<T> current = this.head;
            for (int i = 1; i <= index; i++) {
                current = current.getNext();
            }
            return current.getData();
        }
    }

    /**
     * Returns whether or not the list is empty.
     *
     * O(1).
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Clears the list.
     *
     * Clears all data and resets the size.
     *
     * O(1).
     */
    public void clear() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Removes and returns the last copy of the given data from the list.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the list.
     *
     * O(n).
     *
     * @param data the data to be removed from the list
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if data is not found
     */
    public T removeLastOccurrence(T data) {
        if (data == null) {
            throw new IllegalArgumentException("datsa can't be null");
        }
        CircularSinglyLinkedListNode<T> current = this.head;
        CircularSinglyLinkedListNode<T> nodeBefore = new CircularSinglyLinkedListNode<>(data);


        //Check the rest of the nodes
        for (int i = 0; i < this.size; i++) {
            if (current.getNext().getData().equals(data)) {
                nodeBefore = current;
            }
            current = current.getNext();
        }
        if (nodeBefore.getNext() == null) {
            throw new NoSuchElementException("The data doesn't exist in the list");
        } else if (nodeBefore.getData().equals(this.head.getData())) {
            return removeFromFront();
        } else {
            T toRet = nodeBefore.getNext().getData();
            nodeBefore.setNext(nodeBefore.getNext().getNext());
            size--;
            return toRet;
        }
    }

    /**
     * Returns an array representation of the linked list.
     *
     * O(n)
     *
     * @return the array of length size holding all of the data (not the
     * nodes) in the list in the same order
     */
    public T[] toArray() {
        T[] arrToRet = (T[]) new Object[this.size];
        if (this.size == 0) {
            return arrToRet;
        }
        CircularSinglyLinkedListNode<T> current = this.head;
        arrToRet[0] = current.getData();
        for (int i = 1; i < this.size; i++) {
            current = current.getNext();
            arrToRet[i] = current.getData();
        }
        return arrToRet;
    }

    /**
     * Returns the head node of the list.
     *
     * @return the node at the head of the list
     */
    public CircularSinglyLinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }

    /**
     * Returns the size of the list.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY!
        return size;
    }
}
