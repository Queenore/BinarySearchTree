import java.util.LinkedList;
import java.util.List;

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

    Node main;
    List<Integer> list = new LinkedList<>();

    Node addNode(Node node, int value){
        if (node == null) return new Node(value);
        else if (node.value > value) node.left = addNode(node.left, value);
        else if (node.value < value) node.right = addNode(node.right, value);
        return node;
    }

    public void add(int value) {
        if (!list.contains(value)) list.add(value);
        main = addNode(main, value);
    }

    Node findRightMaxNode(Node node) { // finding the maximum right from left member for remove()
        if (node.right == null) return node;
        else return findRightMaxNode(node.right);
    }

    public void remove(int value) {

        if (!list.contains(value)) throw new IllegalArgumentException("The tree does not contain this number");

        Node parent = list.get(0) != value ? findParent(value) : null;
        Node leftChild = findNode(value).left == null ? null : findNode(value).left;
        Node rightChild = findNode(value).right == null ? null : findNode(value).right;

        if (list.get(0) == value && rightChild == null && leftChild != null) { // value is main and has 1 left descendant
            main = findNode(value).left;
        }
        else if (list.get(0) == value && leftChild == null && rightChild != null) { // value is main and has 1 right descendant
            main = findNode(value).right;
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
            Node rightMaxNodeParent = recursiveFindNode(main, findRightMaxNode(leftChild).value, "parent");
            Node rightMaxNodeLeftChild = findRightMaxNode(leftChild).left;

            if (list.get(0) == value) findNode(value).value = findRightMaxNode(leftChild).value;
            else findNode(value).value = findRightMaxNode(findNode(value).left).value;
            rightMaxNodeParent.right = rightMaxNodeLeftChild;
        }

        list.removeIf(i -> i == value);

    }

    boolean condition(Node node, int value, String condition) {
        if (condition.equals("descendant")) return  node.value == value;
        else if (condition.equals("parent"))
            return (node.left != null && node.left.value == value) || (node.right != null && node.right.value == value);
        else throw new IllegalArgumentException("wrong condition");
    }

    Node recursiveFindNode(Node node, int value, String condition) { // return parent node
        if (condition(node, value, condition)) return node;
        else if (node.value < value) return recursiveFindNode(node.right, value, condition);
        else return recursiveFindNode(node.left, value, condition);
    }

    public Node findNode(int value) { // return value node
        if (!list.contains(value)) throw new IllegalArgumentException("The tree does not contain the passed number");
        return recursiveFindNode(main, value, "descendant");
    }

    public Node findParent(int value) { // return parent node (with exception)
        if (!list.contains(value) || list.get(0) == value) throw new IllegalArgumentException("The number has no parent");
        return recursiveFindNode(main, value, "parent");
    }

    public Node findLeftDescendant(int value) {
        if (!list.contains(value)) throw new IllegalArgumentException("The tree does not contain the passed number");
        else if (recursiveFindNode(main, value, "descendant").left == null) throw new IllegalArgumentException("This number has no left descendant");
        else return recursiveFindNode(main, value, "descendant").left;
    }

    public Node findRightDescendant(int value) {
        if (!list.contains(value)) throw new IllegalArgumentException("The tree does not contain the passed number");
        else if (recursiveFindNode(main, value, "descendant").right == null) throw new IllegalArgumentException("This number has no right descendant");
        else return recursiveFindNode(main, value, "descendant").right;
    }

}