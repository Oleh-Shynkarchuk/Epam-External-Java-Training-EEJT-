package com.olehshynkarchuk.task2;

import java.io.File;

public class FileProcessorByNameExtension extends FileProcessor {
    FileProcessor fileProcessor;

    public FileProcessorByNameExtension(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @Override
    protected boolean needToExcludeIrrelevantFiles(FileParameters fileParameters) {
        return fileParameters.getFilenameExtensions() != null;
    }

    @Override
    protected boolean needToSearchCorrespondingFiles(FileParameters fileParameters) {
        return fileParameters.getFilenameExtensions() != null && fileParameters.getFileList().isEmpty();
    }

    @Override
    public boolean isaFileAccordingToConditions(FileParameters fileParameters, File file) {
        return file.getName().toLowerCase().endsWith(fileParameters.getFilenameExtensions().toLowerCase());
    }

    @Override
    protected void next(File filePath, FileParameters fileParameters) {
        if (fileProcessor != null) fileProcessor.searchFiles(filePath, fileParameters);
    }
}
