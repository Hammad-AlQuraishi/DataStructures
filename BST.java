import java.util.*;


public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("The collection is empty");
        }

        for (T datum: data) {
            if (datum == null) {
                throw new IllegalArgumentException("The data in the collection can't be null");
            } else {
                this.add(datum);
            }
        }
    }

    /**
     * Adds the data to the tree.
     *
     * This must be done recursively.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data passed in can't be null");
        }
        addHelper(data, root);
    }

    /**
     * Helper method for adding data to the tree.
     * @param data The data to be added.
     * @param current The node we are currently at.
     */
    private void addHelper(T data, BSTNode<T> current) {
        BSTNode<T> toAdd = new BSTNode<>(data);
        if (this.size == 0) {
            this.root = toAdd;
            size++;
        } else if (current.getData() == data) { //DEBUG: Should be current.getData().equals(data) ?
            "".isEmpty(); //do nothing if the data is a duplicate
        } else if (current.getLeft() == null
                && current.getData().compareTo(data) > 0) {
            //if current.left is empty and data supposed to go there
            current.setLeft(toAdd);
            size++;
        } else if (current.getRight() == null
                && current.getData().compareTo(data) < 0) {
            //If current.right is empty and data is supposed to go there
            current.setRight(toAdd);
            size++;
        } else if (current.getData().compareTo(data) > 0) {
            //recurse left
            addHelper(data, current.getLeft());
        } else if (current.getData().compareTo(data) < 0) {
            //recurse right
            addHelper(data, current.getRight());
        }
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the predecessor to
     * replace the data. You MUST use recursion to find and remove the
     * predecessor (you will likely need an additional helper method to
     * handle this case efficiently).
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data passed in can't be null");
        }
        BSTNode<T> dummy = new BSTNode<>(data);
        this.root = removeHelper(data, this.root, dummy);
        return dummy.getData();
    }

    /**
     *
     * @param data Data to look for.
     * @param current The current node we are at recursively
     * @param dummy dummy node to store the vaLue of the current
     * @return Returns the node to be removed
     */
    private BSTNode<T> removeHelper(T data, BSTNode<T> current, BSTNode<T> dummy) {
        if (current == null) {
            throw new NoSuchElementException("The data doesn't exist in the BST");
        } else if (data.compareTo(current.getData()) < 0) {
            //If we haven't reached the data
            current.setLeft(removeHelper(data, current.getLeft(), dummy));
        } else if (data.compareTo(current.getData()) > 0) {
            current.setRight(removeHelper(data, current.getRight(), dummy));
        } else {
            dummy.setData(current.getData());
            size--;
            //Three cases when the data is found:
            //If node has  child children:
            if (current.getLeft() == null && current.getRight() == null) {
                return null;
                current.setHeight(calculateHeight(current));
                //If node as atleast 1 child
            } else if (current.getRight() == null) {
                return current.getLeft();
            } else if (current.getLeft() == null) {
                return current.getRight();
            } else { //If node has both child
                //current is the node to be removed
                BSTNode<T> dummy2 = new BSTNode<>(current.getData());
                current.setLeft(removePredecessor(current.getLeft(), dummy2));
                current.setData(dummy2.getData());
            }
        }
        return current;
    }

    /** Helper method to replace the node to be removed with its predecessor
     *
     * @param current2 The left child of the node to be removed in removeHelper
     * @param dummy2 Dummy node to hold the value of the node to be removed
     * @return The predecessor for the node to removed
     */
    private BSTNode<T> removePredecessor(BSTNode<T> current2, BSTNode<T> dummy2)  {
        if (current2.getRight() == null) {
            dummy2.setData(current2.getData()); //Store data of toRemoveNode.left
            return current2.getLeft(); // toRemoveNode.left = toRemoveNode.left.left
        } else {
            current2.setRight(removePredecessor(current2.getRight(), dummy2));
            return current2;
        }
    }

    /**
     * Returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("The data passed in can't be null");
        }
        if (this.size == 0) {
            throw new NoSuchElementException("This BST is empty");
        }
        BSTNode<T> toRet = getHelper(this.root, data);
        if (toRet == null) {
            throw new NoSuchElementException("The data doesn't exist in the BST");
        }
        return toRet.getData();
    }

    /**
     * Helper method to recurse for the get() method.
     * @param current The node at the current level
     * @param data The data we are looking for
     * @return Returns the node containing the data or returns null
     */
    private BSTNode<T> getHelper(BSTNode<T> current, T data) {
        if (current != null && current.getData().compareTo(data) > 0) {
            //If current is bigger than data, recurse left
            return getHelper(current.getLeft(), data);
        } else if (current != null && current.getData().compareTo(data) < 0) {
            //If current is smaller than data, recurse right
            return getHelper(current.getRight(), data);
        } else if (current != null && current.getData().compareTo(data) == 0) {
            //If data is found or reached leaf without finding
            return current;
        } else {
            return null;
        }
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * This must be done recursively.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data passed in can't be null");
        }
        boolean toRet = containsHelper(this.root, data);
        return toRet;
    }

    /**
     * The helper method for the contains method
     *
     * @param current The current node we are checking at the current level
     * @param data data we are looking for
     * @return true if found, false otherwise
     */
    private boolean containsHelper(BSTNode<T> current, T data) {
        if (current.getData().compareTo(data) > 0) {
            //If current is bigger than data, recurse left
            return containsHelper(current.getLeft(), data);
        } else if (current.getData().compareTo(data) < 0) {
            //If current is smaller than data, recurse right
            return containsHelper(current.getRight(), data);
        } else {
            //If data is found
            return true;
        }
    }

    /**
     * Generate a pre-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the preorder traversal of the tree
     */
    public List<T> preorder() {
        List<T> toRet = new ArrayList<>();
        preorderHelper(this.root, toRet);
        return toRet;
    }

    /**
     * Helper method for preorder traversing
     *
     * @param current The current node being traversed
     * @param list The list that the nodes are being added to
     */
    private void preorderHelper(BSTNode<T> current, List<T> list) {
        list.add(current.getData());
        if (current.getLeft() != null) {
            preorderHelper(current.getLeft(), list);
        }
        if (current.getRight() != null) {
            preorderHelper(current.getRight(), list);
        }
    }
    /**
     * Generate an in-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> toRet = new ArrayList<>();
        inorderHelper(this.root, toRet);
        return toRet;
    }

    /**
     *
     * @param current The current node we are at recursively
     * @param list The list we are adding to
     */
    private void inorderHelper(BSTNode<T> current, List<T> list) {
        if (current.getLeft() != null) {
            inorderHelper(current.getLeft(), list);
        }
        list.add(current.getData());
        if (current.getRight() != null) {
            inorderHelper(current.getRight(), list);
        }
    }

    /**
     * Generate a post-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the postorder traversal of the tree
     */
    public List<T> postorder() {
        List<T> toRet = new ArrayList<>();
        postorderHelper(this.root, toRet);
        return toRet;
    }

    /**
     * Helper method for post order traversing
     *
     * @param current The current node we are at recursively
     * @param list The list we are adding to
     */
    private void postorderHelper(BSTNode<T> current, List<T> list) {
        if (current.getLeft() != null) {
            postorderHelper(current.getLeft(), list);
        }
        if (current.getRight() != null) {
            postorderHelper(current.getRight(), list);
        }
        list.add(current.getData());
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * This does not need to be done recursively.
     *
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     *
     * Must be O(n).
     *
     * @return the level order traversal of the tree
     */
    public List<T> levelorder() {
        List<T> toRet = new ArrayList<>();
        if (this.size == 0) {
            return toRet;
        }
        BSTNode<T> current = this.root;
        //instantiate a queue
        //Queue will enqueue at front + size
        LinkedList<BSTNode<T>> queue = new LinkedList<>();
        queue.add(this.root);
        while (queue.size() != 0) {
            current = queue.remove();
            toRet.add(current.getData());
            if (current.getLeft() != null) {
                queue.add(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.add(current.getRight());
            }
        }
        return toRet;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * This must be done recursively.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        } else {
            int toRet = heightHelper(this.root);
            return toRet;
        }
    }

    /**
     *
     * @param current current node we are at recursively
     * @return The total height of the tree
     */
    private int heightHelper(BSTNode<T> current) {
        if (current.getLeft() == null && current.getRight() == null) {
            return 0;
        }
        int leftHeight = 0;
        int rightHeight = 0;
        if (current.getLeft() != null) {
            leftHeight = heightHelper(current.getLeft()) + 1;
        }
        if (current.getRight() != null) {
            rightHeight = heightHelper(current.getRight()) + 1;
        }

        if (rightHeight > leftHeight) {
            return rightHeight;
        } else {
            return leftHeight;
        }
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        if (this.root != null) {
            this.root = null;
            size = 0;
        }
    }

    /**
     * Generates a list of the max data per level from the top to the bottom
     * of the tree. (Another way to think about this is to get the right most
     * data per level from top to bottom.)
     * 
     * This must be done recursively.
     *
     * This list should contain the last node of each level.
     *
     * If the tree is empty, an empty list should be returned.
     *
     * Ex:
     * Given the following BST composed of Integers
     *      2
     *    /   \
     *   1     4
     *  /     / \
     * 0     3   5
     * getMaxDataPerLevel() should return the list [2, 4, 5] - 2 is the max
     * data of level 0, 4 is the max data of level 1, and 5 is the max data of
     * level 2
     *
     * Ex:
     * Given the following BST composed of Integers
     *               50
     *           /        \
     *         25         75
     *       /    \
     *      12    37
     *     /  \    \
     *   11   15   40
     *  /
     * 10
     * getMaxDataPerLevel() should return the list [50, 75, 37, 40, 10] - 50 is
     * the max data of level 0, 75 is the max data of level 1, 37 is the
     * max data of level 2, etc.
     *
     * Must be O(n).
     *
     * @return the list containing the max data of each level
     */
    public List<T> getMaxDataPerLevel() {
        List<T> toRet = new ArrayList<T>();
        if (this.root == null) {
            return toRet;
        }

        getMaxHelper(this.root, toRet, 0);
        return toRet;
    }

    /**
     * Helper method for getting the max value per each level
     *
     * @param current current node we are at recursively
     * @param list The list we are adding to
     * @param level The level of the BST we are currently at
     */
    private void getMaxHelper(BSTNode<T> current, List<T> list, int level) {
        if (current == null) {
            return;
        }
        if (level == list.size()) {
            //if the level's data hasn't been added yet
            list.add(current.getData());
        }
        // Traverse right first, to find the rightmost node at each level
        getMaxHelper(current.getRight(), list, level + 1);
        //Only used when the right node returned a null
        getMaxHelper(current.getLeft(), list, level + 1);
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
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
