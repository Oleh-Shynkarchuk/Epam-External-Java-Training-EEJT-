package com.olehshynkarchuk.task2;

import java.io.File;

public class NameExtensionFileProcessor extends FileProcessor {

    private final String filenameExtension;

    public NameExtensionFileProcessor(FileProcessor next, String filenameExtension) {
        super(next);
        this.filenameExtension = filenameExtension;
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
        return filenameExtension != null;
    }

    private boolean needToSearchCorrespondingFiles(FileParametrs fileArrayList) {
        return filenameExtension != null && fileArrayList.getFileList().isEmpty();
    }

    @Override
    public boolean isaFileAccordingToConditions(File file) {
        return file.getName().toLowerCase().endsWith(filenameExtension.toLowerCase());
    }
}
