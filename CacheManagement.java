import java.util.*;
import LFUCache;

public class CacheManagement {
    
    // Global Variables
    static String referenceString = "";
    static String cacheSetting = "";
    static String replacementAlgorithm = "";

    static List<Integer> cache = new ArrayList<>();
    static int cacheSize = 0;
    static int cacheUsed = 0;
    static int cacheHits = 0;
    static int cacheMisses = 0;
    static Map<Character, List<Integer>> fileSystem = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input phase
        System.out.println("Please provide the path to the reference string of file calls");
        String inputAddress = scanner.nextLine();
        referenceString = readFile(inputAddress);
        System.out.println("Please select whether you would like to simulate a disk (b)lock cache or (p)age cache");
        cacheSetting = scanner.nextLine();
        System.out.println("Please select the average file size as (s)mall, (l)arge, or (m)ixed");
        String avgFileSize = scanner.nextLine();
        System.out.println("Please select the replacement algorithm for the cache: F(i)FO, L(r)U, or L(f)U");
        replacementAlgorithm = scanner.nextLine();

        // Preparation phase
        prepareSimulation(referenceString, cacheSetting, avgFileSize);

        // Simulation phase
        simulateCaching(referenceString, cache, cacheSize, replacementAlgorithm, fileSystem);
    }

    public static String readFile(String path) {
        // Implement file reading logic here
        return "";
    }

    public static void prepareSimulation(String referenceString, String cacheSetting, String avgFileSize) {
        // Implement preparation logic here
    }

    public static void simulateCaching(String referenceString, List<Integer> cache, int cacheSize, String replacementAlgorithm, Map<Character, List<Integer>> fileSystem) {
        // Iterate through the file reference string, adding/replacing cache units as needed
        for (char r : referenceString.toCharArray()) {
            // For the file reference, get the list of cache units on disk
            List<Integer> cacheUnits = fileSystem.get(r);
            cacheManager(cacheUnits);
        }
    }

    public static void cacheManager(List<Integer> cacheUnits) {
        // u corresponds to the Cache Unit ID
        for (int u : cacheUnits) {
            if (!cache.contains(u)) {
                cacheMisses++;
                if (cacheUsed < cacheSize) {
                    cache.add(u);
                } else {
                    replaceCacheUnit(u, true);
                }
            } else {
                cacheHits++;
                if (replacementAlgorithm.equals("r") || replacementAlgorithm.equals("f")) {
                    // Update the cache list for each unit in cache for LRU and LFU
                    replaceCacheUnit(u, false);
                }
            }
        }
    }

    public static void replaceCacheUnit(int cacheUnitID, boolean cacheMiss) {
        switch (replacementAlgorithm) {
            case "i":
                firstInFirstOut(cacheUnitID);
                break;
            case "f":
                leastFrequentlyUsed(cacheUnitID);
                break;
            default:
                leastRecentlyUsed(cacheUnitID);
                break;
        }
    }

    public static void firstInFirstOut(int cacheUnitID) {
        // Implement FIFO logic here
    }

    public static void leastFrequentlyUsed(int cacheUnitID) {
        // Implement LFU logic here
    }

    public static void leastRecentlyUsed(int cacheUnitID) {
        // Implement LRU logic here
    }
}
