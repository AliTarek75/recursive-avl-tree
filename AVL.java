public class AVL {
    class Node {
        
        int element;
        int height = 0;
        int count = 1;

        Node left;
        Node right;
        Node parent;

        Node(int element) {
            this.element = element;
        }
    }

    Node root;  
    int size = 0;

    // Private Methods
    
    private int height(Node n) {
        return (n != null)? n.height : -1;
    }

    private int newHeight(Node n) {
        return 1 + Math.max(height(n.left), height(n.right));
    }

    private int getBalance(Node n) {
        return height(n.left) - height(n.right);
    }

    private Node rotateRight(Node start) {
        if (start == null || start.left == null) return start;
        
        Node leftNode = start.left;
        
        leftNode.parent = start.parent;
        if (start.parent != null) {
            if (start.parent.left == start) {
                start.parent.left = leftNode;
            }
            else {
                start.parent.right = leftNode;
            }
        }

        Node temp = leftNode.right;

        leftNode.right = start;
        start.parent = leftNode;

        start.left = temp;
        if (temp != null) temp.parent = start;

        // Heights must be updated bottom-up after the rotation
        start.height = newHeight(start);
        leftNode.height = newHeight(leftNode);
        if (leftNode.parent != null) leftNode.parent.height = newHeight(leftNode.parent);

        return leftNode;
    }

    private Node rotateLeft(Node start) {
        if (start == null || start.right == null) return start;
        
        Node rightNode = start.right;
        
        rightNode.parent = start.parent;
        if (start.parent != null) {
            if (start.parent.left == start) {
                start.parent.left = rightNode;
            }
            else {
                start.parent.right = rightNode;
            }
        }

        Node temp = rightNode.left;

        rightNode.left = start;
        start.parent = rightNode;

        start.right = temp;
        if (temp != null) temp.parent = start;

        start.height = newHeight(start);
        rightNode.height = newHeight(rightNode);
        if (rightNode.parent != null) rightNode.parent.height = newHeight(rightNode.parent);

        return rightNode;
    }
    
    private Node balanceNode(Node start) {

        int bf = getBalance(start);

        // LL or LR Cases
        if (bf > 1) {
            if (getBalance(start.left) >= 0) { 
                return rotateRight(start);
            } else {                           
                start.left = rotateLeft(start.left); 
                return rotateRight(start);
            }
        } 
        
        // RR or RL Cases
        if (bf < -1) {
            if (getBalance(start.right) <= 0) { 
                return rotateLeft(start);
            } else {                           
                start.right = rotateRight(start.right); 
                return rotateLeft(start);
            }
        }

        return start;
    }

    private Node insert(int n, Node start) {        
        if (start == null) return new Node(n);

        if (n < start.element) {
            start.left = insert(n, start.left);
            start.left.parent = start;
        }
        else if (n > start.element) {
            start.right = insert(n, start.right);
            start.right.parent = start;
        }
        else start.count++; // Increment count instead of creating duplicate nodes

        start.height = newHeight(start);
        return balanceNode(start);
    }

    private Node min(Node start) {
        if (start.left == null) return start;
        
        return min(start.left);
    }

    private Node max(Node start) {
        if (start.right == null) return start;
        
        return max(start.right);
    }

    private Node deleteMax(Node start) {
        if (start == null) return null;
        if (start.right == null) return start.left;

        start.right = deleteMax(start.right);
        if (start.right != null) start.right.parent = start;
        
        start.height = newHeight(start);
        return balanceNode(start);
    }

    private Node deleteMin(Node start) {
        if (start == null) return null;
        if (start.left == null) return start.right;
        
        start.left = deleteMin(start.left);
        if (start.left != null) start.left.parent = start;
        
        start.height = newHeight(start);
        return balanceNode(start);
    }

    private Node delete(int n, Node start) {
        if (start == null) return null;

        if (n < start.element) start.left = delete(n, start.left);
        else if (n > start.element) start.right = delete(n, start.right);

        else {
            if (start.count > 1) {
                start.count--;
                return start;
            } 

            if (start.right == null) return start.left;
            if (start.left == null) return start.right;   

            // physically swap with predecessor or successor
            
            int rand = (int) (Math.random() * 2);
            if (rand == 0) {
                Node pred = max(start.left);
                start.left = deleteMax(start.left);

                pred.left = start.left;
                pred.right = start.right;
                
                start = pred;

            } else {
                Node succ = min(start.right);
                start.right = deleteMin(start.right);

                succ.left = start.left;
                succ.right = start.right;
                
                start = succ;
            }
        }
        
        if (start.right != null) start.right.parent = start;
        if (start.left != null) start.left.parent = start;

        start.height = newHeight(start);
        return balanceNode(start);
    }

     private Node search(int n, Node start) {
        if (start == null) return null;

        if (n < start.element) return search(n, start.left);
        if (n > start.element) return search(n, start.right);
        else return start;
    }

    private void fillInOrder(Node start, int[] result, int[] index) {
        if (start == null) return;

        fillInOrder(start.left, result, index);

        // Dynamically expand array mapping based on stored duplicates
        for (int i = 0; i < start.count; i++) {
            result[index[0]] = start.element;
            index[0]++; 
        }

        fillInOrder(start.right, result, index);
    }

    private void print(Node start, int depth) {
        if (start == null) return;
        
        print(start.right, depth + 1);

        System.out.println("      ".repeat(depth) + start.element + "(" + start.count + ")");
        
        print(start.left, depth + 1);
    }

    // Public Methods

    public int[] getInOrder() {
        int[] result = new int[size]; 
        int[] index = {0}; // 1-element array to act as a mutable pointer
        
        fillInOrder(root, result, index);
    
        return result;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int popMin() {
        Node minimum = min(root);
        if (minimum != null) {
            delete(minimum.element);
            return minimum.element;
        }
        return 0;
    }

    public int popMax() {
        Node maximum = max(root);
        if (maximum != null) {
            delete(maximum.element);
            return maximum.element;
        }
        return 0;
    }

    public void print() {
        System.out.println("\n----------- Tree States " + " -----------");
        System.out.println("Height: " + height(root));
        System.out.println("Size (Including dublicates): " + size);
        System.out.println("---- Tree Structure (Sideways) " + " ----");
        print(root, 0);
        System.out.println("-------------------------------------\n");
    }

    public void delete(int n) {
        // Prevents decrementing size if the node doesn't exist
        if (search(n, root) != null) {
            size--;
            root = delete(n, root);
            if (root != null) root.parent = null;
        }
    }

    public void insert(int n) {
        size++;
        root = insert(n, root);
    }
    
    public int min() {
        Node minimum = min(root);
        return (minimum != null)? minimum.element : 0;
    }

    public int max() {
        Node maximum = max(root);
        return (maximum != null)? maximum.element : 0;
    }
}

