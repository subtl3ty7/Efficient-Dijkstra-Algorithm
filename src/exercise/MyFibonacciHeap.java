package exercise;

import org.jgrapht.util.FibonacciHeap;
import org.jgrapht.util.FibonacciHeapNode;

import java.util.HashMap;
import java.util.Map;

public class MyFibonacciHeap extends MyPriorityQueue {
    FibonacciHeap<Integer> Q = new FibonacciHeap<>();
    Map<Integer, FibonacciHeapNode<Integer>> idToNode = new HashMap<>();

    @Override
    public int removeMin() {
        return Q.removeMin().getData();
    }

    @Override
    public void decreaseKey(int id, double key) {
        Q.decreaseKey(idToNode.get(id), key);
    }

    @Override
    public boolean isEmpty() {
        return Q.isEmpty();
    }

    @Override
    public void add(int id, double key) {
        FibonacciHeapNode<Integer> node = new FibonacciHeapNode<>(id);
        Q.insert(node, key);
        idToNode.put(id, node);
    }

    public String name() {return "Fibonacci_Heap";}

    @Override
    public boolean isImplemented() {
        return true;
    }
}
