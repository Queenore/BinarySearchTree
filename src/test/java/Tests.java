import static org.junit.Assert.*;

import org.junit.Test;

//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.*;


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

        BinarySearchTree firstTree = init();
        firstTree.remove(31); // remove root node
        assertEquals(7, firstTree.findNode(14).left.value);
        assertEquals(55, firstTree.findNode(14).right.value);
        firstTree.remove(55); // remove node with 2 descendants
        assertEquals(34, firstTree.findNode(53).left.value);
        assertEquals(60, firstTree.findNode(53).right.value);
        firstTree.remove(33); // remove node without descendants
        assertNull(firstTree.findNode(34).left);
        firstTree.remove(51); // remove node with 1 descendant
        assertEquals(44, firstTree.findNode(34).right.value);

        BinarySearchTree secondTree = new BinarySearchTree();
        secondTree.add(35);
        secondTree.add(12);
        secondTree.add(7);
        secondTree.add(23);
        secondTree.remove(12);
        assertEquals(35, secondTree.findParent(7).value);

        BinarySearchTree thirdTree = new BinarySearchTree();
        thirdTree.add(12);
        thirdTree.add(27);
        thirdTree.add(44);
        thirdTree.add(24);
        thirdTree.add(22);
        thirdTree.remove(27);
        assertEquals(22, thirdTree.findLeftDescendant(24).value);

        BinarySearchTree fourthTree = new BinarySearchTree();
        fourthTree.add(13);
        fourthTree.add(8);
        fourthTree.add(44);
        fourthTree.remove(13);
        assertEquals(8, fourthTree.root.value);

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

//    public int count = 0;
//    public final Object monitor = new Object();
//
//    @Test
//    public void testConcurrentAdd() {
//
//        BinarySearchTree tree = new BinarySearchTree();
//        final Object monitor = new Object();
//
//        Callable<BinarySearchTree> first = () -> {
//            for (int i = 0; i < 2000; i++) {
//                int digit1 = 1 + (int) (Math.random() * 100);
//                tree.add(digit1);
//                if (tree.findNode(digit1) != null)
//                    synchronized (monitor) {
//                        count++;
//                    }
//            }
//            return null;
//        };
//
//        Callable<BinarySearchTree> second = () -> {
//            for (int j = 0; j < 2000; j++) {
//                int digit2 = 1 + (int) (Math.random() * 100);
//                tree.add(digit2);
//                if (tree.findNode(digit2) != null)
//                    synchronized (monitor) {
//                        count++;
//                    }
//            }
//            return null;
//        };
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        Future<BinarySearchTree> addNodeFirstThread = executor.submit(first);
//        Future<BinarySearchTree> addNodeSecondThread = executor.submit(second);
//
//        try {
//            addNodeFirstThread.get();
//            addNodeSecondThread.get();
//            executor.shutdown();
//            assertEquals(4000, count); // проверка на количество добавленных узлов
//            count = 0;
//        } catch (ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    public void testConcurrentRemove(){
//
//        BinarySearchTree tree = new BinarySearchTree();
//        Set<Integer> set = new HashSet<>();
//        for (int i = 0; i < 4000; i++)
//            tree.add(1 + (int) (Math.random() * 2500));
//
//        Callable<BinarySearchTree> first = () -> {
//            for (int i = 0; i < 2500; i++) {
//                int digit1 = 1 + (int) (Math.random() * 2500);
//                tree.remove(digit1);
//                synchronized (monitor) {
//                    set.add(digit1);
//                }
//            }
//            return null;
//        };
//
//        Callable<BinarySearchTree> second = () -> {
//            for (int j = 0; j < 2500; j++) {
//                int digit2 = 1 + (int) (Math.random() * 2500);
//                tree.remove(digit2);
//                synchronized (monitor) {
//                    set.add(digit2);
//                }
//            }
//            return null;
//        };
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        Future<BinarySearchTree> addNodeThread = executor.submit(first);
//        Future<BinarySearchTree> removeNodeThread = executor.submit(second);
//
//        try {
//            addNodeThread.get();
//            removeNodeThread.get();
//            executor.shutdownNow();
//            for (Integer integer : set)
//                if (tree.findNode(integer) == null)
//                    count++;
//            assertEquals(set.size(), count); // проверка на удаленные узлы
//            count = 0;
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testConcurrentAddAndRemove() {
//
//        BinarySearchTree tree = new BinarySearchTree();
//        Object monitor = new Object();
//        Set<Integer> set = new HashSet<>();
//
//        Callable<BinarySearchTree> first = () -> {
//            for (int i = 0; i < 2500; i++) {
//                int digit1 = 1 + (int) (Math.random() * 500);
//                tree.add(digit1);
//                synchronized (monitor) {
//                    set.add(digit1);
//                }
//            }
//            return null;
//        };
//
//        Callable<BinarySearchTree> second = () -> {
//            for (int j = 0; j < 2500; j++) {
//                int digit2 = 1 + (int) (Math.random() * 500);
//                tree.remove(digit2);
//                synchronized (monitor) {
//                    set.remove(digit2);
//                }
//            }
//            return null;
//        };
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        Future<BinarySearchTree> addNodeThread = executor.submit(first);
//        Future<BinarySearchTree> removeNodeThread = executor.submit(second);
//
//        try {
//            addNodeThread.get();
//            removeNodeThread.get();
//            executor.shutdownNow();
//            for (Integer integer : set)
//                if (tree.findNode(integer) != null)
//                    count++;
//            assertEquals(set.size(), count);
//            count = 0;
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//
//    }

}