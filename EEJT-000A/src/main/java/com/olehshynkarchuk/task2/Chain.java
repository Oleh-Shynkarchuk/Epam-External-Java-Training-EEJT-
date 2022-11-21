package com.olehshynkarchuk.task2;

import java.io.File;

public class Chain {

    private FileProcessor chain;

    public Chain(String fileName, String filenameExtensions, long sizeStartRange,
                 long sizeEndRange, long dateStartRange, long dateEndRange) {
        buildChain(fileName, filenameExtensions, sizeStartRange, sizeEndRange, dateStartRange, dateEndRange);
    }

    private void buildChain(String fileName, String filenameExtensions, long sizeStartRange,
                            long sizeEndRange, long dateStartRange, long dateEndRange) {
        chain = new FileProcessorByName(new FileProcessorByNameExtension(new FileProcessorBySizeRange(new FileProcessorByDateRange(null, dateStartRange, dateEndRange), sizeStartRange, sizeEndRange), filenameExtensions), fileName);
    }

    public void searchFiles(File filePath, FileParametrs fileArrayList) {
        chain.searchFiles(filePath, fileArrayList);
    }

}
