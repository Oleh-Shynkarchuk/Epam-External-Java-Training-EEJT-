package com.olehshynkarchuk.task1;


import java.io.FileReader;
import java.io.IOException;

public class TxtProcessing {

    private FileReader fileReader;
    private String[] arr;
    private String filename;

    public TxtProcessing(String filename) {
        this.filename = filename;
    }

    public String[] getLinesFromArray() {
        arr = arraySizeCount();
        return addLinesToArray();
    }

    private String[] addLinesToArray() {
        try {
            fileReader = new FileReader(filename);
            int nextLine = 0;
            StringBuilder chars = new StringBuilder();
            while (fileReader.ready()) {
                int symbol = fileReader.read();
                if (isaNextLineOrLastSymbol((char) symbol)) {
                    if (!fileReader.ready()) {
                        chars.append((char) symbol);
                    }
                    arr[nextLine] = chars.toString();
                    nextLine++;
                    chars = new StringBuilder();
                } else {
                    chars.append((char) symbol);
                }
            }
        } catch (IOException e) {
            System.out.println("addLinesToArray Error");
        }
        return arr;
    }

    private boolean isaNextLineOrLastSymbol(char symbol) throws IOException {
        return symbol == '\n' || !fileReader.ready();
    }

    private String[] arraySizeCount() {
        try {
            fileReader = new FileReader(filename);
            int size = 0;
            while (fileReader.ready()) {
                int symbol = fileReader.read();
                if (isaNextLineOrLastSymbol((char) symbol)) {
                    size++;
                }
            }
            fileReader.close();
            return new String[size];
        } catch (IOException e) {
            System.out.println("arraySizeCount error");
        }
        return new String[0];
    }
}
