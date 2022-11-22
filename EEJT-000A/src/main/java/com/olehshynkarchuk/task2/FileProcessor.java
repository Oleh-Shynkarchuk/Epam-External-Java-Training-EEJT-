package com.olehshynkarchuk.task2;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;

public abstract class FileProcessor {

    FileProcessor nextFileProcessor;

    protected FileProcessor() {
    }

    public void searchFiles(File filePath, FileParameters fileParameters) {
        if (needToSearchCorrespondingFiles(fileParameters)) {
            if (filePath.isDirectory()) {
                File[] directoryFiles = (File[]) ArrayUtils.nullToEmpty(filePath.listFiles());
                for (File file : directoryFiles) {
                    if (file.isDirectory()) {
                        searchFiles(file, fileParameters);
                    } else if (isaFileAccordingToConditions(fileParameters, file)) {
                        fileParameters.getFileList().add(file);
                    }
                }
            }
        } else if (needToExcludeIrrelevantFiles(fileParameters)) {
            fileParameters.getFileList().removeIf(file -> !isaFileAccordingToConditions(fileParameters, file));
        }
        if (nextFileProcessor != null)
            nextFileProcessor.searchFiles(filePath, fileParameters);
    }

    protected abstract boolean needToSearchCorrespondingFiles(FileParameters fileParameters);

    protected abstract boolean needToExcludeIrrelevantFiles(FileParameters fileParameters);

    protected abstract boolean isaFileAccordingToConditions(FileParameters fileParameters, File file);
}
