class LFUCache:

    def __init__(self, size: int):
        self.size = size    # Size of cache
        self.cache = {}     # Dictionary stores key/value pairs
        self.freqDict = {}  # Dicionary stores key list for each frequency
        self.keyFreq = {}   # Dictionary stores frequency of each key
        self.minFreq = 0    # Tracks minimum frequency

    # Get the value for a key
    def get(self, key: int) -> int:
        if key not in self.cache:
            return -1
        
        freq = self.keyFreq[key]    # Get key frequency

        # Create frequency group if needed
        if freq not in self.freqDict:
            self.freqDict[freq] = []
        
        self.freqDict[freq].remove(key) # Remove key from current group

        # Delete frequency group if empty
        if not self.freqDict[freq]:
            del self.freqDict[freq]
            if freq == self.minFreq:
                self.minFreq += 1

        # Move key to next frequency group
        if freq+1 not in self.freqDict:
            self.freqDict[freq+1] = []
        
        self.freqDict[freq+1].append(key)
        self.keyFreq[key] = freq + 1

        return self.cache[key]
    
    # Store a key in the cache
    def put(self, key: int, value: int):
        if self.size == 0:
            return
        
        # If key is in cache, update value and frequency
        if key in self.cache:
            self.cache[key] = value
            self.get(key)
            return
        
        # If cache is full, find and remove the least frequently used key
        if len(self.cache) >= self.size:
            leastFreqUsed = self.freqDict[self.minFreq]
            lfuKey = leastFreqUsed[0]
            self.cache.pop(lfuKey)
            self.keyFreq.pop(lfuKey)
            self.freqDict[self.minFreq].remove(lfuKey)
            # If frequency group empty, remove it
            if not self.freqDict[self.minFreq]:
                del self.freqDict[self.minFreq]
                self.minFreq += 1
        
        # Add new key with frequency 1
        self.cache[key] = value
        self.keyFreq[key] = 1

        # Create frequency 1 group in frequency dictionary if it doesnt exist
        if 1 not in self.freqDict:
            self.freqDict[1] = []

        self.freqDict[1].append(key)
        self.minFreq = 1

# For testing
# cache = LFUCache(2)
# cache.put(1, 1)     # Cache: {1=1}
# cache.put(2, 2)     # Cache: {2=2}
# print(cache.get(1)) # Cache: {1=1, 2=2}, should print 1 and increase 1's frequency to 2
# cache.put(3, 3)     # Cache: {1=1, 3=3}, cache is at capacity, and 2 is the LFU, so 3 replaces it
# print(cache.get(2)) # Cache: {1=1, 3=3}, should print -1
# cache.put(4, 4)     # Cache: {1=1, 4=4}, cache is at capacity, and 3 is the LFU, so 4 replaces it
# print(cache.get(3)) # Cache: {1=1, 4=4}, should print -1
# print(cache.get(4)) # Cache: {1=1, 4=4}, should print 4 and increase 4's frequency to 2
# cache.put(5, 5)     # Cache: {4=4, 5=5}, cache is at capacity, both 1 and 4 have a frequency of 2, but 1 is the least recently used, so 5 replaces it
# print(cache.get(1)) # Cache: {4=4, 5=5}, should print -1
# print(cache.get(4)) # Cache: {4=4, 5=5}, should print 4 and increase 4's frequency to 3
# print(cache.get(5)) # Cache: {4=4, 5=5}, should print 5 and increase 5's frequency to 2