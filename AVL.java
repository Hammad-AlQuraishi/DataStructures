import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;


public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        for (T datum: data) {
            if (datum == null) {
                throw new IllegalArgumentException("Elements in data cannot be null");
            }
            add(datum);
        }
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Null data can't be added.");
        }
        root = addHelper(data, root);
    }

    /**
     * Recursive helper method to add data to an AVL.
     * Serves as a helper method for add(T data)
     *
     * The method uses pointer reinforcement, and each time we are not
     * dealing with the base case, we will be update the height and the BF,
     * and balancing the tree on our way up.
     *
     * @param data the data to add
     * @param curr the current node
     * @return the correct node
     */
    private AVLNode<T> addHelper(T data, AVLNode<T> curr) {
        if (curr == null) { //Base case; reached leaf
            AVLNode<T> leaf = new AVLNode<>(data);
            size++;
            leaf.setBalanceFactor(0);
            leaf.setHeight(0);
            return leaf;
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(addHelper(data, curr.getLeft()));
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(addHelper(data, curr.getRight()));
        }
        updateHeightBF(curr);
        curr = balance(curr);
        return curr;
    }

    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data, NOT predecessor. As a reminder, rotations can occur
     * after removing the successor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot remove null data");
        }
        AVLNode<T> dummy = new AVLNode<>(null);
        root = removeHelper(data, root, dummy);
        return dummy.getData();
    }
    /**
     * Recursive helper method for remove(T data)
     * @param data the data to remove
     * @param curr the current node
     * @param dummy the dummy node
     * @return the node for the data that was removed
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    private AVLNode<T> removeHelper(T data, AVLNode<T> curr, AVLNode<T> dummy) {
        if (curr == null) {
            throw new NoSuchElementException("Data not found in the tree");
        } else if (data.compareTo(curr.getData()) < 0) {
            curr.setLeft(removeHelper(data, curr.getLeft(), dummy));
        } else if (data.compareTo(curr.getData()) > 0) {
            curr.setRight(removeHelper(data, curr.getRight(), dummy));
        } else {
            dummy.setData(curr.getData());
            size--;
            if (curr.getLeft() == null && curr.getRight() == null) {
                return null;
            } else if (curr.getLeft() == null) {
                return curr.getRight();
            } else if (curr.getRight() == null) {
                return curr.getLeft();
            } else {
                AVLNode<T> dummy2 = new AVLNode<>(null);
                curr.setRight(findSuccessor(curr.getRight(), dummy2));
                curr.setData(dummy2.getData());
            }
        }
        updateHeightBF(curr);
        curr = balance(curr);
        return curr;
    }
    /**
     * Recursive helper method to find the sucessor node.
     * @param curr the current node
     * @param dummy the dummy node
     * @return the correct node
     */
    private AVLNode<T> findSuccessor(AVLNode<T> curr, AVLNode<T> dummy) {
        if (curr.getLeft() == null) {
            dummy.setData(curr.getData());
            if (curr != null) {
                updateHeightBF(curr);
                curr = balance(curr);
            }
            return curr.getRight();
        } else {
            curr.setLeft(findSuccessor(curr.getLeft(), dummy));
            if (curr != null) {
                updateHeightBF(curr);
                curr = balance(curr);
            }
            return curr;
        }
    }
    /**
     * Helper method to balance the AVL tree.
     * @param node the unbalanced node
     * @return the balanced node
     */
    private AVLNode<T> balance(AVLNode<T> node) {
        //right heavy
        if (node.getBalanceFactor() < -1) {
            //left rotation; node.right is either balanced or -1 (right heavy itself)
            if (node.getRight().getBalanceFactor() <= 0) {
                node = rotateLeft(node);
            } else { //node.right is left heavy
                // right left rotation
                node.setRight(rotateRight(node.getRight()));
                node = rotateLeft(node);
            }
            //left heavy
        } else if (node.getBalanceFactor() > 1) {
            //right rotation
            if (node.getLeft().getBalanceFactor() >= 0) {
                node = rotateRight(node);
            } else {
                //left right rotation
                node.setLeft(rotateLeft(node.getLeft()));
                node = rotateRight(node);
            }
        }
        return node;
    }
    /**
     * Helper method to left rotate the AVL tree.
     * @param node the node to rotate
     * @return the rotated node
     *
     * Imagine a right heavy tree:
     *
     *       20
     *      /  \
     *    15    25
     *           \
     *            30
     *           /  \
     *         26    40
     *
     * Assuming we are using pointer reinforcement, returning  30 to 20's right child will rebalance.
     *                                                        /  \
     *                                                      25    40
     *                                                        \
     *                                                         26
     *
     */
    private AVLNode<T> rotateLeft(AVLNode<T> node) {
        //newRoot is 30
        AVLNode<T> newRoot = node.getRight();
        //Save 30's left child (26) as 25's right child
        node.setRight(newRoot.getLeft());
        //Make 25 the left child of 30
        newRoot.setLeft(node);
        //Simply calling update height and BF for 25 and then 30 will always work.
        //Because 25's height and BF will be given by 26, whose height and BF didn't change
        //And 30's height and BF will be given by 25
        updateHeightBF(node);
        updateHeightBF(newRoot);
        return newRoot;
    }
    /**
     * Helper method to right rotate the AVL tree.
     * @param node the imbalanced node being rotated
     * @return the rotated node
     */
    private AVLNode<T> rotateRight(AVLNode<T> node) {
        AVLNode<T> newRoot = node.getLeft();
        node.setLeft(newRoot.getRight());
        newRoot.setRight(node);
        updateHeightBF(node);
        updateHeightBF(newRoot);
        return newRoot;
    }
    /**
     * Helper method to update the height and balance factor of a node.
     * @param node the node to update
     */
    private void updateHeightBF(AVLNode<T> node) {
        if (node == null) {
            return;
        }
        int leftHeight = (node.getLeft() != null) ? node.getLeft().getHeight() : -1;
        int rightHeight = (node.getRight() != null) ? node.getRight().getHeight() : -1;
        node.setHeight(Math.max(leftHeight, rightHeight) + 1);
        node.setBalanceFactor(leftHeight - rightHeight);
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot get null data");
        }
        return getHelper(data, root);
    }

    /**
     * Recursive helper method for get(T data)
     * @param data the data to search for
     * @param curr the current node
     * @return the data in the tree equal to the parameter
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    private T getHelper(T data, AVLNode<T> curr) {
        if (curr == null) {
            throw new NoSuchElementException("Data not found in tree");
        }
        if (curr.getData().equals(data)) {
            return curr.getData();
        } else if (data.compareTo(curr.getData()) < 0) {
            return getHelper(data, curr.getLeft());
        } else {
            return getHelper(data, curr.getRight());
        }
    }
    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null, tree "
                    + "does not contain null data");
        }
        return containsHelper(data, root);
    }
    /**
     * Helper method that returns whether data is in the tree.
     * Recursive helper for contains(T data)
     * @param data the data to search for
     * @param curr the current node
     * @return true if the parameter is contained within the tree, false
     * otherwise
     */
    private boolean containsHelper(T data, AVLNode<T> curr) {
        if (curr == null) {
            return false;
        }
        if (curr.getData().equals(data)) {
            return true;
        } else if (data.compareTo(curr.getData()) < 0) {
            return containsHelper(data, curr.getLeft());
        } else {
            return containsHelper(data, curr.getRight());
        }
    }
    /**
     * Returns the height of the root of the tree.
     *
     * Should be O(1).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        } else {
            return root.getHeight();
        }
    }
    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        root = null;
        size = 0;
    }
    /**
     * Find all elements within a certain distance from the given data.
     * "Distance" means the number of edges between two nodes in the tree.
     *
     * To do this, first find the data in the tree. Keep track of the distance
     * of the current node on the path to the data (you can use the return
     * value of a helper method to denote the current distance to the target
     * data - but note that you must find the data first before you can
     * calculate this information). After you have found the data, you should
     * know the distance of each node on the path to the data. With that
     * information, you can determine how much farther away you can traverse
     * from the main path while remaining within distance of the target data.
     *
     * Use a HashSet as the Set you return. Keep in mind that since it is a
     * Set, you do not have to worry about any specific order in the Set.
     *
     * This must be implemented recursively.
     *
     * Ex:
     * Given the following AVL composed of Integers
     *              50
     *            /    \
     *         25      75
     *        /  \     / \
     *      13   37  70  80
     *    /  \    \      \
     *   12  15    40    85
     *  /
     * 10
     * elementsWithinDistance(37, 3) should return the set {12, 13, 15, 25,
     * 37, 40, 50, 75}
     * elementsWithinDistance(85, 2) should return the set {75, 80, 85}
     * elementsWithinDistance(13, 1) should return the set {12, 13, 15, 25}
     *
     * @param data     the data to begin calculating distance from
     * @param distance the maximum distance allowed
     * @return the set of all data within a certain distance from the given data
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   is the data is not in the tree
     * @throws java.lang.IllegalArgumentException if distance is negative
     */
    public Set<T> elementsWithinDistance(T data, int distance) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        Set<T> result = new HashSet<>();
        elementsWithinDistanceHelper(data, distance, root, result);
        return result;
    }
    /**
     * Recursive helper method for elementsWithinDistance(T data, int distance)
     * @param data the initial data to find the distance from
     * @param currDistance the max distance allowed
     * @param curr current node
     * @param result the set of all data within a certain distance from the given data
     * @return the distance of the current node on the path to the data
     */
    private int elementsWithinDistanceHelper(T data, int currDistance, AVLNode<T> curr, Set<T> result) {
        if (curr == null) {
            throw new NoSuchElementException("Data not found in tree");
        }
        int dist = 0;
        if (data.compareTo(curr.getData()) < 0) {
            dist = 1 + elementsWithinDistanceHelper(data, currDistance, curr.getLeft(), result);
            secondDistanceHelper(curr.getRight(), currDistance - dist - 1, result);
        } else if (data.compareTo(curr.getData()) > 0) {
            dist = 1 + elementsWithinDistanceHelper(data, currDistance, curr.getRight(), result);
            secondDistanceHelper(curr.getLeft(), currDistance - dist - 1, result);
        } else {
            result.add(curr.getData());
            secondDistanceHelper(curr, currDistance, result);
            return 0;
        }
        if (dist <= currDistance) {
            result.add(curr.getData());
        }
        return dist;
    }
    /**
     * Recursive helper method for elementsWithinDistanceHelper method
     * @param curr current node
     * @param currDistance the max distance allowed
     * @param result the set of all data within a certain distance from the given data
     */
    private void secondDistanceHelper(AVLNode<T> curr, int currDistance, Set<T> result) {
        if (curr == null || currDistance < 0) {
            return;
        }
        result.add(curr.getData());
        secondDistanceHelper(curr.getLeft(), currDistance - 1, result);
        secondDistanceHelper(curr.getRight(), currDistance - 1, result);
    }
    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
