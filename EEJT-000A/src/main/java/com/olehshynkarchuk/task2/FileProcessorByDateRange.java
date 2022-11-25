package com.olehshynkarchuk.task2;

import java.io.File;

public class FileProcessorByDateRange extends FileProcessor {

    public FileProcessorByDateRange(FileProcessor nextFileProcessor) {
        super(nextFileProcessor);
    }

    @Override
    protected boolean needToExcludeIrrelevantFiles(FileParameters fileParameters) {
        return fileParameters.getDateEndRange() != 0L;
    }

    @Override
    protected boolean needToSearchCorrespondingFiles(FileParameters fileParameters) {
        return fileParameters.getDateEndRange() != 0L && fileParameters.getFileList().isEmpty();
    }

    @Override
    protected boolean isaFileAccordingToConditions(FileParameters fileParameters, File file) {
        return fileParameters.getDateStartRange() <= file.lastModified() && file.lastModified() <= fileParameters.getDateEndRange();
    }
}
