import static org.junit.Assert.*;
import org.junit.Test;

public class Tests {

    private BinaryTree init() {
        BinaryTree tree = new BinaryTree();
        tree.add(31);
        tree.add(7);
        tree.add(14);
        tree.add(55);
        tree.add(34);
        tree.add(9);
        tree.add(51);
        tree.add(4);
        tree.add(13);
        tree.add(60);
        tree.add(33);
        tree.add(44);
        tree.add(49);
        tree.add(53);
        return tree;
    }

    @Test
    public void testException() {
        BinaryTree tree = init();
        boolean exception = false;

        try {
            tree.findLeftDescendant(4);
        } catch (IllegalArgumentException e) {
            exception = true;
        }

        assertTrue(exception);
        exception = false;

        try {
            tree.findParent(31);
        } catch (IllegalArgumentException e) {
            exception = true;
        }

        assertTrue(exception);
        exception = false;

        try {
            tree.remove(22);
        } catch (IllegalArgumentException e) {
            exception = true;
        }

        assertTrue(exception);
    }

    @Test
    public void testRemove() {
        BinaryTree tree = init();
        tree.remove(31); // remove main node
        assertEquals(7, tree.findNode(14).left.value);
        assertEquals(55, tree.findNode(14).right.value);
        tree.remove(55); // remove node with 2 descendants
        assertEquals(34, tree.findNode(53).left.value);
        assertEquals(60, tree.findNode(53).right.value);
        tree.remove(33); // remove node without descendants
        assertNull(tree.findNode(34).left);
        tree.remove(51); // remove node with 1 descendant
        assertEquals(44, tree.findNode(34).right.value);
    }

    @Test
    public void testFindNode() {
        BinaryTree tree = init();
        assertEquals(34, tree.findNode(34).value);
        assertEquals(55, tree.findNode(55).value);
        assertEquals(14, tree.findNode(7).right.value);
    }
    @Test
    public void testFindParent() {
        BinaryTree tree = init();
        assertEquals(14, tree.findParent(9).value);
        assertEquals(31, tree.findParent(7).value);
        assertEquals(51, tree.findParent(53).value);
    }
    @Test
    public void testFindLeftDescendant() {
        BinaryTree tree = init();
        assertEquals(44, tree.findLeftDescendant(51).value);
        assertEquals(4, tree.findLeftDescendant(7).value);
        assertEquals(33, tree.findLeftDescendant(34).value);
    }
    @Test
    public void testFindRightDescendant() {
        BinaryTree tree = init();
        assertEquals(55, tree.findRightDescendant(31).value);
        assertEquals(13, tree.findRightDescendant(9).value);
        assertEquals(49, tree.findRightDescendant(44).value);
    }

}