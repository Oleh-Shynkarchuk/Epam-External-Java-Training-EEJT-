package com.olehshynkarchuk.task2;

import java.io.File;

public abstract class FileProcessor {

    private final FileProcessor next;

    public FileProcessor(FileProcessor next) {
        this.next = next;
    }

    public void searchFiles(File filePath, FileParametrs fileArrayList) {
        if (next != null)
            next.searchFiles(filePath, fileArrayList);
    }

    public void searchForFilesInDirectory(File filePath, FileParametrs fileArrayList) {
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
    }

    public abstract boolean isaFileAccordingToConditions(File file);
}
