import java.util.*;

class LFUCache
{
    private final int size;                                   // Size of cache
    private Map<Integer, Integer> cache;                // Stores key-value pairs
    private Map<Integer, LinkedList<Integer>> freqDict; // Stores list of keys for each frequency
    private Map<Integer, Integer> keyFreq;              // Stores frequency of each key
    private int minFreq;                                // Tracks minimum frequency

    // Cache constructor
    public LFUCache(int size)
    {
        this.size = size;
        this.cache = new HashMap<>();
        this.freqDict = new HashMap<>();
        this.keyFreq = new HashMap<>();
        this.minFreq = 0;
    }

    // Get value of a key
    public int get(int key)
    {
        if(!cache.containsKey(key))
        {
            return -1;
        }

        int freq = keyFreq.get(key);    // Get key frequency

        // Create frequency group if needed
        if (!freqDict.containsKey(freq))
        {
            freqDict.put(freq, new LinkedList<>());
        }

        freqDict.get(freq).remove((Integer) key); // Remove key from current group

        // Delete frequency group if empty
        if (freqDict.get(freq).isEmpty())
        {
            freqDict.remove(freq);
            if(freq == minFreq)
            {
                minFreq += 1;
            }
        }

        // Move key to next frequency group
        if (!freqDict.containsKey(freq+1))
        {
            freqDict.put(freq+1, new LinkedList<>());
        }
        freqDict.get(freq+1).add(key);
        keyFreq.put(key, freq+1);

        return cache.get(key);
    }

    // Store a key in the cache
    public void put(int key, int value)
    {
        if (size <= 0)
        {
            return;
        }

        // If key is in cache, update value and frequency
        if (cache.containsKey(key))
        {
            cache.put(key, value);
            get(key); // Update frequency
            return;
        }

        // If cache is full, find and remove the least frequently used key
        if (cache.size() >= size)
        {
            LinkedList<Integer> leastFreqUsed = freqDict.get(minFreq);
            int lfuKey = leastFreqUsed.getFirst();
            cache.remove(lfuKey);
            keyFreq.remove(lfuKey);
            keyFreq.remove(lfuKey);
            leastFreqUsed.removeFirst();
            // If frequency group empty, remove it
            if (leastFreqUsed.isEmpty())
            {
                freqDict.remove(minFreq);
            }
        }

        // Add new key with frequency 1
        cache.put(key, value);
        keyFreq.put(key, 1);

        // Create frequency 1 group in frequency dictionary if it doesn't exist
        if (!freqDict.containsKey(1))
        {
            freqDict.put(1, new LinkedList<>());
        }

        freqDict.get(1).add(key);
        minFreq = 1;
    }

    // Main method for testing
    public static void main(String[] args)
    {
        LFUCache cache = new LFUCache(2);
        cache.put(1, 1);              // Cache: {1=1}
        cache.put(2, 2);              // Cache: {2=2}
        System.out.println(cache.get(1));   // Cache: {1=1, 2=2}, should print 1 and increase 1's frequency to 2
        cache.put(3,3);               // Cache: {1=1, 3=3}, cache is at capacity, and 2 is the LFU, so 3 replaces it
        System.out.println(cache.get(2));   // Cache: {1=1, 3=3}, should print -1
        System.out.println(cache.get(3));   // Cache: {1=1, 3=3}, should print 3 and increase 1's frequency to 2
        cache.put(4, 4);              // Cache: {3=3, 4=4}, cache is at capacity, both 1 and 3 have a frequency of 2, but 1 is the least recently used, so 4 replaces it
        System.out.println(cache.get(4));   // Cache: {3=3, 4=4}, should print 4 and increase 4's frequency to 2
    }
}