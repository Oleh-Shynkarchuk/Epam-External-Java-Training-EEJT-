package com.olehshynkarchuk.task2;

import java.io.File;

public class FileProcessorByDateRange extends FileProcessor {

    private final long dateStartRange;
    private final long dateEndRange;

    public FileProcessorByDateRange(FileProcessor next, long dateStartRange, long dateEndRange) {
        super(next);
        this.dateStartRange = dateStartRange;
        this.dateEndRange = dateEndRange;
    }

    @Override
    protected boolean needToExcludeIrrelevantFiles() {
        return dateEndRange != 0L;
    }

    @Override
    protected boolean needToSearchCorrespondingFiles(FileParametrs fileArrayList) {
        return dateEndRange != 0L && fileArrayList.getFileList().isEmpty();
    }

    @Override
    protected boolean isaFileAccordingToConditions(File file) {
        return dateStartRange <= file.lastModified() && file.lastModified() <= dateEndRange;
    }
}
