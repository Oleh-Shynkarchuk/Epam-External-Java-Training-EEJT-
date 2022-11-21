package com.olehshynkarchuk.task2;

import java.io.File;

public class FileProcessorBySizeRange extends FileProcessor {

    private final long sizeStartRange;
    private final long sizeEndRange;

    public FileProcessorBySizeRange(FileProcessor next, long sizeStartRange, long sizeEndRange) {
        super(next);
        this.sizeStartRange = sizeStartRange;
        this.sizeEndRange = sizeEndRange;
    }

    @Override
    protected boolean needToExcludeIrrelevantFiles() {
        return sizeEndRange != 0L;
    }

    @Override
    protected boolean needToSearchCorrespondingFiles(FileParametrs fileArrayList) {
        return sizeEndRange != 0L && fileArrayList.getFileList().isEmpty();
    }

    @Override
    public boolean isaFileAccordingToConditions(File file) {
        return sizeStartRange <= file.length() && file.length() <= sizeEndRange;
    }
}
