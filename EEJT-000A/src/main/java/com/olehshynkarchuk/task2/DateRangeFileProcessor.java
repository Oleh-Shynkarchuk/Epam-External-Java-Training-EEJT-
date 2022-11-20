package com.olehshynkarchuk.task2;

import java.io.File;

public class DateRangeFileProcessor extends FileProcessor {

    private final long dateStartRange;
    private final long dateEndRange;

    public DateRangeFileProcessor(FileProcessor next, long dateStartRange, long dateEndRange) {
        super(next);
        this.dateStartRange = dateStartRange;
        this.dateEndRange = dateEndRange;
    }

    @Override
    public void searchFiles(File filePath, FileParametrs fileArrayList) {
        if (needToSearchCorrespondingFiles(fileArrayList)) {
            searchForFilesInDirectory(filePath, fileArrayList);
        } else if (needToExcludeIrrelevantFiles()) {
            fileArrayList.getFileList().removeIf(file -> !isaFileAccordingToConditions(file));
        }
        super.searchFiles(filePath, fileArrayList);
    }

    private boolean needToExcludeIrrelevantFiles() {
        return dateEndRange != 0L;
    }

    private boolean needToSearchCorrespondingFiles(FileParametrs fileArrayList) {
        return dateEndRange != 0L && fileArrayList.getFileList().isEmpty();
    }

    @Override
    public boolean isaFileAccordingToConditions(File file) {
        return dateStartRange <= file.lastModified() && file.lastModified() <= dateEndRange;
    }
}
