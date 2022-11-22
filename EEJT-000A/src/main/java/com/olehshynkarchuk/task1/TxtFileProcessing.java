package com.olehshynkarchuk.task1;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TxtFileProcessing {

    private List<String> stringListFromFile;
    private final String filePath;

    public TxtFileProcessing(String filePath) {
        this.filePath = filePath;
    }

    public List<String> getLinesFromList() throws IOException {
        stringListFromFile = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(lines -> stringListFromFile.add(lines));
        } catch (NoSuchFileException ex) {
            throw new NoSuchFileException("File not found : " + ex);
        } catch (IOException e) {
            throw new IOException("Exception while adding row to list : " + e);
        }
        return stringListFromFile;
    }
}
