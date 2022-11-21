package com.olehshynkarchuk.task2;

import java.io.File;

public abstract class FileProcessor {

    private final FileProcessor nextFileProcessor;

    public FileProcessor(FileProcessor nextFileProcessor) {
        this.nextFileProcessor = nextFileProcessor;
    }

    public void searchFiles(File filePath, FileParametrs fileArrayList) {
        if (needToSearchCorrespondingFiles(fileArrayList)) {
            if (filePath.isDirectory()) {
                File[] directoryFiles = filePath.listFiles();
                if (directoryFiles != null) {
                    for (File file : directoryFiles) {
                        if (file.isDirectory()) {
                            searchFiles(file, fileArrayList);
                        } else if (isaFileAccordingToConditions(file)) {
                            fileArrayList.getFileList().add(file);
                        }
                    }
                }
            }
        } else if (needToExcludeIrrelevantFiles()) {
            fileArrayList.getFileList().removeIf(file -> !isaFileAccordingToConditions(file));
        }
        if (nextFileProcessor != null)
            nextFileProcessor.searchFiles(filePath, fileArrayList);
    }

    protected abstract boolean needToSearchCorrespondingFiles(FileParametrs fileArrayList);

    protected abstract boolean needToExcludeIrrelevantFiles();

    protected abstract boolean isaFileAccordingToConditions(File file);
}
