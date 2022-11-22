package com.olehshynkarchuk.task2;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;

public abstract class FileProcessor {

    private final FileProcessor nextFileProcessor;

    public FileProcessor(FileProcessor nextFileProcessor) {
        this.nextFileProcessor = nextFileProcessor;
    }

    public void searchFiles(File filePath, FileParameters fileParameters) {
        if (needToSearchCorrespondingFiles(fileParameters)) {
            walkingOfDirectoryByRecursion(filePath, fileParameters);
        } else if (needToExcludeIrrelevantFiles(fileParameters)) {
            fileParameters.getFileList().removeIf(file -> !isaFileAccordingToConditions(fileParameters, file));
        }
        if (nextFileProcessor != null) nextFileProcessor.searchFiles(filePath, fileParameters);
    }

    private void walkingOfDirectoryByRecursion(File filePath, FileParameters fileParameters) {
        if (filePath.isDirectory()) {
            for (File file : ArrayUtils.nullToEmpty(filePath.listFiles(), File[].class)) {
                if (file.isDirectory()) {
                    walkingOfDirectoryByRecursion(file, fileParameters);
                } else if (isaFileAccordingToConditions(fileParameters, file)) {
                    fileParameters.getFileList().add(file);
                }
            }
        }
    }

    protected abstract boolean needToSearchCorrespondingFiles(FileParameters fileParameters);

    protected abstract boolean needToExcludeIrrelevantFiles(FileParameters fileParameters);

    protected abstract boolean isaFileAccordingToConditions(FileParameters fileParameters, File file);
}
