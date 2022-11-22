package com.olehshynkarchuk.task2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;


class ChainTest {
    static String dir = "EEJT-000A";
    Chain chain;
    FileParameters fileParameters;

    @BeforeEach
    void setUp() {
        fileParameters = new FileParameters();
        fileParameters.setFileList(new ArrayList<>());
    }

    @Test
    public void searchFileByNameExtensionsAndSizeParametersTest() {
        String extension = ".txt";
        fileParameters.setFilenameExtensions(extension);
        chain = new Chain(fileParameters);
        chain.searchFiles(new File(dir));
        for (File fileFound : fileParameters.getFileList()) {
            assertTrue(fileFound.getName().endsWith(extension));
        }
        long startRangeSize = 1L;
        long endRangeSize = 65L;
        fileParameters.setSizeStartRange(startRangeSize);
        fileParameters.setSizeEndRange(endRangeSize);
        fileParameters.setFileList(new ArrayList<>());
        chain = new Chain(fileParameters);
        chain.searchFiles(new File(dir));
        for (File fileFound : fileParameters.getFileList()) {
            assertTrue(fileFound.getName().toLowerCase().endsWith(extension.toLowerCase()));
            assertTrue(startRangeSize <= fileFound.length() && fileFound.length() <= endRangeSize);
        }
    }

    @Test
    public void searchFileByNameExtensionsAndDateModifyParametersTest() {
        long startDate = Timestamp.valueOf("2022-11-20 21:30:33").getTime();
        fileParameters.setDateStartRange(startDate);
        long endDate = Timestamp.valueOf("2022-11-22 21:30:33").getTime();
        fileParameters.setDateEndRange(endDate);
        String extension = ".txt";
        fileParameters.setFilenameExtensions(extension);
        chain = new Chain(fileParameters);
        chain.searchFiles(new File(dir));
        for (File fileFound : fileParameters.getFileList()) {
            assertTrue(fileFound.getName().toLowerCase().endsWith(extension.toLowerCase()));
            assertTrue(startDate <= fileFound.lastModified() && fileFound.lastModified() <= endDate);
        }
    }

    @Test
    public void emptyParamSearchTest() {
        fileParameters = new FileParameters();
        fileParameters.setFileList(new ArrayList<>());
        chain = new Chain(fileParameters);
        chain.searchFiles(new File(dir));
        assertTrue(fileParameters.getFileList().isEmpty());
    }

    @Test
    public void searchFileByAllParam() {
        String nameFile = "FILEProcessor";
        String extensionFile = ".JAVA";
        fileParameters.setFileName(nameFile);
        fileParameters.setFilenameExtensions(extensionFile);
        long startDate = Timestamp.valueOf("2022-11-22 14:44:55.215").getTime();
        fileParameters.setDateStartRange(startDate);
        long endDate = Timestamp.valueOf("2022-11-22 14:46:05.958").getTime();
        fileParameters.setDateEndRange(endDate);
        long startRangeSize = 500L;
        fileParameters.setSizeStartRange(startRangeSize);
        long endRangeSize = 1200L;
        fileParameters.setSizeEndRange(endRangeSize);
        chain = new Chain(fileParameters);
        chain.searchFiles(new File(dir));
        for (File fileFound : fileParameters.getFileList()) {
            assertTrue(fileFound.getName().toLowerCase().contains(nameFile.toLowerCase()));
            assertTrue(fileFound.getName().toLowerCase().endsWith(extensionFile.toLowerCase()));
            assertTrue(startRangeSize <= fileFound.length() && fileFound.length() <= endRangeSize);
            assertTrue(startDate <= fileFound.lastModified() && fileFound.lastModified() <= endDate);
        }

    }
}