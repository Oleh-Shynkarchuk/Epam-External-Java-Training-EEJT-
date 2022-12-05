package com.olehshynkarchuk.task.io;

import java.util.Scanner;

public class ConsoleIO {

    public static void println(String message) {
        System.out.println(message);
    }

    public static void printErr(String message) {
        System.err.println(message);
    }

    public Scanner scanner() {
        return new Scanner(System.in);
    }
}
