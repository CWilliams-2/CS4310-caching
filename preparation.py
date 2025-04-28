"""CS 4310.04 Group Project, Spring 2025
    Clifton Williams, Hamin Lee, PeiYu Kuo, Mario Mariotta
    Disk Block/Page Cache Management"""

from random import *
import numpy
import main

def prepareSimulation(referenceString : str, cacheSetting : int, avgFileSize):
    # determine the number of files by making a set out of the reference string
    fileList = set(referenceString)
    numFiles = len(fileList)

    setFileSizes(fileList, avgFileSize)


# this builds the fileSystem dictionary
def setFileSizes(fileList : set, avgFileSize : str):
    for f in fileList:
        if avgFileSize == "s":
            fFileSize = 
        elif avgFileSize == "l":
            fFileSize =
        else:
            fFileSize = 
        main.fileSystem[f] = fFileSize
