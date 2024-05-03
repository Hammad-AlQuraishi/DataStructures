import java.util.*;

import static java.lang.Math.abs;


public class LinearProbingHashMap<K, V> {

    /**
     * The initial capacity of the LinearProbingHashMap when created with the
     * default constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /**
     * The max load factor of the LinearProbingHashMap
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final double MAX_LOAD_FACTOR = 0.67;

    // Do not add new instance variables or modify existing ones.
    private LinearProbingMapEntry<K, V>[] table;
    private int size;

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     *
     * Use constructor chaining.
     */
    public LinearProbingHashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of initialCapacity.
     *
     * You may assume initialCapacity will always be positive.
     *
     * @param initialCapacity the initial capacity of the backing array
     */
    public LinearProbingHashMap(int initialCapacity) {
        table = new LinearProbingMapEntry[initialCapacity];
    }

    /**
     * Adds the given key-value pair to the map. If an entry in the map
     * already has this key, replace the entry's value with the new one
     * passed in.
     *
     * In the case of a collision, use linear probing as your resolution
     * strategy.
     *
     * Before actually adding any data to the HashMap, you should check to
     * see if the array would violate the max load factor if the data was
     * added. For example, let's say the array is of length 5 and the current
     * size is 3 (LF = 0.6). For this example, assume that no elements are
     * removed in between steps. If another entry is attempted to be added,
     * before doing anything else, you should check whether (3 + 1) / 5 = 0.8
     * is larger than the max LF. It is, so you would trigger a resize before
     * you even attempt to add the data or figure out if it's a duplicate. Be
     * careful to consider the differences between integer and double
     * division when calculating load factor. Additionally, be very careful to
     * use the correct types when comparing with LF. Comparing types with
     * different precisions may result in unexpected rounding errors.
     *
     * When regrowing, resize the length of the backing table to
     * 2 * old length + 1. You must use the resizeBackingTable method to do so.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key the key to add
     * @param value the value to add
     * @return null if the key was not already in the map. If it was in the
     * map, return the old value associated with it
     * @throws java.lang.IllegalArgumentException if key or value is null
     */
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("They key or its value can't be null");
        }

        //check if load factor would be reached
        if ((double) (size + 1) / table.length > MAX_LOAD_FACTOR) {
            //resize
            resizeBackingTable(table.length * 2 + 1);
        }
        int hash = key.hashCode();
        int idx = abs(hash % table.length);
        int count = 1;
        int removedIndex = 0;
        boolean removedFound = false;

        while (count <= size) {
            if (table[idx] == null) { //Found the place to put
                break;
            } else if (table[idx].getKey().equals(key) && !table[idx].isRemoved()) { //Duplicate
                V oldValue = table[idx].getValue();
                table[idx].setValue(value);
                return oldValue;
            } else if (table[idx].isRemoved() && !removedFound) { //Found a DEL so keep track
                removedIndex = idx;
                removedFound = true;
            }
            idx = (idx + 1) % table.length;
            count++;
        }
        if (removedFound) {
            table[removedIndex] = new LinearProbingMapEntry<>(key, value);
        } else {
            table[idx] = new LinearProbingMapEntry<>(key, value);
        }
        size++;
        return null;
    }

    /**
     * Removes the entry with a matching key from map by marking the entry as
     * removed.
     *
     * @param key the key to remove
     * @return the value previously associated with the key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Can't pass in null keys");
        }
        int hash = key.hashCode();
        int idx = abs(hash % table.length);
        int h = 0; //Counter so we don't keep on infinitely looping

        while (table[idx] != null && h <= size) {
            //Check for match
            if (table[idx].getKey().equals(key)) {
                if (!table[idx].isRemoved()) {
                    V toRet = table[idx].getValue();
                    table[idx].setRemoved(true);
                    size--;
                    return toRet;
                } else {
                    throw new NoSuchElementException("The key has already been removed");
                }
            }
            h++;
            idx = (idx + h) % table.length;
        }
        throw new NoSuchElementException("The key is not in the map");
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for in the map
     * @return the value associated with the given key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("The key passed in can't be null");
        }
        int hash = key.hashCode();
        int idx = abs(hash % table.length);
        int h = 0;
        while (table[idx] != null && h < size) {
            //check for duplicate
            if (table[idx].getKey().equals(key)) {
                return table[idx].getValue();
            } else {
                if (table[idx] != null || !table[idx].isRemoved()) {
                    h++;
                }
                idx = (idx + 1) % table.length;
            }
        }
        throw new NoSuchElementException("The key doesn't exist in this hashmap");
    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for in the map
     * @return true if the key is contained within the map, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("The key passed in can't be null");
        }
        int hash = key.hashCode();
        int idx = abs(hash % table.length);
        int h = 0;
        while (table[idx] != null && h < size) {
            //check for duplicate
            if (table[idx].getKey().equals(key)) {
                return true;
            }
            h++;
            idx = (idx + 1) % table.length;
        }
        return false;
    }

    /**
     * Returns a Set view of the keys contained in this map.
     *
     * Use java.util.HashSet.
     *
     * @return the set of keys in this map
     */
    public Set<K> keySet() {
        HashSet<K> toRet = new HashSet<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                continue;
            } else if (!table[i].isRemoved()) {
                toRet.add(table[i].getKey());
            }
        }
        return toRet;
    }

    /**
     * Returns a List view of the values contained in this map.
     *
     * Use java.util.ArrayList or java.util.LinkedList.
     *
     * You should iterate over the table in order of increasing index and add
     * entries to the List in the order in which they are traversed.
     *
     * @return list of values in this map
     */
    public List<V> values() {
        if (size == 0) {
            return new ArrayList<>(0);
        }
        ArrayList<V> toRet = new ArrayList<>(table.length);
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                continue;
            } else if (!table[i].isRemoved()) {
                toRet.add(table[i].getValue());
            }
        }
        return toRet;
    }

    /**
     * Resize the backing table to length.
     *
     * Disregard the load factor for this method. So, if the passed in length is
     * smaller than the current capacity, and this new length causes the table's
     * load factor to exceed MAX_LOAD_FACTOR, you should still resize the table
     * to the specified length and leave it at that capacity.
     *
     * You should iterate over the old table in order of increasing index and
     * add entries to the new table in the order in which they are traversed. 
     * You should NOT copy over removed elements to the resized backing table.
     *
     * Since resizing the backing table is working with the non-duplicate
     * data already in the table, you shouldn't explicitly check for
     * duplicates.
     *
     * Hint: You cannot just simply copy the entries over to the new array.
     *
     * @param length new length of the backing table
     * @throws java.lang.IllegalArgumentException if length is less than the
     *                                            number of items in the hash
     *                                            map
     */
    public void resizeBackingTable(int length) {
        if (length < size) {
            throw new IllegalArgumentException("The value of length needs to be bigger than this.size");
        }

        int haveAdded = 0;
        LinearProbingMapEntry<K, V>[] newTable = new LinearProbingMapEntry[length];
        for (int i = 0; i < table.length && haveAdded <= size; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                haveAdded++;
                int idx = table[i].getKey().hashCode() % length;
                while (newTable[idx] != null) {
                    idx = (idx + 1) % length;
                }
                newTable[idx] = table[i];
            }
        }

        table = newTable;
    }
    /**
     * Clears the map.
     *
     * Resets the table to a new array of the INITIAL_CAPACITY and resets the
     * size.
     *
     * Must be O(1).
     */
    public void clear() {
        table = new LinearProbingMapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the table of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the table of the map
     */
    public LinearProbingMapEntry<K, V>[] getTable() {
        // DO NOT MODIFY THIS METHOD!
        return table;
    }

    /**
     * Returns the size of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the map
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
