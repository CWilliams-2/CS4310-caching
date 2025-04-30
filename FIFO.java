import java.util.HashMap;

public class FIFOCache {

    private final int CACHE_SIZE;
    private final HashMap<Integer, Integer> cache; // key = file fragment/page/block ID, value = data (or same as key)
    private final int[] order; // to simulate queue manually
    private int front, rear, count;

    public FIFOCache(int capacity) {
        this.CACHE_SIZE = capacity;
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
        if (count == CACHE_SIZE) {
            int oldestKey = order[front];
            cache.remove(oldestKey);
            front = (front + 1) % CACHE_SIZE;
            count--;
        }

        // Add new key to cache and tracking array
        cache.put(key, value);
        order[rear] = key;
        rear = (rear + 1) % CACHE_SIZE;
        count++;
    }

    // For debugging or display
    public void printCache() {
        System.out.print("FIFO Cache Contents: ");
        int idx = front;
        for (int i = 0; i < count; i++) {
            System.out.print(order[idx] + " ");
            idx = (idx + 1) % CACHE_SIZE;
        }
        System.out.println();
    }
}
