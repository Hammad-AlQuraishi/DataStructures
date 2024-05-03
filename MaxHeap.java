import java.util.ArrayList;
import java.util.NoSuchElementException;


public class MaxHeap<T extends Comparable<? super T>> {

    /*
     * The initial capacity of the MaxHeap when created with the default
     * constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new MaxHeap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     */
    public MaxHeap() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * You must use the BuildHeap algorithm that was taught in lecture! Simply
     * adding the data one by one using the add method will not get any credit.
     * As a reminder, this is the algorithm that involves building the heap
     * from the bottom up by repeated use of downHeap operations.
     *
     * Before doing the algorithm, first copy over the data from the
     * ArrayList to the backingArray (leaving index 0 of the backingArray
     * empty). The data in the backingArray should be in the same order as it
     * appears in the passed in ArrayList before you start the BuildHeap
     * algorithm.
     *
     * The backingArray should have capacity 2n + 1 (including the empty 0
     * index) where n is the number of data in the passed in ArrayList (not
     * INITIAL_CAPACITY). Index 0 should remain empty, indices 1 to n should
     * contain the data in proper order, and the rest of the indices should
     * be empty.
     *
     * Consider how to most efficiently determine if the list contains null data.
     * 
     * @param data a list of data to initialize the heap with
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public MaxHeap(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("data passed in can't be null");
        }
        int listSize = data.size();
        if (listSize == 0) {
            throw new IllegalArgumentException("data passed in can't be null");
        }
        //build the backing array
        backingArray = (T[]) new Comparable[2 * listSize + 1];
        size = 0;
        backingArray[0] = null;
        for (int i = 0; i < listSize; i++) {
            if (data.get(i) == null) {
                throw new IllegalArgumentException("data inside the list can't be null");
            }
            backingArray[i + 1] = data.get(i);
            size++;
        }

        //apply buildHeap to make it adhere to maxHeap
        for (int i = size / 2; i > 0; i--) {
            buildHeap(i);
        }
    }

    /**
     * buildHeap method to initialize a heap with the ArrayList
     *
     * @param index The index of the array we are verifying for.
     */
    private void buildHeap(int index) {
        //Node has both children
        if (2 * index < backingArray.length && backingArray[2 * index] != null
                && 2 * index + 1 < backingArray.length
                && backingArray[2 * index + 1] != null) {
            //if left child is bigger:
            if (backingArray[2 * index].compareTo(backingArray[2 * index + 1]) > 0
                && backingArray[2 * index].compareTo(backingArray[index]) > 0) {
                T temp = backingArray[2 * index];
                backingArray[2 * index] = backingArray[index];
                backingArray[index] = temp;
                buildHeap(2 * index);
            } else if (backingArray[2 * index].compareTo(backingArray[2 * index + 1]) < 0
                    && backingArray[2 * index + 1].compareTo(backingArray[index]) > 0) {
                //if right child is bigger
                T temp = backingArray[2 * index + 1];
                backingArray[2 * index + 1] = backingArray[index];
                backingArray[index] = temp;
                buildHeap(2 * index + 1);
            }
            //node has left child:
        } else if (2 * index < backingArray.length && backingArray[2 * index] != null) {
            //If only 1 child present
            if (backingArray[2 * index].compareTo(backingArray[index]) > 0) {
                T temp = backingArray[2 * index];
                backingArray[2 * index] = backingArray[index];
                backingArray[index] = temp;
                buildHeap(2 * index);
            }
            //node has right child:
        } else if (2 * index + 1 < backingArray.length && backingArray[2 * index + 1] != null) {
            if (backingArray[2 * index + 1].compareTo(backingArray[index]) > 0) {
                T temp = backingArray[2 * index + 1];
                backingArray[2 * index + 1] = backingArray[index];
                backingArray[index] = temp;
                buildHeap(2 * index + 1);
            }
        }
    }

    /**
     * Adds the data to the heap.
     *
     * If sufficient space is not available in the backing array (the backing
     * array is full except for index 0), resize it to double the current
     * length.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data can't be null");
        }
        //If not enough space
        if (size + 1 == backingArray.length) {
            T[] newBackingArr = (T[]) new Object[backingArray.length * 2];
            newBackingArr[0] = null;
            for (int i = 1; i < backingArray.length; i++) {
                newBackingArr[i] = backingArray[i];
            }
            newBackingArr[backingArray.length] = data;
            size++;
        } else if (size == 0) {
            backingArray[size + 1] = data;
            size++;
        } else {
            backingArray[size + 1] = data;
            size++;
        }

        //Restore the order property
        restoreOrderAdd(size);
    }

    /**
     * Helper function to restore the order property of MaxHeap
     * @param index The index we are checking for
     */
    private void restoreOrderAdd(int index) {
        //check if parent is smaller
        if (index / 2 != 0 && backingArray[index / 2].compareTo(backingArray[index]) < 0) {
            T temp = backingArray[index / 2];
            backingArray[index / 2] = backingArray[index];
            backingArray[index] = temp;
            restoreOrderAdd(index / 2);
        }
    }

    /**
     * Removes and returns the root of the heap.
     *
     * Do not shrink the backing array.
     *
     * Replace any unused spots in the array with null.
     *
     * @return the data that was removed
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T remove() {
        if (size == 0) {
            throw new NoSuchElementException("The heap is already empty");
        }

        T temp = backingArray[1];
        backingArray[1] = backingArray[size];
        backingArray[size] = null;
        size--;

        restoreOrderRemove(1);
        return temp;
    }

    /**
     * Restores the order of the heap after removing an item\
     *
     * @param index The index we are currently fixing the order at
     */
    private void restoreOrderRemove(int index) {
        if (backingArray[2 * index] != null
            && backingArray[2 * index + 1] != null) {
            //if both child are present, check for bigger
            //if left is bigger than right child and the parent:
            if (backingArray[2 * index].compareTo(backingArray[2 * index + 1]) > 0
                && backingArray[2 * index].compareTo(backingArray[index]) > 0) {
                T temp = backingArray[index];
                backingArray[index] = backingArray[2 * index];
                backingArray[2 * index] = temp;
                restoreOrderRemove(2 * index);
            } else if (backingArray[2 * index + 1].compareTo(backingArray[2 * index]) > 0
                    && backingArray[2 * index + 1].compareTo(backingArray[index]) > 0) {
                T temp = backingArray[index];
                backingArray[index] = backingArray[2 * index + 1];
                backingArray[2 * index + 1] = temp;
                restoreOrderRemove(2 * index + 1);
            }
        } else if (backingArray[2 * index] != null) {
            if (backingArray[2 * index].compareTo(backingArray[index]) > 0) {
                T temp = backingArray[index];
                backingArray[index] = backingArray[2 * index];
                backingArray[2 * index] = temp;
                restoreOrderRemove(2 * index);
            }
        } else if (backingArray[2 * index + 1] != null) {
            if (backingArray[2 * index + 1].compareTo(backingArray[index]) > 0) {
                T temp = backingArray[index];
                backingArray[index] = backingArray[2 * index + 1];
                backingArray[2 * index + 1] = temp;
                restoreOrderRemove(2 * index + 1);
            }
        }
    }

    /**
     * Returns the maximum element in the heap.
     *
     * @return the maximum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T getMax() {
        if (size == 0) {
            throw new NoSuchElementException("The heap is empty");
        }
        return backingArray[1];
    }

    /**
     * Returns whether or not the heap is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the heap.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     */
    public void clear() {
        backingArray = (T[]) new Comparable[backingArray.length];
        size = 0;
    }

    /**
     * Returns the backing array of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the list
     */
    public T[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }

    /**
     * Returns the size of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
