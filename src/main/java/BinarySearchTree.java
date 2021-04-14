public class BinarySearchTree {

    public static final class Node {
        volatile public int value;
        volatile public Node left;
        volatile public Node right;

        public Node(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }

        public boolean hasNoDescendant() {
            return this.left == null && this.right == null;
        }

        @Override
        public String toString() {
            String result = "[value: " + this.value;
            result += this.left == null ? "; left descendant: " + null : "; left descendant: " + this.left.value;
            result += this.right == null ? "; right descendant: " + null + "]" : "; right descendant: " + this.right.value + "]";
            return result;
        }
    }

    volatile Node root;

//    public void add(int value) {
//        root = addNode(root, value);
//    }
//
//    private Node addNode(Node node, int value) {
//        if (node == null) return new Node(value);
//        else if (node.value > value) node.left = addNode(node.left, value);
//        else if (node.value < value) node.right = addNode(node.right, value);
//        return node;
//    }
//
//    private Node findRightMaxNode(Node node) {
//        if (node.right == null) return node;
//        else return findRightMaxNode(node.right);
//    }


    public synchronized void add(int value) {
        if (root == null) root = new Node(value);
        Node node = root;
        while (node != null && node.value != value) {
            if (node.value > value) {
                if (node.left == null) {
                    node.left = new Node(value);
                    break;
                } else
                    node = node.left;
            } else {
                if (node.right == null) {
                    node.right = new Node(value);
                    break;
                } else
                    node = node.right;
            }
        }
    }

    private Node findRightMaxNode(Node node) { // finding the maximum right from left member for remove
        Node newNode = node;
        if (newNode == null) return null;
        while (newNode.right != null)
            newNode = newNode.right;
        return newNode;
    }

    public synchronized boolean remove(int value) {

        if (findNode(value) == null) return false;

        Node parent = findParent(value);
        Node leftChild = findLeftDescendant(value);
        Node rightChild = findRightDescendant(value);

        if (root.value == value && root.hasNoDescendant()) root = null;
        else if (root.value == value && rightChild == null && leftChild != null) { // value is root and has 1 left descendant
            root = leftChild;
        } else if (root.value == value && leftChild == null && rightChild != null) { // value is root and has 1 right descendant
            root = rightChild;
        } else if (leftChild == null && rightChild == null) { // value has no descendant
            if (parent.value > value) parent.left = null;
            else parent.right = null;
        } else if (rightChild == null) { // value has 1 left descendant
            if (parent.value > value) parent.left = leftChild;
            else parent.right = leftChild;
        } else if (leftChild == null) { // value has 1 right descendant
            if (parent.value > value) parent.left = rightChild;
            else parent.right = rightChild;
        } else { // value has 2 descendant
            Node rightMaxNode = findRightMaxNode(leftChild);
            assert rightMaxNode != null;
            Node rightMaxNodeParent = findParent(rightMaxNode.value);

            if (root.value == value) {
                root.value = rightMaxNode.value;
                if (rightMaxNodeParent != null)
                    rightMaxNodeParent.right = rightMaxNode.left;
            } else if (leftChild.hasNoDescendant()) {
                leftChild.right = rightChild;
                if (parent.value > value) parent.left = leftChild;
                else parent.right = leftChild;
            } else {
                findNode(value).value = rightMaxNode.value;
                if (rightMaxNodeParent != null)
                    rightMaxNodeParent.right = rightMaxNode.left;
            }
        }
        return true;
    }


//    public Node findParent(int value) {
//        return findParentRecursive(root, value);
//    }
//
//    private Node findParentRecursive(Node node, int value) {
//        if ((node.left != null && node.left.value == value) || (node.right != null && node.right.value == value))
//            return node;
//        else if (node.right != null && node.value < value) return findParentRecursive(node.right, value);
//        else if (node.left != null) return findParentRecursive(node.left, value);
//        else return null;
//    }
//
//    public Node findNode(int value) {
//        return findNodeRecursive(root, value);
//    }
//
//    private Node findNodeRecursive(Node node, int value) {
//        if (node == null || node.value == value) return node;
//        else if (node.value < value) return findNodeRecursive(node.right, value);
//        else return findNodeRecursive(node.left, value);
//    }

    public Node findParent(int value) {
        if (root == null) return null;
        Node node = root;
        while (true) {
            if ((node.left != null && node.left.value == value) || (node.right != null && node.right.value == value))
                break;
            if (node.right != null && node.value < value)
                node = node.right;
            else if (node.left != null)
                node = node.left;
            else {
                node = null;
                break;
            }
            if (node.value == value) break;
        }
        return node;
    }

    public Node findNode(int value) {
        if (root == null) return null;
        Node node = root;
        while (node != null && node.value != value) {
            if (node.value < value) node = node.right;
            else node = node.left;
            if (node.value == value) break;
        }
        return node;
    }

    public Node findLeftDescendant(int value) {
        if (findNode(value) != null) return findNode(value).left;
        else return null;
    }

    public Node findRightDescendant(int value) {
        if (findNode(value) != null) return findNode(value).right;
        else return null;
    }

}