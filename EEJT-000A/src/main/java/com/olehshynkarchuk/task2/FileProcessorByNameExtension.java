package com.olehshynkarchuk.task2;

import java.io.File;

public class FileProcessorByNameExtension extends FileProcessor {

    private final String filenameExtension;

    public FileProcessorByNameExtension(FileProcessor next, String filenameExtension) {
        super(next);
        this.filenameExtension = filenameExtension;
    }

    @Override
    protected boolean needToExcludeIrrelevantFiles() {
        return filenameExtension != null;
    }

    @Override
    protected boolean needToSearchCorrespondingFiles(FileParametrs fileArrayList) {
        return filenameExtension != null && fileArrayList.getFileList().isEmpty();
    }

    @Override
    public boolean isaFileAccordingToConditions(File file) {
        return file.getName().toLowerCase().endsWith(filenameExtension.toLowerCase());
    }
}
