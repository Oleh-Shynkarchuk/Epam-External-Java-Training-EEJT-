package com.olehshynkarchuk.task2;

import java.io.File;

public class FileProcessorBySizeRange extends FileProcessor {
    FileProcessor fileProcessor;

    public FileProcessorBySizeRange(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }

    @Override
    protected boolean needToExcludeIrrelevantFiles(FileParameters fileParameters) {
        return fileParameters.getSizeEndRange() != 0L;
    }

    @Override
    protected boolean needToSearchCorrespondingFiles(FileParameters fileParameters) {
        return fileParameters.getSizeEndRange() != 0L && fileParameters.getFileList().isEmpty();
    }

    @Override
    public boolean isaFileAccordingToConditions(FileParameters fileParameters, File file) {
        return fileParameters.getSizeStartRange() <= file.length() && file.length() <= fileParameters.getSizeEndRange();
    }
}
