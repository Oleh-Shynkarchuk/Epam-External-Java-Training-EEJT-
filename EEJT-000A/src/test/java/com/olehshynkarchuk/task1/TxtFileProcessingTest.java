package com.olehshynkarchuk.task1;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class TxtFileProcessingTest {

    TxtFileProcessing txtFileProcessing;
    String[] array;

    @Test
    void fullGetArrayStringFromFileTest() throws TxtFileProcessingException {
        txtFileProcessing = new TxtFileProcessing("EEJT-000A\\test.txt");
        array = txtFileProcessing.getLinesFromArray();
        assertEquals(3, array.length);
        assertEquals("simple string\r", array[0]);
        assertEquals("for checking the correct operation\r", array[1]);
        assertEquals(" of the method", array[2]);
    }

    @Test
    void incorrectFilePathTest() {
        txtFileProcessing = new TxtFileProcessing("EEJT-000A\\Nonexistent.txt");
        TxtFileProcessingException exception = assertThrows(TxtFileProcessingException.class, () -> txtFileProcessing.getLinesFromArray());
        assertEquals("File not found", exception.getMessage());
    }

    @Test
    void emptyFileTest() throws TxtFileProcessingException {
        txtFileProcessing = new TxtFileProcessing("EEJT-000A\\empty.txt");
        array = txtFileProcessing.getLinesFromArray();
        assertEquals(0, array.length);
        assertEquals("[]", Arrays.toString(array));
    }
}