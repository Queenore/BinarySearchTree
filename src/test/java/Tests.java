import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class Tests {

    private BinarySearchTree init() {
        BinarySearchTree tree = new BinarySearchTree();
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
    public void testRemove() {
        BinarySearchTree tree = init();
        tree.remove(31); // remove root node
        assertEquals(7, tree.findNode(14).left.value);
        assertEquals(55, tree.findNode(14).right.value);
        tree.remove(55); // remove node with 2 descendants
        assertEquals(34, tree.findNode(53).left.value);
        assertEquals(60, tree.findNode(53).right.value);
        tree.remove(33); // remove node without descendants
        assertNull(tree.findNode(34).left);
        tree.remove(51); // remove node with 1 descendant
        assertEquals(44, tree.findNode(34).right.value);

        BinarySearchTree newTree = new BinarySearchTree();
        newTree.add(35);
        newTree.add(12);
        newTree.add(7);
        newTree.add(23);
        newTree.remove(12);
        System.out.println(newTree.root);

    }

    @Test
    public void testFindNode() {
        BinarySearchTree tree = init();
        assertEquals(34, tree.findNode(34).value);
        assertEquals(55, tree.findNode(55).value);
        assertEquals(14, tree.findNode(7).right.value);
    }

    @Test
    public void testFindParent() {
        BinarySearchTree tree = init();
        assertEquals(14, tree.findParent(9).value);
        assertEquals(31, tree.findParent(7).value);
        assertEquals(51, tree.findParent(53).value);
    }

    @Test
    public void testFindLeftDescendant() {
        BinarySearchTree tree = init();
        assertEquals(44, tree.findLeftDescendant(51).value);
        assertEquals(4, tree.findLeftDescendant(7).value);
        assertEquals(33, tree.findLeftDescendant(34).value);
    }

    @Test
    public void testFindRightDescendant() {
        BinarySearchTree tree = init();
        assertEquals(55, tree.findRightDescendant(31).value);
        assertEquals(13, tree.findRightDescendant(9).value);
        assertEquals(49, tree.findRightDescendant(44).value);
    }

    public volatile static int count = 0;

    @Test
    public void testConcurrentAdding() {

        BinarySearchTree tree = new BinarySearchTree();
        List<Integer> list = new ArrayList<>();
        Object monitor = new Object();

        list.add(33);
        list.add(23);
        list.add(12);
        list.add(39);
        list.add(36);
        list.add(17);
        list.add(44);
        list.add(2);
        list.add(67);
        list.add(35);
        list.add(5);
        list.add(34);
        list.add(35);
        list.add(37);
        list.add(62);
        list.add(79);
        list.add(81);
        list.add(80);
        list.add(3);
        list.add(16);

        Callable first = () -> {
            for (int i = 0; i < 20; i += 2) {
                synchronized (monitor) {
                    tree.add(list.get(i));
                    count++;
                }
            }
            return 0;
        };

        Callable second = () -> {
            for (int j = 1; j < 20; j += 2) {
                synchronized (monitor) {
                    tree.add(list.get(j));
                    count++;
                }
            }
            return 0;
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<BinarySearchTree> addNodeFirstThread = executor.submit(first);
        Future<BinarySearchTree> addNodeSecondThread = executor.submit(second);

        try {
            addNodeFirstThread.get();
            addNodeSecondThread.get();
            executor.shutdown();
            assertEquals(20, count); // проверка на количество добавленных узлов
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        count = 0;

    }

    @Test
    public void testConcurrentAddAndRemove() {

        BinarySearchTree tree = new BinarySearchTree();
        List<Integer> list = new ArrayList<>();
        Object monitor = new Object();

        list.add(55);
        list.add(12);
        list.add(35);
        list.add(74);
        list.add(7);
        list.add(68);
        list.add(23);
        list.add(72);
        list.add(67);
        list.add(39);
        list.add(5);
        list.add(34);

        Callable<BinarySearchTree> first = () -> {
            synchronized (monitor) {
                tree.add(list.get(0));
                tree.add(list.get(1));
                count += 2;
            }
            for (int i = 2; i < 12; i++) {
                synchronized (monitor) {
                    count++;
                    tree.add(list.get(i));
                    try {
                        tree.remove(list.get(i - 2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        };

        Callable<BinarySearchTree> second = () -> {
            for (int j = 0; j < 12; j++) {
                synchronized (monitor) {
                    count++;
                    tree.findNode(list.get(j));
                }
            }
            return null;
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<BinarySearchTree> addNodeThread = executor.submit(first);
        Future<BinarySearchTree> removeNodeThread = executor.submit(second);

        try {
            addNodeThread.get();
            removeNodeThread.get();
            executor.shutdown();
            assertEquals(24, count);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        count = 0;

    }
}