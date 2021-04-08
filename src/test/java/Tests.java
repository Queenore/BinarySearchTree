import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
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
        assertEquals(35, newTree.findParent(7).value);

    }

    @Test
    public void testFindNode() {
        BinarySearchTree tree = init();
        assertEquals(34, tree.findNode(34).value);
        assertEquals(31, tree.findNode(31).value);
        assertEquals(55, tree.findNode(55).value);
        assertEquals(14, tree.findNode(7).right.value);
    }

    @Test
    public void testFindParent() {
        BinarySearchTree tree = init();
        assertNull(tree.findParent(31));
        assertNull(tree.findParent(132));
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
    public void testConcurrentAdd() {

        BinarySearchTree tree = new BinarySearchTree();
        final Object monitor = new Object();

        Callable first = () -> {
            for (int i = 0; i < 2000; i++) {
                int digit1 = 1 + (int) (Math.random() * 100);
                tree.add(digit1);
                if (tree.findNode(digit1) != null)
                    synchronized (monitor) {
                        count++;
                    }
            }
            return 0;
        };

        Callable second = () -> {
            for (int j = 0; j < 2000; j++) {
                int digit2 = 1 + (int) (Math.random() * 100);
                tree.add(digit2);
                if (tree.findNode(digit2) != null)
                    synchronized (monitor) {
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
            assertEquals(4000, count); // проверка на количество добавленных узлов
            count = 0;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testConcurrentRemove() {

        BinarySearchTree tree = new BinarySearchTree();
        Set<Integer> set = new HashSet<>();

        Thread first = new Thread(() -> {
            for (int i = 0; i < 2500; i++) {
                int digit1 = 1 + (int) (Math.random() * 2500);
                System.out.println("Первый поток зашел");
                tree.remove(digit1);
                System.out.println("Первый поток вышел");
                set.add(digit1);
            }
        });

        Thread second = new Thread(() -> {
            for (int j = 0; j < 2500; j++) {
                int digit2 = 1 + (int) (Math.random() * 2500);
                System.out.println("Второй поток зашел");
                tree.remove(digit2);
                System.out.println("Второй поток зашел");
                set.add(digit2);
            }
        });


        for (int i = 0; i < 4000; i++) {
            tree.add(1 + (int) (Math.random() * 2500));
            if (i == 3999) System.out.println(i);
        }
        first.start();
        second.start();

        while (first.isAlive() || second.isAlive()) {
            System.out.println("wait");
        }
        for (Integer integer : set)
            if (tree.findNode(integer) == null)
                count++;
        assertEquals(set.size(), count);
        count = 0;
    }

    @Test
    public void testConcurrentAddAndRemove() {

        BinarySearchTree tree = new BinarySearchTree();
        Object monitor = new Object();

        Callable<BinarySearchTree> first = () -> {
            for (int i = 0; i < 2500; i++) {
                int digit1 = 1 + (int) (Math.random() * 500);
                System.out.println("first1");
                tree.add(digit1);
                System.out.println("first2");
                synchronized (monitor) {
                    if (tree.findNode(digit1) != null)
                        count++;
                }
            }
            return null;
        };

        Callable<BinarySearchTree> second = () -> {
            for (int j = 0; j < 2500; j++) {
                int digit2 = 1 + (int) (Math.random() * 500);
                System.out.println("second1");
                tree.remove(digit2);
                System.out.println("second2");
                synchronized (monitor) {
                    if (tree.findNode(digit2) == null)
                        count++;
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
            executor.shutdownNow();
            assertEquals(5000, count);
            count = 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}