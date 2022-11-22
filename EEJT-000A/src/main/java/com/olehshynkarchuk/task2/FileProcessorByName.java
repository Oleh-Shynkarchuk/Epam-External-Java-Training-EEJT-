package com.olehshynkarchuk.task2;

import java.io.File;

public class FileProcessorByName extends FileProcessor {

    public FileProcessorByName(FileProcessor nextFileProcessor) {
        super(nextFileProcessor);
    }

    @Override
    protected boolean needToSearchCorrespondingFiles(FileParameters fileParameters) {
        return fileParameters.getFileName() != null && fileParameters.getFileList().isEmpty();
    }

    @Override
    protected boolean needToExcludeIrrelevantFiles(FileParameters fileParameters) {
        return fileParameters.getFileName() != null;
    }

    @Override
    public boolean isaFileAccordingToConditions(FileParameters fileParameters, File file) {
        return file.getName().toLowerCase().contains(fileParameters.getFileName().toLowerCase());
    }
}
