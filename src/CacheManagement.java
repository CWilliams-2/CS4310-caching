import java.util.*;

public class CacheManagement {
    private static char readChar(Scanner scanner, String prompt) {
        System.out.println(prompt);
        String s = scanner.next();
        char c = s.charAt(0);
        return c;
    }

    public static void main(String[] args) {
        // Input phase
        Scanner scanner = new Scanner(System.in);
        // System.out.println("Please provide the path to the reference string of file calls");
        // String inputAddress = scanner.nextLine();
        // referenceString = readFile(inputAddress);
        // System.out.println("Please select whether you would like to simulate a disk (b)lock cache or (p)age cache");
        // cacheSetting = scanner.nextLine();
        // System.out.println("Please select the average file size as (s)mall, (l)arge, or (m)ixed");
        // String avgFileSize = scanner.nextLine();


        
        // char cacheType = readChar(scanner, "Please select the replacement algorithm for the cache: F(i)FO, L(r)U, or L(f)U");
        // scanner.close();

        int cacheSize = 128;
        int numVals = 1000;

        System.out.println("Completely random references");
        runMultipleTrials('i', cacheSize, 10 * cacheSize, false, false);
        runMultipleTrials('r', cacheSize, 10 * cacheSize, false, false);
        runMultipleTrials('f', cacheSize, 10 * cacheSize, false, false);

        System.out.println("Compltely random references with prefetching");
        runMultipleTrials('i', cacheSize, 10 * cacheSize, true, false);
        runMultipleTrials('r', cacheSize, 10 * cacheSize, true, false);
        runMultipleTrials('f', cacheSize, 10 * cacheSize, true, false);

        System.out.println("Random references with some locality with prefetching");
        runMultipleTrials('i', cacheSize, 10 * cacheSize, true, true);
        runMultipleTrials('r', cacheSize, 10 * cacheSize, true, true);
        runMultipleTrials('f', cacheSize, 10 * cacheSize, true, true);

        // Cache fifoCache = new FIFOCache(cacheSize);
        // int[] res = simulateCaching(arr, fifoCache);
        // System.out.printf("Cache misses: %d\n", res[0]);
        // System.out.printf("Cache hits: %d\n", res[1]);
        
        // Cache lruCache = new LRUCache(cacheSize);
        // res = simulateCaching(arr, lruCache);
        // System.out.printf("Cache misses: %d\n", res[0]);
        // System.out.printf("Cache hits: %d\n", res[1]);
        
        // cache.print();
    }

    public static int[] simulateCaching(int[] arr, Cache cache, boolean prefetching, int maxVal) {
        int cacheMisses = 0;
        int cacheHits = 0;

        for (int key: arr) {
            // System.out.println(key);
            
            int val = cache.get(key);
            if (val == -1) {
                cacheMisses++;
                if (prefetching) {
                    if (val > 1) {
                        cache.put(key - 1, key - 1);
                    }
                    if (val < maxVal - 1) {
                        cache.put(key + 1, key + 1);
                        cache.put(key + 2, key + 2);
                    }
                }
            } else {
                cacheHits++;
            }
            cache.put(key, key);

            // cache.print();
        }

        int[] res = {cacheMisses, cacheHits};
        return res;
    }

    private static int[] generateRandom(int size, int maxVal) {
        int[] arr = new int[size];
        Random r = new Random(System.currentTimeMillis());
        for (int i=0; i<size; i++) {
            int val = r.nextInt(maxVal);
            arr[i] = val;
        }
        return arr;
    }

    private static int[] generateSemiLocal(int size, int maxVal) {
        int[] arr = new int[size];
        Random r = new Random(System.currentTimeMillis());
        for (int i=0; i<size; i++) {
            int val = r.nextInt(maxVal);
            
            arr[i] = val;
            if (val % 5 == 0) {
                if (i < size - 2) {
                    arr[i+1] = val + 1;
                    arr[i+2] = val + 2;
                    i+= 1;
                }
            } 
        }
        return arr;
    }

    private static int[] generateIncreasing(int size, int maxVal) {
        int[] arr = new int[size];
        int val = 0;
        for (int i=0; i<size; i++) {
            arr[i] = val;
            val = (val + 1) % maxVal;
        }
        return arr;
    }

    // private static int[] generateProbabilistic(int size, int maxVal) {
    //     // stretch
    // }

    public static void runMultipleTrials(char cacheType, int capacity, int maxVal, boolean prefetching, boolean semilocal) {
        System.out.printf("Cache: %c, Capacity: %d, MaxVal: %d\n", cacheType, capacity, maxVal);

        int numTrials = 1000; // hard-code for now
        int numReferences = 1000; // hard-code for now

        // for (int i=0; i<numVals; i++) {
        //     System.out.println(arr[i]);
        // }

        int[] misses = new int[numTrials];
        int[] hits = new int[numTrials];
        for (int i=0; i<numTrials; i++) {
            Cache cache;
            switch(cacheType) {
                case 'i':
                    cache = new FIFOCache(capacity);
                    break;
                case 'r':
                    cache = new LRUCache(capacity);
                    break;
                case 'f':
                    cache = new LFUCache(capacity);
                default:
                    cache = new FIFOCache(capacity);
            }
            
            int[] references;
            if (semilocal) {
                references = generateSemiLocal(numReferences, maxVal);
            }
            else {
                references = generateRandom(numReferences, maxVal);
            }
            int[] res = simulateCaching(references, cache, prefetching, maxVal);
            misses[i] = res[0];
            hits[i] = res[1];
        }

        System.out.printf("Misses: %.2f +- %.3g\n", getMean(misses), getStdev(misses));
        System.out.printf("Hits: %.2f +- %.3g\n", getMean(hits), getStdev(hits));
        System.out.println();
    }

    private static double getMean(int[] arr) {
        int sum = 0;
        for (int val: arr) {
            sum += val;
        }
        return (double) sum / arr.length;
    }

    private static double getStdev(int[] arr) {
        double mean = getMean(arr);
        double sumSquaredDiffs = 0.0;
        for (int val: arr) {
            double diff = val - mean;
            sumSquaredDiffs += diff * diff;
        }
        return Math.sqrt(sumSquaredDiffs / arr.length);
    }
}
