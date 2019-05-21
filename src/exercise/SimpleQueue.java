package exercise;

import java.util.ArrayList;
import java.util.Collections;

public class SimpleQueue extends MyPriorityQueue {
    private ArrayList<Integer> elements = new ArrayList<>();
    private ArrayList<Double> keys = new ArrayList<>();

    @Override
    public int removeMin() {
        int pos = keys.indexOf(Collections.min(keys));
        int element = elements.get(pos);

        elements.remove(pos);
        keys.remove(pos);

        return element;
    }

    @Override
    public void decreaseKey(int id, double key) {
        int pos = elements.indexOf(id);
        keys.set(pos, key);
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public void add(int id, double key) {
        elements.add(id);
        keys.add(key);
    }

    @Override
    public String name() {
        return "Simple_Queue";
    }

    @Override
    public boolean isImplemented() {
        return true;
    }
}
