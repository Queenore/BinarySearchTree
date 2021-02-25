public class BinaryTree {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }

        @Override
        public String toString() {
            String result = "[value: " + this.value;
            result += this.left == null ? "; left descendant: " + null: "; left descendant: " + this.left.value;
            result += this.right == null ? "; right descendant: " + null + "]": "; right descendant: " + this.right.value + "]";
            return result;
        }
    }

    Node root;

    private Node addNode(Node node, int value){
        if (node == null) return new Node(value);
        else if (node.value > value) node.left = addNode(node.left, value);
        else if (node.value < value) node.right = addNode(node.right, value);
        return node;
    }

    public void add(int value) {
        root = addNode(root, value);
    }

    private Node findRightMaxNode(Node node) { // finding the maximum right from left member for remove()
        if (node.right == null) return node;
        else return findRightMaxNode(node.right);
    }

    public void remove(int value) {

        if (search(root, value) == null) throw new IllegalArgumentException("The tree does not contain this number");

        Node parent = findParent(value);
        Node leftChild = findNode(value).left == null ? null : findNode(value).left;
        Node rightChild = findNode(value).right == null ? null : findNode(value).right;

        if (root.value == value && rightChild == null && leftChild != null) { // value is root and has 1 left descendant
            root = findNode(value).left;
        }
        else if (root.value == value && leftChild == null && rightChild != null) { // value is root and has 1 right descendant
            root = findNode(value).right;
        }
        else if (leftChild == null && rightChild == null) { // value has no descendant
            if (parent.value > value) parent.left = null;
            else parent.right = null;
        }
        else if (rightChild == null) { // value has 1 left descendant
            if (parent.value > value) parent.left = leftChild;
            else parent.right = leftChild;
        }
        else if (leftChild == null) { // value has 1 right descendant
            if (parent.value > value) parent.left = rightChild;
            else parent.right = rightChild;
        }
        else { // value has 2 descendant
            Node rightMaxNodeParent = searchParent(root, findRightMaxNode(leftChild).value);
            Node rightMaxNodeLeftChild = findRightMaxNode(leftChild).left;

            if (root.value == value) findNode(value).value = findRightMaxNode(leftChild).value;
            else findNode(value).value = findRightMaxNode(findNode(value).left).value;
            rightMaxNodeParent.right = rightMaxNodeLeftChild;
        }

    }

    private Node search(Node node, int value) {
        if (node == null || node.value == value) return node;
        else if (node.value < value) return search(node.right, value);
        else return search(node.left, value);
    }

    private Node searchParent(Node node, int value) {
        if (root.value == value) return null;
        else if ((node.left != null && node.left.value == value) || (node.right != null && node.right.value == value)) return node;
        else if (node.value < value) return searchParent(node.right, value);
        else return searchParent(node.left, value);
    }

    public Node findNode(int value) {
        if (search(root, value) == null) throw new IllegalArgumentException("The tree does not contain the passed number");
        return search(root, value);
    }

    public Node findParent(int value) { return searchParent(root, value); }

    public Node findLeftDescendant(int value) { return search(root, value).left; }

    public Node findRightDescendant(int value) { return search(root, value).right; }

}