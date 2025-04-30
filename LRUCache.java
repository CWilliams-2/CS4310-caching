import java.util.*;

/**
 * Represents a node in a doubly linked list, used for implementing the LRU Cache.
 * Each node contains a key (representing the ID of a page) and pointers to the
 * next and previous nodes in the list.
 **/
class Node{
    int key; // ID of the page
    public Node next;
    public Node prev;
    public Node(int key) {
        this.next = null;
        this.prev = null;
        this.key = key;
    }
}

/**
 * A DoublyLinkedList class that maintains a doubly linked list structure
 * with dummy head and tail nodes. This implementation is designed to support
 * operations for an LRU (Least Recently Used) Cache.
 * Most Recently Used (MRU) at the head and Least Recently Used (LRU) at the tail.
 * 
 * Features:
 * - Maintains a count of the number of nodes in the list.
 * - Supports adding, removing, and moving nodes to the front of the list.
 * - Provides access to the Least Recently Used (LRU) node.
 * - Includes a helper method for debugging and printing the list state.
 **/
class DoublyLinkedList{
    private Node head;  // Points to the Most Recently Used (MRU) node
    private Node tail;  // Points to the Least Recently Used (LRU) node
    private int count;  // Number of nodes in the list

    // Constructor to initialize the list with dummy head and tail nodes
    public DoublyLinkedList() {
        head = new Node(-1); // Dummy head node
        tail = new Node(-1); // Dummy tail node
        head.next = tail;
        tail.prev = head;
        this.count = 0;
    }
    
    // Adds a node to the front of the list (after the dummy head)
    public void addNodeToFront(Node node) {
        Node currentFirst = head.next; // The node currently after head

        node.prev = head;   // Set the previous pointer of the new node to head
        node.next = currentFirst;   // Set the next pointer of the new node to the current first node
        head.next = node;   // Set the next pointer of head to the new node
        currentFirst.prev = node;   // Set the previous pointer of the current first node to the new node

        count++;            
    }

    // Removes a node from the list
    public void removeNode(Node node) {
        // Check if the node is null or if it's a dummy node (head or tail)
        if (node == null || node == head || node == tail) {
            // Cannot remove dummy nodes or null
           return;
        }

        // Update the pointers of the previous and next nodes
        Node prevNode = node.prev;
        Node nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        count--;

        // Clear pointers of the removed node
        node.prev = null;
        node.next = null;  
    }

    // Adds a new node to the front of the list and returns it
    public Node addToFront(int kay) {
        Node newNode = new Node(kay);
        addNodeToFront(newNode);
        return newNode;
    }

    // Moves a node to the front of the list (after the dummy head)
    public void MoveToFront(Node node) {
        if (node == null || node.prev == head) {
            // Node is null, or already at the front
            return;
        }
        removeNode(node); // Remove from current position
        addNodeToFront(node); // Add back after dummy head
    }
    
    // Removes a node from the list (not the dummy nodes)
    public void remove(Node node) {
        removeNode(node);
    }

    // Returns the dummy head node
    public Node getTailNode() {
        return tail;
    }

    // Returns the actual LRU node (the one just before the dummy tail)
    public Node getLRUNode() {
        if (tail.prev == head) { // List is empty
            return null;
        }
        return tail.prev;
    }

    // Returns the number of nodes in the list
    public int size() {
        return count;
    }

    // Helper for printing the list state (MRU -> LRU)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = head.next;
        while (current != tail) {
            sb.append(current.key);
            if (current.next != tail) {
                sb.append(" <-> ");
            }
            current = current.next;
        }
        return sb.toString();
    }

}

/**
 * LRUCache is a class that implements a Least Recently Used (LRU) Cache.
 * It uses a combination of a HashMap and a Doubly Linked List to efficiently
 * manage cache operations such as access, eviction, and insertion.
 * 
 * The cache has a fixed capacity, and when the capacity is exceeded, the 
 * least recently used page is evicted to make room for new pages.
 * 
 * Key Features:
 * - Cache Hit: If a page is accessed and it exists in the cache, it is moved 
 *   to the front of the list (Most Recently Used position).
 * - Cache Miss: If a page is accessed and it does not exist in the cache, it 
 *   is added to the front of the list. If the cache is full, the least 
 *   recently used page is evicted before adding the new page.
 * - Cache State: The cache maintains the order of pages from Most Recently 
 *   Used (MRU) to Least Recently Used (LRU).
 **/
class LRUCache {
    private Map<Integer, Node> cacheMap; // Maps key to the corresponding node
    private DoublyLinkedList pageList; // Doubly linked list to maintain the order of pages
    private int capacity; // Maximum number of pages that can be stored in the cache

    // Constructor to initialize the cache with a given capacity
    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.cacheMap = new HashMap<>();
        this.pageList = new DoublyLinkedList();
        this.capacity = capacity;
    }

    // Function to access a page with a given key
    public void accessPage(int key) {
        // Check if the page is in the cache. cache hit
        if (cacheMap.containsKey(key)) {
            Node node = cacheMap.get(key);
            // Move the accessed page's node to the front (MRU position)
            System.out.println("Cache Hit: " + key);
            pageList.MoveToFront(node);

        } else {    // cache miss
            System.out.println("Cache Miss: " + key);
            // Check if the cache is full
            if (cacheMap.size() >= capacity) {
                // Evict the LRU page (the one before the dummy tail)
                Node lruNode = pageList.getLRUNode();
                if (lruNode != null) {
                    System.out.println("Cache Full. Evicting: " + lruNode.key);
                    cacheMap.remove(lruNode.key); // Remove from map first
                    pageList.removeNode(lruNode); // Then remove from list
                }
            }
            // Add the new page to the front of the list (MRU position)
            Node newNode = new Node(key);
            pageList.addNodeToFront(newNode); // Add to front of list (MRU)
            cacheMap.put(key, newNode);      // Add to map
        }
        System.out.println("Cache State (MRU->LRU): " + pageList);
        System.out.println("Map Size: " + cacheMap.size());
        System.out.println("---");
    }

    // Function to clear the cache
    public void clearCache() {
        cacheMap.clear();
        pageList = new DoublyLinkedList(); // Reset the list
    }

    // Test the LRU Cache implementation
    public static void main(String[] args) {
        System.out.println("--- Testing LRU Cache ---");
        LRUCache cache = new LRUCache(3); // Capacity of 3

        cache.accessPage(1); // Miss, Add 1. State: 1
        cache.accessPage(2); // Miss, Add 2. State: 2 <-> 1
        cache.accessPage(3); // Miss, Add 3. State: 3 <-> 2 <-> 1 (Full)
        cache.accessPage(2); // Hit, Move 2. State: 2 <-> 3 <-> 1
        cache.accessPage(4); // Miss, Evict 1, Add 4. State: 4 <-> 2 <-> 3
        cache.accessPage(1); // Miss, Evict 3, Add 1. State: 1 <-> 4 <-> 2
        cache.accessPage(2); // Hit, Move 2. State: 2 <-> 1 <-> 4
        cache.accessPage(5); // Miss, Evict 4, Add 5. State: 5 <-> 2 <-> 1
        cache.accessPage(1); // Hit, Move 1. State: 1 <-> 5 <-> 2
        cache.accessPage(2); // Hit, Move 2. State: 2 <-> 1 <-> 5
        cache.accessPage(3); // Miss, Evict 5, Add 3. State: 3 <-> 2 <-> 1
        cache.accessPage(4); // Miss, Evict 1, Add 4. State: 4 <-> 3 <-> 2
        cache.accessPage(5); // Miss, Evict 2, Add 5. State: 5 <-> 4 <-> 3

        System.out.println("\n--- Test Complete ---");

        // Clear the cache
        cache.clearCache();
    }
}