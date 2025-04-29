"""CS 4310.04 Group Project, Spring 2025
    Clifton Williams, Hamin Lee, PeiYu Kuo, Mario Mariotta
    Disk Block/Page Cache Management"""

from preparation import *


"""Input Section of Program"""

# Global Variables
referenceString = str()
cacheSetting = str()
replacementAlgorithm = str()

cache = []
cacheSize = 0
cacheUsed = 0
cacheHits = 0
cacheMisses = 0
fileSystem = {}

def main():
    # input phase
    
    inputAddress = input("Please provide the path to the reference string of file calls")
    inputFile = open(inputAddress, "r")
    referenceString = inputFile.read()
    cacheSetting = input("Please select whether you would like to simulate a disk (b)lock cache or (p)age cache")
    avgFileSize = input("Please select the average file size as (s)mall, (l)arge, or (m)ixed")
    replacementAlgorithm = input("Please select the replacement algorithm for the cache: F(i)FO, L(r)U, or L(f)U")

    # preparation phase
    
    prepareSimulation(referenceString, cacheSetting, avgFileSize)

    # simulation phase

    simulateCaching(referenceString, cache, cacheSize, replacementAlgorithm, fileSystem)



def cacheManager(cacheUnits):
    # u corresponds to the Cache Unit ID
    for u in cacheUnits:
        if u not in cache:
            cacheMisses += 1
            if cacheUsed < cacheSize:
                cache.append(u)
            else:
                replaceCacheUnit(u, True)
        else:
            cacheHits += 1
            if replacementAlgorithm == ("r" | "f"):
                # update the cache list for each unit in cache for LRU and LFU
                replaceCacheUnit(u, False)

def replaceCacheUnit(cacheUnitID : int, cacheMiss : bool):
    if replacementAlgorithm == "i":
        firstInFirstOut(cacheUnitID)
    elif replacementAlgorithm == "f":
        leastFrequentlyUsed(cacheUnitID)
    else:
        leastRecentlyUsed(cacheUnitID)

def simulateCaching(refenceString, cache, cacheSize, replacementAlgorithm, fileSystem):
    # iterate through the file reference string, adding/replacing cache units as needed
    for r in referenceString:
        # for the file reference, get the list of cache units on disk
        cacheUnits = fileSystem[r]
        cacheManager(cacheUnits)