package com.olehshynkarchuk;

import com.olehshynkarchuk.task1.TxtFileProcessing;
import com.olehshynkarchuk.task2.Chain;
import com.olehshynkarchuk.task2.FileParameters;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        /* ------- Task1 ---------*/

        TxtFileProcessing txtFileProcessing = new TxtFileProcessing("EEJT-000A\\Bigtext.txt");
        for (String arrayString : txtFileProcessing.getLinesFromList()) {
            System.out.println(arrayString);
        }

        /* ------- Task2 ---------*/


        boolean work = true;
        while (work) {
            FileParameters fileParameters = new FileParameters();
            fileParameters.setFileList(new ArrayList<>());
            int step;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Шукати по імені файлу?(0/1) ");
            step = scanner.nextInt();
            scanner.nextLine();
            switch (step) {
                case 0 -> System.out.println("Пошук за іменем файлу не відбуватиметься.");
                case 1 -> {
                    System.out.println("Назва файлу : ");
                    fileParameters.setFileName(scanner.nextLine());
                }
                default -> throw new IllegalStateException("Неочікуване введення даних: " + step);
            }
            System.out.println("Шукати за форматом файла?(0/1) ");
            step = scanner.nextInt();
            scanner.nextLine();
            switch (step) {
                case 0 -> System.out.println("Пошук за форматом не відбуватиметься.");
                case 1 -> {
                    System.out.println("Формат (.docx) крапка обовязкова : ");
                    fileParameters.setFilenameExtensions(scanner.nextLine());
                    while (!fileParameters.getFilenameExtensions().matches("^[^a-zA-Z0-9,]*\\.[a-zA-Z0-9,]*$")) {
                        System.out.println("Невірний формат, необхідно вказати формат з допомогою крапки : ");
                        fileParameters.setFilenameExtensions(scanner.nextLine());
                    }
                }
                default -> throw new IllegalStateException("Неочікуване введення даних: " + step);
            }
            System.out.println("Шукати за розміром файла?(0/1) ");
            step = scanner.nextInt();
            scanner.nextLine();
            switch (step) {
                case 0 -> System.out.println("Пошук за розіром файлу не відбуватиметься.");
                case 1 -> {
                    while (fileParameters.getSizeStartRange() >= fileParameters.getSizeEndRange()) {
                        System.out.println("Зверніть увагу кінцева межа пошуку за розміром не може бути меньшою за початкову.");
                        System.out.println("Початкова межа пошуку (у байтах) : ");
                        fileParameters.setSizeStartRange(scanner.nextLong());
                        System.out.println("Кінцева межа пошуку (у байтах) : ");
                        fileParameters.setSizeEndRange(scanner.nextLong());
                    }
                }
            }
            System.out.println("Шукати за датою модифікації файла?(0/1) ");
            step = scanner.nextInt();
            scanner.nextLine();
            switch (step) {
                case 0 -> System.out.println("Пошук за датою модифікації файлу не відбуватиметься.");
                case 1 -> {
                    while (fileParameters.getDateStartRange() >= fileParameters.getDateEndRange()) {
                        System.out.println("Початкова межа дати повинна бути старшою за кінцеву");
                        System.out.println("Введіть початкову межу дати модифікації (формат yyyy-MM-dd HH:mm:ss) ");
                        fileParameters.setDateStartRange(Timestamp.valueOf(scanner.nextLine()).getTime());
                        System.out.println("Введіть кінцеву межу дати модифікації (формат yyyy-MM-dd HH:mm:ss) ");
                        fileParameters.setDateEndRange(Timestamp.valueOf(scanner.nextLine()).getTime());
                    }
                }
            }
            Chain chain = new Chain(fileParameters);
            chain.searchFiles(new File("EEJT-000A"));
            for (File f : fileParameters.getFileList()) {
                Timestamp t = new Timestamp(f.lastModified());
                System.out.println("\n" + f.getAbsolutePath());
                System.out.println("Останій раз модифікований : " + t);
                System.out.println("Розмір :" + f.length() + " байт");
            }
            System.out.println("Продовжити пошук?(0/1) ");
            step = scanner.nextInt();
            if (step == 0) {
                work = false;
            }
            scanner.nextLine();
        }
    }
}

