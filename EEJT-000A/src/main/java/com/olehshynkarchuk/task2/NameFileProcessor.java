package com.olehshynkarchuk.task2;

import java.io.File;

public class NameFileProcessor extends FileProcessor {

    private final String filename;

    public NameFileProcessor(FileProcessor next, String filename) {
        super(next);
        this.filename = filename;
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

    private boolean needToSearchCorrespondingFiles(FileParametrs fileArrayList) {
        return filename != null && fileArrayList.getFileList().isEmpty();
    }

    private boolean needToExcludeIrrelevantFiles() {
        return filename != null;
    }

    @Override
    public boolean isaFileAccordingToConditions(File file) {
        return file.getName().toLowerCase().contains(filename.toLowerCase());
    }
}
