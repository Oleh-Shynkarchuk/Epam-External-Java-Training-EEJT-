package com.olehshynkarchuk.task2;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class Chain {

    private FileProcessor chain;
    private final Map<Predicate<FileParameters>, Function<FileProcessor, FileProcessor>> chainMapSearching = new LinkedHashMap<>();

    public Chain() {
        buildChain();
    }

    private void buildChain() {
        chainMapSearching.put(parameters -> parameters.getDateEndRange() != 0L, FileProcessorByDateRange::new);
        chainMapSearching.put(parameters -> parameters.getSizeEndRange() != 0L, FileProcessorBySizeRange::new);
        chainMapSearching.put(parameters -> parameters.getFilenameExtensions() != null, FileProcessorByNameExtension::new);
        chainMapSearching.put(parameters -> parameters.getFileName() != null, FileProcessorByName::new);
    }

    public void searchFiles(File filePath, FileParameters fileParameters) {
        if (fileParameters != null) {
            for (Map.Entry<Predicate<FileParameters>, Function<FileProcessor, FileProcessor>> entry : chainMapSearching.entrySet()) {
                if (entry.getKey().test(fileParameters)) {
                    chain = entry.getValue().apply(chain);
                }
            }
            if (chain != null) {
                chain.searchFiles(filePath, fileParameters);
            }
        }
    }

}
