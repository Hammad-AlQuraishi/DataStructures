# :computer: Java Data Structures Implementation 💻:

This repository showcases the implementation of common data structures in Java. Each data structure is implemented with clean, efficient, and well-documented code to serve as a reference for understanding fundamental concepts and for use in various projects.
Table of Contents:

- Data Structures Implemented
- Usage

## Data Structures Implemented 🏛️

    Circular Singly Linked List: Implementation of a singly linked list.
    Stack: Implementation of a stack using an array.
    Queue: Implementation of a queue using an array and a linked list.
    Binary Tree: Implementation of a binary tree with basic operations.
    Heap: Implementation of a binary heap (min-heap and max-heap).
    Hash Table: Implementation of a hash table using separate chaining for collision resolution.
    Graph: Implementation of a graph using adjacency list representation.
    

## Usage 🏗️

To use any of these data structures in your Java project, follow these steps:

Clone this repository:


    git clone https://github.com/Hammad-AlQuraishi/DataStructures.git

Navigate to the desired data structure directory:


    cd java-data-structures/CircularSinglyLinkedList

Compile the Java files:

    javac *.java

Use the compiled classes in your Java project as such:

    java

    import CircularSinglyLinkedList;

    public class Main {
        public static void main(String[] args) {
            CircularSinglyLinkedList<Integer> list = new CircularSinglyLinkedList<>();
            list.addToFront(1);
            list.addToBack(2);
            System.out.println(list);
        }
    }

Feel free to explore and utilize the other data structures in a similar manner.
