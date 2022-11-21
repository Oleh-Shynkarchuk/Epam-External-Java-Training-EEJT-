package com.olehshynkarchuk.task2;

import java.io.File;

public class FileProcessorByName extends FileProcessor {

    private final String filename;

    public FileProcessorByName(FileProcessor next, String filename) {
        super(next);
        this.filename = filename;
    }

    @Override
    protected boolean needToSearchCorrespondingFiles(FileParametrs fileArrayList) {
        return filename != null && fileArrayList.getFileList().isEmpty();
    }

    @Override
    protected boolean needToExcludeIrrelevantFiles() {
        return filename != null;
    }

    @Override
    public boolean isaFileAccordingToConditions(File file) {
        return file.getName().toLowerCase().contains(filename.toLowerCase());
    }
}
