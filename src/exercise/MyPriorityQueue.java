package exercise;

public abstract class MyPriorityQueue {
    public abstract int removeMin();
	public abstract void decreaseKey(int id, double key);
	public abstract boolean isEmpty();
	public abstract void add(int id, double key);

	public abstract String name();
	public abstract boolean isImplemented();
}
