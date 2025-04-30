import java.text.FieldPosition;
import java.util.HashMap;

public class FIFOCache implements Cache {

    private final int capacity;
    private final HashMap<Integer, Integer> cache; // key = file fragment/page/block ID, value = data (or same as key)
    private final int[] order; // to simulate queue manually
    private int front, rear, count;

    public FIFOCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.order = new int[capacity];
        this.front = 0;
        this.rear = 0;
        this.count = 0;
    }

    public int get(int key) {
        if (cache.containsKey(key)) {
            // Cache hit
            return cache.get(key);
        } else {
            // Cache miss
            return -1;
        }
    }

    public void put(int key, int value) {
        // If key already exists, do nothing (FIFO doesn’t reorder)
        if (cache.containsKey(key)) {
            return;
        }

        // If cache is full, evict oldest
        if (count == capacity) {
            int oldestKey = order[front];
            cache.remove(oldestKey);
            front = (front + 1) % capacity;
            count--;
        }

        // Add new key to cache and tracking array
        cache.put(key, value);
        order[rear] = key;
        rear = (rear + 1) % capacity;
        count++;
    }

    // For debugging or display
    public void print() {
        System.out.print("FIFO Cache Contents: ");
        int idx = front;
        for (int i = 0; i < count; i++) {
            System.out.print(order[idx] + " ");
            idx = (idx + 1) % capacity;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        FIFOCache cache = new FIFOCache(10);
        cache.put(5, 5);
        cache.get(0);
        cache.print();
        cache.get(5);
        cache.print();

        cache.put(1, 1);
        cache.print();
    }
}
