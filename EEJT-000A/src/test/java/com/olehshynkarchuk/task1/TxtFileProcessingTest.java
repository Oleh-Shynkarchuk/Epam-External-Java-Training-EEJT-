package com.olehshynkarchuk.task1;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TxtFileProcessingTest {

    TxtFileProcessing txtFileProcessing;
    List<String> array;

    @Test
    void fullGetArrayStringFromFileTest() throws IOException {
        txtFileProcessing = new TxtFileProcessing("EEJT-000A\\test.txt");
        array = txtFileProcessing.getLinesFromList();
        assertEquals(3, array.size());
        assertEquals("simple string", array.get(0));
        assertEquals("for checking the correct operation", array.get(1));
        assertEquals(" of the method", array.get(2));
    }

    @Test
    void incorrectFilePathTest() {
        txtFileProcessing = new TxtFileProcessing("EEJT-000A\\Nonexistent.txt");
        assertThrows(NoSuchFileException.class, () -> txtFileProcessing.getLinesFromList());
    }

    @Test
    void emptyFileTest() throws IOException {
        txtFileProcessing = new TxtFileProcessing("EEJT-000A\\empty.txt");
        array = txtFileProcessing.getLinesFromList();
        assertEquals(0, array.size());
        assertEquals("[]", array.toString());
    }
}