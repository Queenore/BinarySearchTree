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
        if (node == null) return null;
        while (node.right != null)
            node = node.right;
        return node;
    }

    public synchronized boolean remove(int value) {
        if (findNode(value) == null) return false;

        Node parent = findParent(value);
        Node leftChild = findLeftDescendant(value);
        Node rightChild = findRightDescendant(value);

        if (root.value == value && root.hasNoDescendant())
            root = null;
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
            Node rightMaxNodeParent = findParent(rightMaxNode.value);

            if (root.value == value) {
                if ((leftChild.left != null && leftChild.right == null) || leftChild.hasNoDescendant()) {
                    root.left = leftChild.left;
                    root.value = leftChild.value;
                } else {
                    root.value = rightMaxNode.value;
                    if (rightMaxNodeParent != null)
                        rightMaxNodeParent.right = rightMaxNode.left;
                }
            } else if ((leftChild.left != null && leftChild.right == null) || leftChild.hasNoDescendant()) {
                findNode(value).left = leftChild.left;
                findNode(value).value = leftChild.value;
            } else {
                findNode(value).value = rightMaxNode.value;
                if (rightMaxNodeParent != null)
                    rightMaxNodeParent.right = rightMaxNode.left;
            }
        }
        return true;

    }

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
        }
        return node;
    }


    public synchronized Node findNode(int value) {
        if (root == null) return null;
        Node node = root;
        while (node != null && node.value != value)
            if (node.value < value) node = node.right;
            else node = node.left;
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