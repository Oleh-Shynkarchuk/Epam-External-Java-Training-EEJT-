package com.olehshynkarchuk.task2;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class Chain {

    private final FileParameters parameters;
    private FileProcessor chain;
    private final Map<Predicate<FileParameters>, Function<FileProcessor, FileProcessor>> chainProcessorMap = new LinkedHashMap<>();

    public Chain(FileParameters parameters) {
        this.parameters = parameters;
        buildChain();
    }

    private void buildChain() {
        chainProcessorMap.put(parameters -> parameters.getDateEndRange() != 0L, FileProcessorByDateRange::new);
        chainProcessorMap.put(parameters -> parameters.getSizeEndRange() != 0L, FileProcessorBySizeRange::new);
        chainProcessorMap.put(parameters -> parameters.getFilenameExtensions() != null, FileProcessorByNameExtension::new);
        chainProcessorMap.put(parameters -> parameters.getFileName() != null, FileProcessorByName::new);
    }

    public void searchFiles(File filePath) {
        if (parameters != null) {
            for (Map.Entry<Predicate<FileParameters>, Function<FileProcessor, FileProcessor>> entry : chainProcessorMap.entrySet()) {
                if (entry.getKey().test(parameters)) {
                    chain = entry.getValue().apply(chain);
                }
            }
            if (chain != null) {
                chain.searchFiles(filePath, parameters);
            }
        }
    }

}
