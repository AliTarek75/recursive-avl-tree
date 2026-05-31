# recursive-avl-tree

A personal coding project to build a self-balancing AVL Tree in Java from scratch. To keep it clean and avoid complex `while` loops, the balancing engine leans heavily on recursion. It uses the Java call stack to naturally track parent nodes, updating heights and triggering rotations exactly where needed as the recursive stack works.

### Core Features

* **Recursive Auto-Balancing:** Dynamically handles all four rotation cases (LL, RR, LR, RL) on the way up the tree to guarantee $O(\log n)$ time complexity.
* **Duplicate Handling:** Instead of injecting duplicate physical nodes and ruining the tree's depth, it uses an internal `count` variable to track multiples.
* **Terminal Visualization:** Includes a reverse in-order printing method to easily visualize the tree's structure and depth rotated sideways in the console.

### Example Usage

```java
AVL tree = new AVL();
tree.insert(50);
tree.insert(25);
tree.insert(75);
tree.insert(25); // Safely increments the duplicate count
tree.insert(5); 

int minimum = tree.popMin();
tree.print(); // Prints sideways
int[] sorted = tree.getInOrder(); // Returns [25, 25, 50, 75]

```

### Critical Constraint

This implementation is strictly hardcoded to store primitive `int` values. It does not use Java Generics (`<T extends Comparable<T>>`), meaning it cannot be used out-of-the-box to balance custom objects, strings, or other data types without refactoring the `Node` class.
