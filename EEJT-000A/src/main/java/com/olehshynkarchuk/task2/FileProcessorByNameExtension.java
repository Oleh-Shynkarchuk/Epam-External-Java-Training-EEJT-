package com.olehshynkarchuk.task2;

import java.io.File;

public class FileProcessorByNameExtension extends FileProcessor {

    public FileProcessorByNameExtension(FileProcessor nextFileProcessor) {
        super(nextFileProcessor);
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
}
