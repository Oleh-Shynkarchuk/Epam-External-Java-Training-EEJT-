package com.olehshynkarchuk.task2;

import java.io.File;

public class FileProcessorByName extends FileProcessor {
    FileProcessor fileProcessor;

    public FileProcessorByName(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
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

    @Override
    protected void next(File filePath, FileParameters fileParameters) {
        if (fileProcessor != null) fileProcessor.searchFiles(filePath, fileParameters);
    }
}
