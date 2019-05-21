package exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PartialMinHeap extends MyPriorityQueue {
    List<Integer> elements = new ArrayList<>();
    Map<Integer, Double> keys = new HashMap<>();
    Map<Integer, Integer> idToPos = new HashMap<>();

    @Override
    public int removeMin() {
        if(!isEmpty()) {
            if(elements.size() > 1)
                swap(elements.size() - 1, 0);

            int min = elements.remove(elements.size() - 1);
            keys.remove(min);
            idToPos.remove(min);

            if(!isEmpty())
                heapifyDown(0);

            return min;
        }

        return -1;
    }

    @Override
    public void decreaseKey(int id, double key) {
        keys.put(id, key);
        heapifyUp(idToPos.get(id));
    }

    @Override
    public boolean isEmpty() {
        return elements.size() == 0;
    }

    @Override
    public void add(int id, double key) {
        elements.add(id);
        keys.put(id, key);
        idToPos.put(id, elements.size() - 1);

        heapifyUp(elements.size() - 1);
    }

    @Override
    public String name() {
        return "MinHeap";
    }

    protected int size() {
        return elements.size();
    }

    protected abstract void heapifyUp(int i);
    protected abstract void heapifyDown(int i);
    protected abstract int getParent(int i);
    protected abstract int getLeft(int i);
    protected abstract int getRight(int i);

    protected void swap(int i, int j) {
        int tmp = elements.get(j);
        elements.set(j, elements.get(i));
        elements.set(i, tmp);

        idToPos.put(elements.get(i), i);
        idToPos.put(elements.get(j), j);
    }

    protected double keyAt(int i) {
        return keys.get(elements.get(i));
    }
}
