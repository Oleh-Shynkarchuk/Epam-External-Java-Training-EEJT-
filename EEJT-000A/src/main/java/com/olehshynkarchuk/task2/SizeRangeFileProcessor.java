package com.olehshynkarchuk.task2;

import java.io.File;

public class SizeRangeFileProcessor extends FileProcessor {

    private final long sizeStartRange;
    private final long sizeEndRange;

    public SizeRangeFileProcessor(FileProcessor next, long sizeStartRange, long sizeEndRange) {
        super(next);
        this.sizeStartRange = sizeStartRange;
        this.sizeEndRange = sizeEndRange;
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
        return sizeEndRange != 0L;
    }

    private boolean needToSearchCorrespondingFiles(FileParametrs fileArrayList) {
        return sizeEndRange != 0L && fileArrayList.getFileList().isEmpty();
    }

    @Override
    public boolean isaFileAccordingToConditions(File file) {
        return sizeStartRange <= file.length() && file.length() <= sizeEndRange;
    }
}
