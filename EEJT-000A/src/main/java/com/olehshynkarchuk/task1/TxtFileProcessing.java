package com.olehshynkarchuk.task1;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TxtFileProcessing {

    private FileReader fileReader;
    private String[] arrayOfStringFromTxt;
    private String fileNamePath;

    public TxtFileProcessing(String fileNamePath) {
        this.fileNamePath = fileNamePath;
    }

    public String[] getLinesFromArray() throws TxtFileProcessingException {
        arrayOfStringFromTxt = arrayLengthCount();
        return addLinesToArray();
    }

    private String[] addLinesToArray() throws TxtFileProcessingException {
        try {
            fileReader = new FileReader(fileNamePath);
            int nextLine = 0;
            StringBuilder chars = new StringBuilder();
            while (fileReader.ready()) {
                int symbol = fileReader.read();
                if (isaNextLineOrLastSymbol((char) symbol)) {
                    if (!fileReader.ready()) {
                        chars.append((char) symbol);
                    }
                    arrayOfStringFromTxt[nextLine] = chars.toString();
                    nextLine++;
                    chars = new StringBuilder();
                } else {
                    chars.append((char) symbol);
                }
            }
        } catch (FileNotFoundException ex) {
            throw new TxtFileProcessingException("File not found");
        } catch (IOException e) {
            throw new TxtFileProcessingException("Exception while adding row to array");
        }
        return arrayOfStringFromTxt;
    }

    private boolean isaNextLineOrLastSymbol(char symbol) throws IOException {
        return symbol == '\n' || !fileReader.ready();
    }

    private String[] arrayLengthCount() throws TxtFileProcessingException {
        try {
            fileReader = new FileReader(fileNamePath);
            int size = 0;
            while (fileReader.ready()) {
                int symbol = fileReader.read();
                if (isaNextLineOrLastSymbol((char) symbol)) {
                    size++;
                }
            }
            fileReader.close();
            return new String[size];
        } catch (FileNotFoundException ex) {
            throw new TxtFileProcessingException("File not found");
        } catch (IOException e) {
            throw new TxtFileProcessingException("Exception while counting array size");
        }
    }
}
